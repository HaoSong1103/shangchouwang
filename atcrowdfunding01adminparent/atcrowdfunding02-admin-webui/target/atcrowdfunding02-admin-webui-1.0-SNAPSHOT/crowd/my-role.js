//执行分页,生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
    //1.获取分页数据
    var pageInfo = getPageInfoRemote();

    //2.填充表格
    fillTableBody(pageInfo);

    $("#summaryBox").prop("checked",false);
}
//远程访问服务器端程序获取PageInfo数据
function getPageInfoRemote() {
    // 调用$.ajax()函数发 送请求并接受 $.ajax()函数的返回值
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,
        "dataType": "json"
    });

    console.log(ajaxResult);
    var statusCode = ajaxResult.status;
    if (statusCode != 200) {
        layer.msg("服务器端程序调用失败！相应状态码=" + statusCode + ",说明信息=" + ajaxResult.statusText);
        return null;
    }
    // 如果响应状态码是 200，说明请求处理成功，获取 pageInfo
    var resultEntity = ajaxResult.responseJSON;

    // 从 resultEntity中获取 result属性
    var result = resultEntity.result;

    // 判断 result是否成功
    if (result == "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }
    // 确认 result为成功后获取 pageInfo
    var pageInfo = resultEntity.date;
    // 返回 pageInfo
    console.log(pageInfo);
    return pageInfo;
}

//填充表格
function fillTableBody(pageInfo) {
    //清除 tbody中的旧的内容
    $("#rolePageBody").empty();
    //这里清空是为了让没有搜索结果时也没有页码
    $("#Pagination").empty();

    //判断pageInfo是否有效
    if (pageInfo ==null || pageInfo == undefined ||pageInfo.list == null || pageInfo.list.length ==0){
        $("#rolePageBody").append("<tr> <td colspan='4' align='center'>抱歉!没有查询到你搜索的数据!</td></tr>");
        return ;
    }
    //使用pageInfo的list属性填充tbody部分
    for (var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;

        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input class='itemBox' type='checkbox'></td>";
        var roleNameTd = "<td>"+roleName+"</td>";
        var checkBtn = "<button type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>";
        var tr = "<tr id='"+roleId+"'>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>";
        $("#rolePageBody").append(tr);
        // 生成分页导航条
        generateNavigator(pageInfo);
    }

}
//生成分页页码导航条
function generateNavigator(pageInfo) {
    // 获取总记录数
        var totalRecord = pageInfo.total;
    // 声明相关属性
        var properties = {
            "num_edge_entries": 3,
            "num_display_entries": 5,
            "callback": paginationCallBack,
            "items_per_page": pageInfo.pageSize,
            "current_page": pageInfo.pageNum-1,
            "prev_text": "上一页 ",
            "next_text": "下一页 "
        }
    // 调用 pagination()函数
        $("#Pagination").pagination(totalRecord, properties);
}

//翻页时的回调函数
function paginationCallBack(pageIndex, jQuery) {
    // 修改 window对象的 pageNum属性
    window.pageNum = pageIndex + 1;
    // 调用分页函数
    generatePage();
    // 取消页码超链接的默认行为
    return false;
}

function showConfirmModel(roleArray) {
    $("#confirmModal").modal("show");
    $("#roleNameDiv").empty();

    window.roleIdArray = [];
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName+"<br/>");
        var roleId = role.roleId;
        // 调用数组对象的 push()方法存入新元素
        window.roleIdArray.push(roleId);
    }
}

function fillAuthTree() {
     var ajaxReturn = $.ajax({
         "url": "assign/get/all/auth.json",
         "type": "post",
         "dataType": "json",
         "async":false
     });
    if(ajaxReturn.status != 200) {
        layer.msg(" 请求处理出错！ 响应状态码是： "+ajaxReturn.status+" 说明是："+ajaxReturn.statusText);
        return ;
    }
    // 2.从响应结果中获取Auth 的JSON 数据
    // 从服务器端查询到的list 不需要组装成树形结构，这里我们交给zTree 去组装
    var authList = ajaxReturn.responseJSON.date;

    // 3.准备对zTree 进行设置的JSON 对象
    var setting = {
        "data": {
            "simpleData": {
            // 开启简单JSON 功能
                "enable": true,
            // 使用categoryId 属性关联父节点，不用默认的pId 了
                "pIdKey": "categoryId"
            },
            "key": {
            // 使用title 属性显示节点名称，不用默认的name 作为属性名了
                "name": "title"
            }
        },
        "check": {
            "enable": true
        }
    };

    // 4.生成树形结构
    // <ul id="authTreeDemo" class="ztree"></ul>
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);

    // 获取zTreeObj 对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

    // 调用zTreeObj 对象的方法，把节点展开
    zTreeObj.expandAll(true);

    //5.查询已分配的Auth 的id 组成的数组
    ajaxReturn = $.ajax({
        "url":"assign/get/assigned/auth/id/by/role/id.json",
        "type":"post",
        "data":{
            "roleId":window.id
        },
        "dataType":"json",
        "async":false
    });
    if(ajaxReturn.status != 200) {
        layer.msg(" 请求处理出错！ 响应状态码是： "+ajaxReturn.status+" 说明是："+ajaxReturn.statusText);
        return ;
    }
    // 从响应结果中获取authIdArray
    var authIdArray = ajaxReturn.responseJSON.date;
    // 6.根据authIdArray 把树形结构中对应的节点勾选上
    // ①遍历authIdArray
    for(var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        // ②根据id 查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        // ③将treeNode 设置为被勾选
        // checked 设置为true 表示节点勾选
        var checked = true;
        // checkTypeFlag 设置为false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;
        // 执行
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}
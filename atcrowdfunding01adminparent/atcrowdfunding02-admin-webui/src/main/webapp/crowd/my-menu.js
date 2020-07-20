function myAddDiyDom(treeId,treeNode) {
    console.log("treeId="+treeId);
    // 当前树形节点的全部的数据，包括从后端查询得到的Menu 对象的全部属性
    console.log(treeNode);

    var spanId = treeNode.tId + "_ico";
    // 1.根据控制图标的span 标签的id 找到这个span 标签
    // 2.删除旧的class
    // 3.添加新的class
    $("#"+spanId).removeClass().addClass(treeNode.icon);
}

//鼠标移入结点范围时添加按钮组
function myAddHoverDom(treeId,treeNode) {
    var btnGroupId = treeNode.tId + "_btnGrp";

    if($("#"+btnGroupId).length > 0) {
        return ;
    }

    var editBtn = "<a id='"+treeNode.id+"' class=\'btn btn-info dropdown-toggle editBtn btn-xs\' style=\'margin-left:10px;padding-top:0px;\' href=\'#\' title=\'修改节点\'>&nbsp;&nbsp;<i class=\'fa fa-fw fa-edit rbg \'></i></a>";
    var removeBtn = "<a id='"+treeNode.id+"' class=\'btn btn-info dropdown-toggle removeBtn btn-xs\' style=\'margin-left:10px;padding-top:0px;\' href=\'#\' title=\'删除节点\'>&nbsp;&nbsp;<i class=\'fa fa-fw fa-times rbg \'></i></a>";
    var addBtn = "<a id='"+treeNode.id+"' class=\'btn btn-info dropdown-toggle addBtn btn-xs\' style=\'margin-left:10px;padding-top:0px;\' href=\'#\' title=\'添加子节点\'>&nbsp;&nbsp;<i class=\'fa fa-fw fa-plus rbg \'></i></a>";

    var level = treeNode.level;
    // 声明变量存储拼装好的按钮代码
    var btnHTML = "";

    // 判断当前节点的级别
    if(level == 0) {
    // 级别为0 时是根节点，只能添加子节点
        btnHTML = addBtn;
    }

    if(level == 1) {
    // 级别为1时是分支节点，可以添加子节点、修改
        btnHTML = addBtn + " " + editBtn;
    // 获取当前节点的子节点数量
        var length = treeNode.children.length;
    // 如果没有子节点，可以删除
        if(length == 0) {
            btnHTML = btnHTML + " " + removeBtn;
        }
    }

    if(level == 2) {
    // 级别为2时是叶子节点，可以修改、删除
        btnHTML = editBtn + " " + removeBtn;
    }

    // 找到附着按钮组的超链接
    var anchorId = treeNode.tId + "_a";
    // 执行在超链接后面附加span 元素的操作
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btnHTML+"</span>");

}

//鼠标移出结点范围时删除按钮组
function myRemoveHoverDom(treeId,treeNode) {
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 移除对应的元素
    $("#"+btnGroupId).remove();
}

function generateTree() {
    $.ajax({
        "url": "menu/get/tree.json",
        "type":"post",
        "dataType":"json",
        "success":function(response){
            var result = response.result;
            if(result == "SUCCESS") {
                // 2.创建JSON 对象用于存储对zTree 所做的设置
                var setting = {
                    "view":{
                        "addDiyDom":myAddDiyDom,
                        "addHoverDom":myAddHoverDom,
                        "removeHoverDom":myRemoveHoverDom
                    },
                    "data":{
                        "key":{
                            "url":"maomixx"
                        }
                    }
                };
                // 3.从响应体中获取用来生成树形结构的JSON 数据
                var zNodes = response.date;
                // 4.初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            if(result == "FAILED") {
                layer.msg(response.message);
            }
        }
    });

    //给添加图标绑定单击响应函数，显示模态框
    $("#treeDemo").on("click",".addBtn",function () {
        //获取当前按钮的id，
        window.pid = this.id;
        //显示模态框
        $("#menuAddModal").modal("show");
        return false;
    });

    //给编辑图标绑定单击响应函数，显示模态框
    $("#treeDemo").on("click",".editBtn",function () {
        //获取当前按钮的父节点的父节点的id，即为：treeDemo_xxx
        window.id = this.id;


        //显示模态框
        $("#menuEditModal").modal("show");

        //获取zTreeObj这个对象
        var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

        //根据id属性查询节点对象
        //用来搜索节点属性名
        var key = "id";

        //用来搜索节点属性值
        var currentNode = zTreeObj.getNodeByParam(key,window.id);

        //回显表单数据
        $("#menuEditModal [name=name]").val(currentNode.name);
        $("#menuEditModal [name=url]").val(currentNode.url);

        // 回显radio 可以这样理解：被选中的radio 的value 属性可以组成一个数组，
        // 然后再用这个数组设置回radio，就能够把对应的值选中
        $("#menuEditModal [name=icon]").val([currentNode.icon]);

        return false;
    });

    //给添加模态框的添加按钮绑定单击响应函数
    $("#menuSaveBtn").click(function () {
        // 收集表单项中用户输入的数据
        var name = $.trim($("#menuAddModal [name=name]").val());
        var url = $.trim($("#menuAddModal [name=url]").val());
        // 单选按钮要定位到“被选中”的那一个
        var icon = $("#menuAddModal [name=icon]:checked").val();

        //发送ajax请求
        $.ajax({
            "url": "menu/save.json",
            "type": "post",
            "data": {
                "pid":window.pid,
                "name":name,
                "url":url,
                "icon":icon
            },
            "async": false,
            "dataType": "json",
            "success": function (response) {
                var result = response.result;
                if (result == "SUCCESS") {
                    layer.msg("操作成功");
                    //重新加载树形结构,确认服务器端完成保存操作后在刷新
                    generateTree();
                }
                if (result == "FAILED") {
                    layer.msg("操作失败" + response.message);
                }
            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });

        //关闭模态框
        $("#menuAddModal").modal("hide");

        //清空表单，click不传参数相当于用户点击了一次
        $("#menuResetBtn").click();

    });

    //给修改模态框的修改按钮绑定单击响应函数
    $("#menuEditBtn").click(function(){
        // 收集表单数据
        var name = $("#menuEditModal [name=name]").val();
        var url = $("#menuEditModal [name=url]").val();
        var icon = $("#menuEditModal [name=icon]:checked").val();
        // 发送Ajax 请求
        $.ajax({
            "url":"menu/update.json",
            "type":"post",
            "data":{
                "id": window.id,
                "name":name,
                "url":url,
                "icon":icon
            },
            "dataType":"json",
            "success":function(response){
                var result = response.result;
                if(result == "SUCCESS") {
                    layer.msg("操作成功！");
                    // 重新加载树形结构，注意：要在确认服务器端完成保存操作后再刷新
                    // 否则有可能刷新不到最新的数据，因为这里是异步的
                    generateTree();
                }
                if(result == "FAILED") {
                    layer.msg("操作失败！"+response.message);
                }
            },
            "error":function(response){
                layer.msg(response.status+" "+response.statusText);
            }
        });
        // 关闭模态框
        $("#menuEditModal").modal("hide");
    });

    $("#treeDemo").on("click",".removeBtn",function(){
        // 将当前节点的id 保存到全局变量
        window.id = this.id;
        // 打开模态框
        $("#menuConfirmModal").modal("show");
        // 获取zTreeObj 对象
        var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
        // 根据id 属性查询节点对象
        // 用来搜索节点的属性名
        var key = "id";
        // 用来搜索节点的属性值
        var value = window.id;
        var currentNode = zTreeObj.getNodeByParam(key, value);
        $("#removeNodeSpan").html(" 【<i class='"+currentNode.icon+"'></i>"+currentNode.name+"】");
        return false;
    });

    $("#confirmBtn").click(function () {
         $.ajax({
             "url": "menu/remove.json",
             "type": "post",
             "data": {
                 "id":window.id
             },
             "dataType": "json",
             "success": function (response) {
              var result = response.result;
              if (result == "SUCCESS") {
                  layer.msg("操作成功");
                  generateTree();
              }
              if (result == "FAILED") {
                  layer.msg("操作失败" + response.message);
              }
              },
              "error": function (response) {
              layer.msg(response.status + " " + response.statusText);
              }
         });
        // 关闭模态框
        $("#menuConfirmModal").modal("hide");
    });
}
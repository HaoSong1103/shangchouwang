<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css"/>
<%--<link rel="stylesheet" href="css/layer.css"/>--%>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-role.js"></script>
<script type="text/javascript" src="layer/layer.js"></script>
<script type="text/javascript">
    $(function () {
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";

        generatePage();
        //给查询按钮绑定单击响应函数
        $("#searchBtn").click(function () {
            window.keyword = $("#keywordInput").val();
            window.pageNum = 1;
            generatePage();
        });

        $("#showAddModelBtn").click(function () {
            $("#addModal").modal("show");
        });

        $("#saveRoleBtn").click(function () {
            //1.获取用户在文本框内输入的名称
            var roleName = $.trim($("#addModal [name=roleName]").val());

            //2.发送ajax请求
            $.ajax({
                "url": "role/save.json",
                "type": "post",
                "data": {
                    "name": roleName
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                        //页码定位到最后一页
                        window.pageNum = 999999;
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            //3.关闭模态框
            $("#addModal").modal("hide");
            //4.清理模态框
            $("#addModal [name=roleName]").val("");
        });


        //首先找到所有动态生成的元素所附着的“静态元素”
        // ② on()函数的第一个参数是事件类型
        // ③ on()函数的第二个参数是找到真正要绑定事件的元素的选择器
        // ④ on()函数的第三个参数是事件的响应函数
        $("#rolePageBody").on("click", ".pencilBtn", function () {
            // 打开模态框
            $("#editModal").modal("show");
            // 获取表格中当前行中的角色名称
            var roleName = $(this).parent().prev().text();
            // 获取当前角色的id
            // 依据是： var pencilBtn = "<button id='"+roleId+"' ……这段代码中我们把 roleId设置到id属性了
            // 为了让执行更新的按钮能够获取到 roleId的值，把它放在全局变量上
            window.roleId = $(this).parent().parent().attr("id");
            // 使用 roleName的值设置模态框中的文本框
            $("#editModal [name=roleName]").val(roleName);
        });

        $("#updateRoleBtn").click(function () {
            //1.从文本框中获取新的角色名称
            var roleName = $("#editModal [name=roleName]").val();

            //2.发送ajax请求执行更新
            $.ajax({
                "url": "role/update.json",
                "type": "post",
                "data": {
                    "id": window.roleId,
                    "name": roleName
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            $("#editModal").modal("hide");
        });

        $("#removeRoleBtn").click(function () {
            var requestBody = JSON.stringify(window.roleIdArray);
            $.ajax({
                "url": "role/delete.json",
                "type": "post",
                "data": requestBody,
                "dataType": "json",
                "contentType":"application/json;charset=UTF-8",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                        generatePage();
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            $("#confirmModal").modal("hide");
        });

        $("#rolePageBody").on("click",".removeBtn",function () {
            var roleName = $(this).parent().prev().text();
            var roleId = $(this).parent().parent().attr("id");
            var roleArray = [{
                roleId : roleId,
                roleName: roleName
            }];
            showConfirmModel(roleArray);
        });

        $("#summaryBox").click(function () {
            //1.获取当前多选框的状态
            var currentStatus = this.checked;
            //2.用当前多选框的状态设置其他多选框的状态
            $(".itemBox").prop("checked",currentStatus);
        });

        $("#rolePageBody").on("click",".itemBox",function () {
            //1.获取已经选中的.itemBox数量
            var checkBoxCount = $(".itemBox:checked").length;
            var totalBoxCount = $(".itemBox").length;
            $("#summaryBox").prop("checked",checkBoxCount == totalBoxCount);
        });

        $("#BatchRemoveBtn").click(function () {
            var roleArray = [];
            $(".itemBox:checked").each(function () {
                var roleId = $(this).parent().parent().attr("id");
                var roleName = $(this).parent().next().text();
                roleArray.push({
                    "roleId":roleId,
                    "roleName":roleName
                });
            });
            if(roleArray.length==0){
                layer.msg("至少选择一个执行删除");
                return ;
            }
            showConfirmModel(roleArray);
        });

        $("#rolePageBody").on("click", ".checkBtn", function () {
            window.id = $(this).parent().parent().attr("id");

            $("#assignModal").modal("show");

            //在模态框中装载Auth树形结构
            fillAuthTree();
        });

        $("#assignRoleBtn").click(function () {
            // ①收集树形结构的各个节点中被勾选的节点
            // [1]声明一个专门的数组存放id
            var authIdArray = [];
            // [2]获取zTreeObj 对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // [3]获取全部被勾选的节点
            var checkedNodes = zTreeObj.getCheckedNodes();

            // [4]遍历checkedNodes
            for(var i = 0; i < checkedNodes.length; i++) {
                var checkedNode = checkedNodes[i];
                var authId = checkedNode.id;
                authIdArray.push(authId);
            }

            var requestBody = {
                "authIdArray":authIdArray,
                // 为了服务器端handler 方法能够统一使用List<Integer>方式接收数据，roleId 也存入数组
                "roleId":[window.id]
            };
            requestBody = JSON.stringify(requestBody);
            $.ajax({
                "url":"assign/do/role/assign/auth.json",
                "type":"post",
                "data":requestBody,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function(response){
                    var result = response.result;
                    if(result == "SUCCESS") {
                        layer.msg("操作成功！");
                    }
                    if(result == "FAILED") {
                        layer.msg("操作失败！"+response.message);
                    }
                },
                "error":function(response) {
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            $("#assignModal").modal("hide");
        });
    });
</script>
<body>
<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button type="button" id="BatchRemoveBtn" class="btn btn-danger" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button"
                            id="showAddModelBtn" class="btn btn-primary"
                            style="float:right;">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/mode-role-add.jsp" %>
<%@include file="/WEB-INF/mode-role-edit.jsp" %>
<%@include file="/WEB-INF/mode-role-confirm.jsp" %>
<%@include file="modal-role-assign-auth.jsp"%>
</body>
</html>
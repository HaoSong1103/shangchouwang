<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<%--    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>--%>
<%--    <link rel="stylesheet" href="css/login.css"/>--%>
<%--    <script type="text/javascript" src="layer/layer.js"></script>--%>
<%--    <script>--%>
<%--        $(function () {--%>
<%--            $("#btn1").click(function () {--%>
<%--                $.ajax({--%>
<%--                    "url": "send/array/one.json",--%>
<%--                    "type": "post",--%>
<%--                    "data": {--%>
<%--                        "array": [5, 8, 12]--%>
<%--                    },--%>
<%--                    "dataType": "text",--%>
<%--                    "success": function (response) {--%>
<%--                        console.log(response);--%>
<%--                    },--%>
<%--                    "error": function (response) {--%>
<%--                        console.log(response);--%>
<%--                    }--%>
<%--                })--%>
<%--            });--%>
<%--            $("#btn2").click(function () {--%>
<%--                var array = [5, 8, 12];--%>
<%--                var requestBody = JSON.stringify(array);--%>
<%--                $.ajax({--%>
<%--                    "url": "send/array/two.json",--%>
<%--                    "type": "post",--%>
<%--                    "data": requestBody,--%>
<%--                    "contentType": "application/json;charset=UTF-8",--%>
<%--                    "dataType": "json",--%>
<%--                    "success": function (response) {--%>
<%--                        console.log(response);--%>
<%--                    },--%>
<%--                    "error": function (response) {--%>
<%--                        console.log(response);--%>
<%--                    }--%>
<%--                })--%>
<%--            });--%>
<%--            $("#btn3").click(function () {--%>
<%--                layer.msg("Layer的弹框");--%>
<%--            });--%>
<%--        })--%>
<%--    </script>--%>

</head>
<body>
<%--<a href="test/ssm.html">测试SSM整合环境</a>--%>
<%--<br/>--%>
<%--<button id="btn1">Send [5,8,12] One</button>--%>
<%--<br/>--%>
<%--<button id="btn2">Send [5,8,12] Two</button>--%>
<%--<br/>--%>
<%--<button id="btn3">点我弹框</button>--%>
<%--<br/>--%>
<jsp:forward page="admin/to/main/page.html"/>
</body>
</html>

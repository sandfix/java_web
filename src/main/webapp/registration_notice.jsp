<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="instruments.Util.CookieUtil"%>
<%
Cookie[] cookies = request.getCookies();
String login = CookieUtil.getVal(cookies,"login");
String password = CookieUtil.getVal(cookies,"password");
if(login == null)
login = "";
if(password == null)
password = "";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/registration_notice_styles.css">
    </head>
    <body>
        <form class="main" action="index.jsp" method="get">
            <p>Логин: <%=login%></p>
            <p>Пароль: <%=password%></p>
            <button type="submit">Вернуться на главную</button>
        </form>
    </body>
</html>
<%
CookieUtil.delCook(cookies, "login", response);
CookieUtil.delCook(cookies, "password", response);
%>
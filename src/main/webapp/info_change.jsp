<%@page import="java.util.Arrays"%>
<%@page import="java.sql.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="instruments.Util.DButil"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.logging.Logger"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="instruments.Util.CookieUtil"%>
<%@ page session="false" %>
<%@page buffer="32kb" autoFlush="true" %>
<%
    HttpSession my_session = request.getSession(false);
    
    String fio="";
    String phone="";
    String email="";
    String dob="";
    String gender="";
    String bio="";
    
    Cookie[] cookies = request.getCookies();
    boolean[] errors = new boolean[4];
    Long user_id = null;
    String[] langs = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
    String[] check = new String[langs.length];
    String errors_cook = CookieUtil.getVal(cookies,"errors");
    
    Arrays.fill(check,"");
    
    if(my_session!=null){
        user_id = (Long)my_session.getAttribute("user_id");
    }
    
    
    for(char i : errors_cook.toCharArray())
    {
        if(i!='-')
            errors[Character.getNumericValue(i)] = true;
    }
    
        //////
    boolean editFlag = false;
    if(!CookieUtil.getVal(cookies,"editID").equals("")){
        user_id=Long.parseLong(CookieUtil.getVal(cookies,"editID"));
        editFlag = true;
    }
    if(user_id!=null)
    {
        try (Connection conn = DButil.getConnection()){
                    ResultSet user_info = DButil.getUserInfo(conn, user_id);
                    ResultSet user_langs = DButil.getLangsNums(conn, user_id);
                    if(user_info.next())
                    {
                        fio = user_info.getString("fio");
                        phone = user_info.getString("phone");
                        email = user_info.getString("email");
                        dob = user_info.getDate("dob").toString();
                        gender = user_info.getString("gender");
                        bio = user_info.getString("bio");
                    }
                    while(user_langs.next())
                    {
                        check[user_langs.getInt(1)-1] = "selected";
                    }
                }
            catch(Exception ex){
                ex.printStackTrace();
            }
    }
%>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Изменение</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<% if(user_id==null){ %>
<body>необходимо залогиниться</body>
<% }else{ %>
<body>
    <div class="main">
        <%if(editFlag==false){%>
        <div class="login_form_div">
            <form class="login_form" action="index.jsp" method="post">
                <span style="text-align: center; font-size: medium; padding-bottom: 5px; font-weight: bold;"><%= my_session.getAttribute("user_fio") %></span>
                <button type="submit">Вернуться к форме</button>
            </form>
        </div>
        <%}%>
        <%if(editFlag==true){%>
        <div class="login_form_div">
            <form class="login_form" action="index.jsp" method="post">
                <a href="protected/admin_panel.jsp" style="display: block; margin-bottom: 10px; text-align: center;">отредактировать данные</a>
            </form>
        </div>
        <%}%>   
        <div class="center_elem">
            <h1 style="text-align: center;">Изменение данных</h1>
            <form class="main_form" action="edit" method="post">
                <!-- ФИО -->
                <label for="fio">ФИО:</label>
                <input class="<%= (errors[0] == true) ? "error" : "" %>" type="text" id="fio" name="fio" value="<%= fio %>" >
                <% if(errors[0] == true){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: fio format number of characters less than 150 (ONLY ENG)
                    </div>
                <% } %>
                <br><br>

                <!-- Телефон -->
                <label for="phone">Телефон:</label>
                <input class="<%= (errors[1] == true) ? "error" : "" %>" type="tel" id="phone" name="phone"  placeholder="123-456-7890" value=<%= phone %> >
                <% if(errors[1] == true){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: phone format only '-' and numbers. May start with +
                    </div>
                <% } %>
                <br><br>

                <!-- E-mail -->
                <label for="email">E-mail:</label>
                <input class="<%= (errors[2] == true) ? "error" : "" %>" id="email" name="email" value=<%= email %>>
                <% if(errors[2] == true){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: email format xxxx@xxxx
                    </div>
                <% } %>
                <br><br>

                <!-- Дата рождения -->
                <label for="dob">Дата рождения:</label>
                <input class="<%= (errors[3] == true) ? "error" : "" %>" type="date" id="dob" name="dob" value=<%= dob %>>
                <% if(errors[3] == true){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: date format xx-xx-xxxx
                    </div>
                <% } %>
                <br><br>

                <!-- Пол -->
                <div>
                    <label for="gender">Пол:</label>
                    <input type="radio" id="male" name="gender" value="male" required <%= gender.equals("male") ? "checked":"" %>>
                    <label for="male">Мужской</label>
                    <input type="radio" id="female" name="gender" value="female" <%= gender.equals("female") ? "checked":"" %>>
                    <label for="female">Женский</label>
                </div><br><br>
                <!-- Любимый язык программирования -->
                <label for="languages">Любимый язык программирования:</label>
                <select id="languages" name="languages" multiple size="7">
                    <%
                    for(int i=0;i<langs.length;i++)
                    {
                        out.println("<option name=" + langs[i] + " value=" + i + " " + check[i] + ">" + langs[i] + "</option>");
                    }
                    %>
                </select><br><br>

                <!-- Биография -->
                <label for="biography">Биография:</label>
                <textarea id="biography" name="biography" rows="4" cols="50"><%= bio %></textarea>
                <br><br>

                <!-- Кнопка "Сохранить" -->
                <button type="submit">Изменить</button>
            </form>
        </div>
    </div>
</body>
<%}%>
</html>
<%
    if(!errors_cook.equals(""))
    {
        System.out.println("errors deleted");
        CookieUtil.delCook(cookies, "errors", response);
    }
    
%>
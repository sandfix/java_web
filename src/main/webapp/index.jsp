<%@page import="java.util.logging.Logger"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="instruments.Util.CookieUtil"%>
<%
    Cookie[] cookies = request.getCookies();
    boolean[] errors = new boolean[4];
    String[] langs = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
    String[] check = new String[langs.length];
    String langs_cook = CookieUtil.getVal(cookies,"langs");
    String errors_cook = CookieUtil.getVal(cookies,"errors");
    
    for(char i : langs_cook.toCharArray())
    {
        if(i!='-')
            check[Character.getNumericValue(i)] = "selected";
    }
    
    for(char i : errors_cook.toCharArray())
    {
        if(i!='-')
            errors[Character.getNumericValue(i)] = true;
    }
    
    for(boolean b : errors)
        System.out.println(b);
%>
<%!

%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Форма</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <div class="main">
        <div class="center_elem">
            <h1 style="text-align: center;">Форма регистрации</h1>
            <form class="main_form" action="main_page" method="post">
                <!-- ФИО -->
                <label for="fio">ФИО:</label>
                <input class="<%= (errors[0] == true) ? "error" : "" %>" type="text" id="fio" name="fio" value="<%= CookieUtil.getVal(cookies,"fio")%>" >
                <% if(errors[0] == true){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: fio format number of characters less than 150 (ONLY ENG)
                    </div>
                <% } %>
                <br><br>

                <!-- Телефон -->
                <label for="phone">Телефон:</label>
                <input class="<%= (errors[1] == true) ? "error" : "" %>" type="tel" id="phone" name="phone"  placeholder="123-456-7890" value=<%= CookieUtil.getVal(cookies,"phone")%> >
                <% if(errors[1] == true){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: phone format only '-' and numbers. May start with +
                    </div>
                <% } %>
                <br><br>

                <!-- E-mail -->
                <label for="email">E-mail:</label>
                <input class="<%= (errors[2] == true) ? "error" : "" %>" id="email" name="email" value=<%= CookieUtil.getVal(cookies,"email")%>>
                <% if(errors[2] == true){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: email format xxxx@xxxx
                    </div>
                <% } %>
                <br><br>

                <!-- Дата рождения -->
                <label for="dob">Дата рождения:</label>
                <input class="<%= (errors[3] == true) ? "error" : "" %>" type="date" id="dob" name="dob" value=<%= CookieUtil.getVal(cookies,"dob")%>>
                <% if(errors[3] == true){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: date format xx-xx-xxxx
                    </div>
                <% } %>
                <br><br>

                <!-- Пол -->
                <div>
                    <label for="gender">Пол:</label>
                    <input type="radio" id="male" name="gender" value="male" <%= CookieUtil.getVal(cookies,"gender").equals("male") ? "checked":"" %>>
                    <label for="male">Мужской</label>
                    <input type="radio" id="female" name="gender" value="female" <%= CookieUtil.getVal(cookies,"gender").equals("female") ? "checked":"" %>>
                    <label for="female">Женский</label>
                </div><br><br>
                <!-- Любимый язык программирования -->
                <label for="languages">Любимый язык программирования:</label>
                <select id="languages" name="languages" multiple size="7">
                   <%-- <option name="Pascal" value="1" <%= !CookieUtil.getVal(cookies,"Pascal").equals("") ? "selected" : "" %>>Pascal</option>
                    <option name="C" value="2" <%= !CookieUtil.getVal(cookies,"C").equals("") ? "selected" : "" %>>C</option>
                    <option name="C++" value="3" <%= !CookieUtil.getVal(cookies,"C++").equals("") ? "selected" : "" %>>C++</option>
                    <option name="JavaScript"value="4"  <%= !CookieUtil.getVal(cookies,"JavaScript").equals("") ? "selected" : "" %>>JavaScript</option>
                    <option name="PHP" value="5" <%= !CookieUtil.getVal(cookies,"PHP").equals("") ? "selected" : "" %>>PHP</option>
                    <option name="Python" value="6" <%= !CookieUtil.getVal(cookies,"Python").equals("") ? "selected" : "" %>>Python</option>
                    <option name="Java" value="7" <%= !CookieUtil.getVal(cookies,"Java").equals("") ? "selected" : "" %>>Java</option>
                    <option name="Haskell" value="8" <%= !CookieUtil.getVal(cookies,"Haskell").equals("") ? "selected" : "" %>>Haskell</option>
                    <option name="Clojure" value="9" <%= !CookieUtil.getVal(cookies,"Clojure").equals("") ? "selected" : "" %>>Clojure</option>
                    <option name="Prolog" value="10" <%= !CookieUtil.getVal(cookies,"Prolog").equals("") ? "selected" : "" %>>Prolog</option>
                    <option name="Scala" value="11" <%= !CookieUtil.getVal(cookies,"Scala").equals("") ? "selected" : "" %>>Scala</option>
                    <option name="Go" value="12" <%= !CookieUtil.getVal(cookies,"Go").equals("") ? "selected" : "" %>>Go</option> --%>
                    <%
                    for(int i=0;i<langs.length;i++)
                    {
                        out.println("<option name=" + langs[i] + " value=" + i + " " + check[i] + ">" + langs[i] + "</option>");
                    }
                    %>
                </select><br><br>

                <!-- Биография -->
                <label for="biography">Биография:</label>
                <textarea id="biography" name="biography" rows="4" cols="50"><%= CookieUtil.getVal(cookies,"bio")%></textarea><br><br>

                <!-- Ознакомлен с контрактом -->
                <label for="contract">
                    <input type="checkbox" id="contract" name="contract" required>
                    С контрактом ознакомлен(а)
                </label><br><br>

                <!-- Кнопка "Сохранить" -->
                <button type="submit">Сохранить</button>
            </form>
        </div>
    </div>
</body>
</html>
<%
    if(!errors_cook.equals(""))
    {
        CookieUtil.delCook(cookies, "fio", response);
        CookieUtil.delCook(cookies, "phone", response);
        CookieUtil.delCook(cookies, "email", response);
        CookieUtil.delCook(cookies, "dob", response);
        CookieUtil.delCook(cookies, "gender", response);
        CookieUtil.delCook(cookies, "bio", response);
        CookieUtil.delCook(cookies, "langs", response);
        CookieUtil.delCook(cookies, "errors", response);
    }
%>
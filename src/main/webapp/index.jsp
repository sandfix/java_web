<%@page import="java.util.logging.Logger"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="instruments.Util.CookieUtil"%>
<%
    Cookie[] cookies = request.getCookies();
    String[] langs = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
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
                <input class="<%= (request.getParameter("fio")!=null && request.getParameter("fio").equals("true")) ? "error" : "" %>" type="text" id="fio" name="fio" value="<%= CookieUtil.contains(cookies,"fio")%>" >
                <% if(request.getParameter("fio")!=null && request.getParameter("fio").equals("true")){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: fio format number of characters less than 150 (ONLY ENG)
                    </div>
                <% } %>
                <br><br>

                <!-- Телефон -->
                <label for="phone">Телефон:</label>
                <input class="<%= (request.getParameter("phone")!=null && request.getParameter("phone").equals("true")) ? "error" : "" %>" type="tel" id="phone" name="phone"  placeholder="123-456-7890" value=<%= CookieUtil.contains(cookies,"phone")%> >
                <% if(request.getParameter("phone")!=null && request.getParameter("phone").equals("true")){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: phone format only '-' and numbers. May start with +
                    </div>
                <% } %>
                <br><br>

                <!-- E-mail -->
                <label for="email">E-mail:</label>
                <input class="<%= (request.getParameter("email")!=null && request.getParameter("email").equals("true")) ? "error" : "" %>" id="email" name="email" value=<%= CookieUtil.contains(cookies,"email")%>>
                <% if(request.getParameter("email")!=null && request.getParameter("email").equals("true")){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: email format xxxx@xxxx
                    </div>
                <% } %>
                <br><br>

                <!-- Дата рождения -->
                <label for="dob">Дата рождения:</label>
                <input class="<%= (request.getParameter("dob")!=null && request.getParameter("dob").equals("true")) ? "error" : "" %>" type="date" id="dob" name="dob" value=<%= CookieUtil.contains(cookies,"dob")%>>
                <% if(request.getParameter("dob")!=null && request.getParameter("dob").equals("true")){ %>
                    <div style="color: #af0000;font-size: 20px;">
                        Error: date format xx-xx-xxxx
                    </div>
                <% } %>
                <br><br>

                <!-- Пол -->
                <div>
                    <label for="gender">Пол:</label>
                    <input type="radio" id="male" name="gender" value="male" <%= CookieUtil.contains(cookies,"gender").equals("male") ? "checked":"" %>>
                    <label for="male">Мужской</label>
                    <input type="radio" id="female" name="gender" value="female" <%= CookieUtil.contains(cookies,"gender").equals("female") ? "checked":"" %>>
                    <label for="female">Женский</label>
                </div><br><br>
                <!-- Любимый язык программирования -->
                <label for="languages">Любимый язык программирования:</label>
                <select id="languages" name="languages" multiple size="7">
                    <option value="Pascal" <%= !CookieUtil.contains(cookies,"Pascal").equals("") ? "selected" : "" %>>Pascal</option>
                    <option value="C" <%= !CookieUtil.contains(cookies,"C").equals("") ? "selected" : "" %>>C</option>
                    <option value="C++" <%= !CookieUtil.contains(cookies,"C++").equals("") ? "selected" : "" %>>C++</option>
                    <option value="JavaScript" <%= !CookieUtil.contains(cookies,"JavaScript").equals("") ? "selected" : "" %>>JavaScript</option>
                    <option value="PHP" <%= !CookieUtil.contains(cookies,"PHP").equals("") ? "selected" : "" %>>PHP</option>
                    <option value="Python" <%= !CookieUtil.contains(cookies,"Python").equals("") ? "selected" : "" %>>Python</option>
                    <option value="Java" <%= !CookieUtil.contains(cookies,"Java").equals("") ? "selected" : "" %>>Java</option>
                    <option value="Haskell" <%= !CookieUtil.contains(cookies,"Haskell").equals("") ? "selected" : "" %>>Haskell</option>
                    <option value="Clojure" <%= !CookieUtil.contains(cookies,"Clojure").equals("") ? "selected" : "" %>>Clojure</option>
                    <option value="Prolog" <%= !CookieUtil.contains(cookies,"Prolog").equals("") ? "selected" : "" %>>Prolog</option>
                    <option value="Scala" <%= !CookieUtil.contains(cookies,"Scala").equals("") ? "selected" : "" %>>Scala</option>
                    <option value="Go" <%= !CookieUtil.contains(cookies,"Go").equals("") ? "selected" : "" %>>Go</option>
                </select><br><br>

                <!-- Биография -->
                <label for="biography">Биография:</label>
                <textarea id="biography" name="biography" rows="4" cols="50"><%= CookieUtil.contains(cookies,"bio")%></textarea><br><br>

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
    if(request.getParameter("status")!=null && request.getParameter("status").equals("error"))
    {
        CookieUtil.delCook(cookies, "fio", response);
        CookieUtil.delCook(cookies, "phone", response);
        CookieUtil.delCook(cookies, "email", response);
        CookieUtil.delCook(cookies, "dob", response);
        CookieUtil.delCook(cookies, "gender", response);
        CookieUtil.delCook(cookies, "bio", response);
        if(langs!=null)
            for(String i: langs)
                CookieUtil.delCook(cookies, i, response);
    }
%>
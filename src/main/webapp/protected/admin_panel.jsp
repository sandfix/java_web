<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="instruments.Util.DButil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
Connection conn = DButil.getConnection();
ResultSet rs = DButil.GetBestLangs(conn);
ResultSet rsUsers = DButil.GetUsers(conn);
int num = 1;
%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/admin_panel_styles.css">
    </head>
    <body>
        <div>
            <table>
                <tr><th>ID</th><th>Имя</th><th>Телефон</th><th>Email</th><th>Дата рождения</th><th>Пол</th><th></th><th></th></tr>
                <%while(rsUsers.next())
                {
                    int id = rsUsers.getInt("id");
                    String name = rsUsers.getString("fio");
                    String phone = rsUsers.getString("phone");
                    String email = rsUsers.getString("email");
                    String dob = rsUsers.getDate("dob").toString();
                    String gender = rsUsers.getString("gender");
                %>
                    <tr>
                        <td><%=id%></td>
                        <td><%=name%></td>
                        <td><%=phone%></td>
                        <td><%=email%></td>
                        <td><%=dob%></td>
                        <td><%=gender%></td>
                        <td><a href=<%="DeleteUser?id="+id%>>🗑️</a></td>
                        <td><a href=<%="EditUser?id="+id%>>️✏️</a></td>
                    </tr>
                <%}%>
            </table>
        </div>
        <div>
            <table>
                <tr><th>Место</th><th>ЯП</th><th>Кол-во пользователй</th></tr>
                    <%while(rs.next())
                    {
                        String name = rs.getString("name");
                        int kol = rs.getInt("kol");
                    %>
                    <tr>
                        <td><%=num%></td>
                        <td><%=name%></td>
                        <td><%=kol%></td>
                    </tr>
                    <%num++;}%>
            </table>
        </div>
    </body>
</html>
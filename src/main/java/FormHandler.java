
import authorization.User;
import instruments.Util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/main_page")
public class FormHandler extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] all_langs = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
        boolean correctInfo = true;
        String errors = "";

        response.setContentType("text/html");

        String fio = request.getParameter("fio");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String bio = request.getParameter("biography");
        String languages[] = request.getParameterValues("languages");

        String langs = "";
        if(languages!=null){
            for(String i: languages)
            {
                 langs+=i+"-";
            }
            langs = langs.substring(0,langs.length()-1);
        }
        if(!fio.matches("[a-zA-Z ]{0,150}"))
        {
            correctInfo=false;
            errors+="0-";
        }
        if(!phone.matches("[0-9+]{1}[0-9- ]{0,20}"))
        {
            correctInfo=false;
            errors+="1-";
        }
        if(!email.matches("^\\S+@\\S+$"))
        {
            correctInfo=false;
            errors+="2-";
        }
        if(!dob.matches("^\\d{4}-\\d\\d-\\d\\d$"))
        {
            correctInfo=false;
            errors+="3-";
        }

        if(correctInfo){
            User user = new User();
            
            CookieUtil.addCook("login", user.getLogin(), -1, response);
            CookieUtil.addCook("password", user.getPassword(), -1, response);
            
            try (Connection conn = DButil.getConnection()){
                    long id = DButil.insertUser(conn,fio,phone,email,dob,gender,bio);
                    DButil.insertFavLangs(conn, id, languages);
                    DButil.insertAuth(conn, user, id);
                }
            catch(Exception ex){
                PrintWriter writer = response.getWriter();
                writer.println("Connection failed...");
                writer.println(ex);
                writer.close();
            }
            CookieUtil.addCook("fio",fio,365*24*60*60,response);
            CookieUtil.addCook("phone",phone,365*24*60*60,response);
            CookieUtil.addCook("email",email,365*24*60*60,response);
            CookieUtil.addCook("dob",dob,365*24*60*60,response);
            CookieUtil.addCook("gender",gender,365*24*60*60,response);
            CookieUtil.addCook("bio",bio,365*24*60*60,response);
            CookieUtil.addCook("langs",langs,365*24*60*60,response);
            String responseLink = request.getContextPath() + "/registration_notice.jsp";
            response.sendRedirect(responseLink);
        }
        else{
            CookieUtil.addCook("fio",fio,-1,response);
            CookieUtil.addCook("phone",phone,-1,response);
            CookieUtil.addCook("email",email,-1,response);
            CookieUtil.addCook("dob",dob,-1,response);
            CookieUtil.addCook("gender",gender,-1,response);
            CookieUtil.addCook("bio",bio,-1,response);
            CookieUtil.addCook("langs",langs,-1,response);
            CookieUtil.addCook("errors",errors,-1,response);
            
            String responseLink = request.getContextPath() + "/index.jsp";
            response.sendRedirect(responseLink);
        }

    }
}

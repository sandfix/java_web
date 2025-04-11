
import authorization.User;
import instruments.Util.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/main_page")
public class FormHandler extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] all_langs = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
        boolean correctInfo = true;
        String errors = "";

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        String fio = request.getParameter("fio");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String bio = request.getParameter("biography");
        String languages[] = request.getParameterValues("languages");

        String langs = "";
            if(languages!=null)
            for(String i: languages)
            {
                langs+=i+"-";
            }
        langs = langs.substring(0,langs.length()-1);

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
//            try (Connection conn = DButil.getConnection()){
//                    writer.println("All good");
//                    DButil.insertUser(conn,fio,phone,email,dob,gender,bio);
//                    DButil.insertFavLangs(conn,fio,languages);
//                }
//            catch(Exception ex){
//                writer.println("Connection failed...");
//                writer.println(ex);
//            }
            CookieUtil.addCook("fio",fio,365*24*60*60,response);
            CookieUtil.addCook("phone",phone,365*24*60*60,response);
            CookieUtil.addCook("email",email,365*24*60*60,response);
            CookieUtil.addCook("dob",dob,365*24*60*60,response);
            CookieUtil.addCook("gender",gender,365*24*60*60,response);
            CookieUtil.addCook("bio",bio,365*24*60*60,response);
            CookieUtil.addCook("langs",langs,365*24*60*60,response);
            
            User user = new User();
            writer.println(user.getLogin());
            writer.println(user.getPassword());
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            writer.close();
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
            
            String responseLink = "/web_proj/index.jsp";
            response.sendRedirect(responseLink);
        }

    }
}

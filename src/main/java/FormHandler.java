
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

@WebServlet("/main_page")
public class FormHandler extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String[] all_langs = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
        boolean correctInfo = true;
        boolean[] errors = new boolean[4];
        
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
            errors[0] = true;
        }
        if(!phone.matches("[0-9+]{1}[0-9- ]{0,20}"))
        {
            correctInfo=false;
            errors[1] = true;
        }
        if(!email.matches("^\\S+@\\S+$"))
        {
            correctInfo=false;
            errors[2] = true;
        }
        if(!dob.matches("^\\d{4}-\\d\\d-\\d\\d$"))
        {
            correctInfo=false;
            errors[3] = true;
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

            String responseLink = "index.jsp";
            request.setAttribute("errors", errors);
            ServletContext sc = getServletContext();
            RequestDispatcher rd = sc.getRequestDispatcher("/indext.jsp");
            rd.forward(request, response);
           // response.sendRedirect(responseLink);
        }
        
    }
}

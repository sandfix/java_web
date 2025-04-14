
import authorization.User;
import instruments.Util;
import instruments.Util.DButil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;

/**
 *
 * @author Артем
 */
@WebServlet("/edit")
public class EditHandler extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] all_langs = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
        boolean correctInfo = true;
        String errors = "";
        
        HttpSession my_session = request.getSession(false);
        Integer user_id=null;
        if(my_session!=null){
        user_id = (Integer)my_session.getAttribute("user_id");
        }
        
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
            try (Connection conn = Util.DButil.getConnection()){
                    DButil.deleteFavLangs(conn, user_id);
                    DButil.insertFavLangs(conn, fio, languages);
                    DButil.updateUserInfo(conn, user_id, fio, phone, email, dob, gender, bio);
                }
            catch(Exception ex){
                ex.printStackTrace();
            }
            Util.CookieUtil.addCook("fio",fio,365*24*60*60,response);
            Util.CookieUtil.addCook("phone",phone,365*24*60*60,response);
            Util.CookieUtil.addCook("email",email,365*24*60*60,response);
            Util.CookieUtil.addCook("dob",dob,365*24*60*60,response);
            Util.CookieUtil.addCook("gender",gender,365*24*60*60,response);
            Util.CookieUtil.addCook("bio",bio,365*24*60*60,response);
            Util.CookieUtil.addCook("langs",langs,365*24*60*60,response);
        }
        else{
            
            Util.CookieUtil.addCook("errors",errors,-1,response);
            String responseLink = "/web_proj/info_change.jsp";
            response.sendRedirect(responseLink);
        }

    }
}

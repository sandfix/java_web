
import instruments.Util.CookieUtil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/main_page")
public class FormHandler extends HttpServlet {
    
    private void addCook(String name, String val, int time, HttpServletResponse response)
    {
        if(val==null)
            return;
        try{
        Cookie cook = new Cookie(name,URLEncoder.encode(val, "UTF-8"));
        cook.setMaxAge(time);
        response.addCookie(cook);
        }
        catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String[] all_langs = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
        CookieUtil cu = new CookieUtil();
        boolean correctInfo = true;
        ArrayList<String> errors = new ArrayList<>();
        
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        String fio = request.getParameter("fio");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String bio = request.getParameter("biography");
        String languages[] = request.getParameterValues("languages"); 
        
        
        if(!fio.matches("[a-zA-Z ]{0,150}"))
        {
            correctInfo=false;
            errors.add("fio");
        }
        if(!phone.matches("[0-9+]{1}[0-9- ]{0,20}"))
        {
            correctInfo=false;
            errors.add("phone");
        }
        if(!email.matches("^\\S+@\\S+$"))
        {
            correctInfo=false;
            errors.add("email");
        }
        if(!dob.matches("^\\d{4}-\\d\\d-\\d\\d$"))
        {
            correctInfo=false;
            errors.add("dob");
        }
        
        
        if(correctInfo){
            try{
            String url = "jdbc:mariadb://localhost:3306/users_info";
            String login = "root";
            String password = "omegalul";
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url,login,password)){
                    writer.println("All good");
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(fio,phone,email,dob,gender,bio) VALUES(?,?,?,?,?,?)");
                    stmt.setString(1, fio);
                    stmt.setString(2, phone);
                    stmt.setString(3, email);
                    stmt.setDate(4,Date.valueOf(dob));
                    stmt.setString(5, gender);
                    stmt.setString(6, bio);
                    stmt.executeUpdate();
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO fav_langs(user_id,lang_id)\n" +
                                                                    "SELECT u.id, l.id FROM\n" +
                                                                    "users u JOIN langs l ON l.name=?\n" +
                                                                    "WHERE u.fio=?");
                    if(languages!=null)
                    for(String lang: languages){
                        stmt2.setString(1,lang);
                        stmt2.setString(2,fio);
                        stmt2.executeUpdate();
                    }
                }
            catch(Exception ex){
                writer.println("Connection failed...");
                writer.println(ex);
            }
            }
            catch(Exception ex){
                writer.println("Driver problems");
                writer.println(ex);
            }

            addCook("fio",fio,365*24*60*60,response);
            addCook("phone",phone,365*24*60*60,response);
            addCook("email",email,365*24*60*60,response);
            addCook("dob",dob,365*24*60*60,response);
            addCook("gender",gender,365*24*60*60,response);
            addCook("bio",bio,365*24*60*60,response);
            
            Cookie[] allCook = request.getCookies();
            if(all_langs!=null)
                for(String i: all_langs)
                    cu.delCook(allCook, i, response);
            if(languages!=null)
                for(String lang: languages){
                    addCook(lang,lang,365*24*60*60,response);
                }
            writer.close();
        }
        else{
            addCook("fio",fio,-1,response);
            addCook("phone",phone,-1,response);
            addCook("email",email,-1,response);
            addCook("dob",dob,-1,response);
            addCook("gender",gender,-1,response);
            addCook("bio",bio,-1,response);
            if(languages!=null)
                for(String lang: languages){
                    addCook(lang,lang,-1,response);
                }
            String responseLink = "index.jsp?status=error";
            if(errors.size()!=0)
                for(String i: errors)
                {
                    responseLink+="&"+i+"=true";
                }
            response.sendRedirect(responseLink);
        }
        
    }
}

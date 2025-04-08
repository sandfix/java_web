package instruments;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Util {
    private static final Logger logger = Logger.getLogger(Util.class.getName());
    
    //класс для работы с куки
    public static class CookieUtil{
        public static String getVal(Cookie mas[], String name)
        {   
            String ret = "";
            
            if(mas == null || mas.length==0)
                return ret;
            for(Cookie k: mas){
                if(k.getName().equals(name))
                {
                    try
                    {
                        ret = URLDecoder.decode(k.getValue(), "UTF-8");
                    }
                    catch (UnsupportedEncodingException e) 
                    {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            return ret;
        }
        
        
        public static void delCook(Cookie mas[], String name, HttpServletResponse response){
            
            if(mas == null || mas.length==0)
                return;
            for(Cookie k: mas){
                if(k.getName().equals(name))
                {
                    k.setValue("");
                    k.setMaxAge(0);
                    response.addCookie(k);
                    break;
                }
            }
        }
        
        public static void addCook(String name, String val, int time, HttpServletResponse response){
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
        //overload for String[]
//        public static void addCook(String name, String[] mas, int time, HttpServletResponse response){
//            if(mas==null)
//                return;
//            for(String val : mas){
//                try
//                {
//                    Cookie cook = new Cookie(name,URLEncoder.encode(val, "UTF-8"));
//                    cook.setMaxAge(time);
//                    response.addCookie(cook);
//                }
//                catch (UnsupportedEncodingException e) 
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
        
    }
    //класс с подготовленными sql запросами и подключением к бд
    public static class DButil{
        private static final String URL = "jdbc:mariadb://localhost:3306/users_info";
        private static final String USER = "root";
        private static final String PASSWORD = "";
        static {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } 
            catch (ClassNotFoundException e) {
                throw new RuntimeException("Не удалось загрузить драйвер базы данных", e);
            }   
        }
        
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        
        public static void insertUser(Connection conn, String fio, String phone, String email, String dob, String gender, String bio) throws SQLException
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(fio,phone,email,dob,gender,bio) VALUES(?,?,?,?,?,?)");
            stmt.setString(1, fio);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setDate(4,Date.valueOf(dob));
            stmt.setString(5, gender);
            stmt.setString(6, bio);
            stmt.executeUpdate();
        }
        
        public static void insertFavLangs(Connection conn, String fio, String[] languages) throws SQLException
        {
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
        
    }
}

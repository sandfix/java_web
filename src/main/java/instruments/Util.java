package instruments;
import authorization.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Util {
    private static final Logger logger = Logger.getLogger(Util.class.getName());
    private static final String[] ALL_LANGS = {"Pascal","C","C++","JavaScript","PHP","Python","Java","Haskell","Clojure","Prolog","Scala","Go"};
    
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
                    System.out.println("Cookies " + k.getName() + " deleted");
                    k.setValue("");
                    k.setMaxAge(0);
                    k.setPath("/");
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
                cook.setPath("/");
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
        private static final String PASSWORD = "omegalul";
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
        
        public static long insertUser(Connection conn, String fio, String phone, String email, String dob, String gender, String bio) throws SQLException
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(fio,phone,email,dob,gender,bio) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, fio);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setDate(4,Date.valueOf(dob));
            stmt.setString(5, gender);
            stmt.setString(6, bio);
            int affected_rows = stmt.executeUpdate();
            if(affected_rows>0)
            {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) 
                {
                    if (generatedKeys.next())
                    {
                        long generatedId = generatedKeys.getLong(1);
                        return generatedId;
                    }
                    else
                        return -1;
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            return -1;
        }
        
        public static void insertFavLangs(Connection conn, Long id, String[] num_languages) throws SQLException
        {
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO fav_langs(user_id,lang_id)\n" +
                                                                    "SELECT u.id, l.id FROM\n" +
                                                                    "users u JOIN langs l ON l.name=?\n" +
                                                                    "WHERE u.id=?");
            if(num_languages!=null)
            for(String num_lang: num_languages){
                stmt2.setString(1,ALL_LANGS[Integer.parseInt(num_lang)]);
                stmt2.setLong(2,id);
                stmt2.executeUpdate();
            }
        }
        
        public static void insertAuth(Connection conn, User user, Long id) throws SQLException
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO auth(user_id, login, password, hash_algorithm, salt) VALUES(?,?,?,?,?)");
            stmt.setLong(1, id);
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getHash("PBKDF2WithHmacSHA512"));
            stmt.setString(4,"PBKDF2WithHmacSHA512");
            stmt.setBytes(5, user.getSalt());
            stmt.executeUpdate();
        }
        
        public static ResultSet getAuthInfo(Connection conn, String login) throws SQLException{
            PreparedStatement stmt = conn.prepareStatement("SELECT user_id, password, hash_algorithm, salt FROM auth "
                                                            + "WHERE login=?");
            stmt.setString(1, login);
            return stmt.executeQuery();
        }
        
        public static String getFio(Connection conn, Long id) throws SQLException{
            PreparedStatement stmt = conn.prepareStatement("SELECT fio FROM users "
                                                            + "WHERE id=?");
            stmt.setLong(1, id);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getString("fio");
        }
        
        public static ResultSet getUserInfo(Connection conn, Long id) throws SQLException{
            PreparedStatement stmt = conn.prepareStatement("select fio, phone, email, dob, gender, bio from users " +
                                                            "where id = ?");
            stmt.setLong(1, id);
            return stmt.executeQuery();
        }
        
        public static ResultSet getLangsNums(Connection conn, Long id) throws SQLException{
            PreparedStatement stmt = conn.prepareStatement("select lang_id from fav_langs " +
                                                            "where user_id = ?");
            stmt.setLong(1, id);
            return stmt.executeQuery();
        }
        
        public static void deleteFavLangs(Connection conn, Long id) throws SQLException{
            PreparedStatement stmt = conn.prepareStatement("delete from fav_langs where user_id=?");
            stmt.setLong(1, id);
            stmt.executeQuery();
        }
        
        public static void updateUserInfo(Connection conn, Long id, String fio, String phone, String email, String dob, String gender, String bio) throws SQLException{
            if(id==null)
                return;
            PreparedStatement stmt = conn.prepareStatement("update users set fio=?, phone=?, email=?, dob=?, gender=?, bio=? where id=?");
            stmt.setString(1, fio);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setDate(4,Date.valueOf(dob));
            stmt.setString(5, gender);
            stmt.setString(6, bio);
            stmt.setLong(7, id);
            stmt.executeQuery();
        }
        
        public static ResultSet GetBestLangs(Connection conn) throws SQLException{
            PreparedStatement stmt = conn.prepareStatement("select name, count(user_id) kol from\n " +
                                                            "fav_langs join langs on id = lang_id\n " +
                                                            "group by name\n " +
                                                            "order by count(user_id) desc");
            return stmt.executeQuery();
        }
        
        public static ResultSet GetUsers(Connection conn) throws SQLException{
            PreparedStatement stmt = conn.prepareStatement("select id,fio,phone,email,dob,gender from users order by id");
            return stmt.executeQuery();
        }
        
        public static void deleteUser(Connection conn, Long id) throws SQLException{
            if(id==null)
                return;
            PreparedStatement stmt = conn.prepareStatement("delete from users where id=? ");
            stmt.setLong(1, id);
            stmt.executeQuery();
        }
        
    }
}

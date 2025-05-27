package services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
import rest.User;

public class SecurityService {

    private static final String URL = "jdbc:mariadb://localhost:3306/proj_db";
    private static final String USER = "root";
    private static final String PASS = "omegalul";
    
    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("MariaDB драйвер зарегистрирован");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public User checkUser(String login, String password) {
        String sql = "select * from users where login = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()){
                System.out.println("pusto");
                return null;
            }
            String stored_pass = rs.getString("password");
            if(!BCrypt.checkpw(password, stored_pass)){
                System.out.println("ne sovpal");
                return null;
            }
            else
                return new User(rs.getString("name"), rs.getString("phone"), rs.getString("email"), rs.getString("message"), rs.getString("login"), rs.getString("password"));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
}


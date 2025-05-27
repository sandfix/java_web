package services;
import instruments.PasswordGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import rest.ContactForm;
import org.mindrot.jbcrypt.BCrypt;
import rest.User;

public class ContactFormService {

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
    
    public void saveContactForm(User user) {
        String sql = "INSERT INTO users(name, password, phone, email, message, login) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getMessage());
            stmt.setString(6, user.getLogin());
            stmt.executeUpdate();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void editContactForm(User user) {
        String sql = "UPDATE users SET name = ?, phone = ?, email = ?, message = ? WHERE login = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getMessage());
            stmt.setString(5, user.getLogin());
            stmt.executeUpdate();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}


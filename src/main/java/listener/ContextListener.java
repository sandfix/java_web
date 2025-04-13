package listener;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Артем
 */
public class ContextListener implements ServletContextListener{
     @Override
    public void contextDestroyed(ServletContextEvent sce) {
         try {
             Driver driver = DriverManager.getDriver("jdbc:mariadb://localhost:3306/users_info");
             DriverManager.deregisterDriver(driver);
         } catch (SQLException ex) {
             ex.printStackTrace();
         }
    }
}

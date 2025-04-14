
package authorization;

import static authorization.Secure.hashPassword;
import java.security.SecureRandom;
import java.util.UUID;

public class User {
    private String login;
    private String password;
    private byte[] salt;
    {
        login = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        salt = Secure.getSalt();
    }
    public String getLogin(){
        return login;
    }
    public String getPassword(){
        return password;
    }
    public byte[] getSalt(){
        return salt;
    }
    public String getHash(String algorithm){
        try{
            return hashPassword(password, salt, algorithm);
        }
        catch(Exception e){
            System.out.println(e.fillInStackTrace());
            return null;
        }
    }
}

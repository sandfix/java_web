
package authorization;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Secure {
    
    public static byte[] getSalt()
    {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
    
    public static String hashPassword(String password, byte[] salt, int iterations, int key_length, String algorithm) throws Exception{
        
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, key_length);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        
        return Base64.getEncoder().encodeToString(hash);    
    }
    
}

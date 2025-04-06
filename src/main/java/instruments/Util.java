package instruments;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;

public class Util {
    private static final Logger logger = Logger.getLogger(Util.class.getName());
    public static class CookieUtil{
        public String contains(Cookie mas[], String name)
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
        
        
        public void delCook(Cookie mas[], String name, HttpServletResponse response){
            
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
        
        
//        public void lsCook(Cookie mas[]){
//           if(mas != null)
//           for(Cookie k: mas)
//           {
//                logger.info(k.getName() + " Existed cookie");
//            } 
//        }
    }
}

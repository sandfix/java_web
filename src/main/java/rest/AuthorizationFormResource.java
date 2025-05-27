
package rest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.inject.Inject;
import services.SecurityService;

@Path("/login")
public class AuthorizationFormResource {

    @Context
    private HttpServletRequest request;

    public static class LoginRequest {
        public String username;
        public String password;
    }
    
    public static class LoginResponse {
        public boolean success;
        public String message;

        public LoginResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        SecurityService ss = new SecurityService();
        User user = ss.checkUser(loginRequest.username, loginRequest.password);
        if(user==null){
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new LoginResponse(false, "Неверный логин или пароль"))
                .build();
        }
        else{
            HttpSession session = request.getSession(true);
            session.setAttribute("user",user);
            return Response.ok(new LoginResponse(true, "Успешный вход")).build();
        }
    }
}

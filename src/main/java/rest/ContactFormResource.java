package rest;

import instruments.PasswordGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import services.ContactFormService;

@Path("/contact")
public class ContactFormResource {
    
    @Context
    private HttpServletRequest request;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleJson(ContactForm form) {
        Map<String, String> errors = new HashMap<>();
        if (form.getName() == null || form.getName().trim().isEmpty()) {
            errors.put("name", "Имя обязательно для заполнения");
        }

        // Валидация телефона
        if (form.getPhone() == null || !form.getPhone().matches("^\\+?[0-9\\-\\s]{7,15}$")) {
            errors.put("phone", "Введите корректный номер телефона");
        }

        // Валидация email
        if (form.getEmail() == null || !form.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            errors.put("email", "Введите корректный email");
        }

        // Валидация сообщения
        if (form.getMessage() == null || form.getMessage().trim().length() < 10) {
            errors.put("message", "Сообщение должно содержать хотя бы 10 символов");
        }
        
        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errors)
                           .build();
        }
        
        ContactFormService formService = new ContactFormService();
        HttpSession session = request.getSession(true);
        User user=null;
        String raw_pass="";
        if(session.getAttribute("user")!=null)
        {
            User user_now = (User)session.getAttribute("user");
            user_now.dataChange(form.getName(), form.getPhone(), form.getEmail(), form.getMessage());
            System.out.println(user_now.getLogin());
            formService.editContactForm(user_now);
            session.setAttribute("user", user_now);
            return Response.ok(Map.of("login", "test", "password", "test123")).build();
        }
        
        else{
            raw_pass = PasswordGenerator.generate();
            user = new User(form, raw_pass);
            formService.saveContactForm(user);
            session.setAttribute("user", user);
        }
        System.out.println(user);
        return Response.ok(Map.of("login", user.getLogin(), "password", raw_pass)).build();
    }
    
}

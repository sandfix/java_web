package rest;

import instruments.PasswordGenerator;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private String name;
    private String password;
    private String login;
    private String phone;
    private String email;
    private String message;
    
    User(String name, String phone, String email, String message){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.message = message;
        String pass = BCrypt.hashpw(PasswordGenerator.generate(),BCrypt.gensalt());
        String login = PasswordGenerator.generate();
        this.login = login;
        this.password = pass;
    }
    
    public User(String name, String phone, String email, String message, String login, String pass){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.message = message;
        this.password = pass;
        this.login = login;
    }
    
    User(ContactForm form){
        this.name = form.getName();
        this.phone = form.getPhone();
        this.email = form.getEmail();
        this.message = form.getMessage();
        String raw_pass = PasswordGenerator.generate();
        String pass = BCrypt.hashpw(raw_pass,BCrypt.gensalt());
        String login = PasswordGenerator.generate();
        this.login = login;
        this.password = pass;
        System.out.println(login);
        System.out.println(raw_pass);
    }
    
    User(ContactForm form, String raw_pass){
        this.name = form.getName();
        this.phone = form.getPhone();
        this.email = form.getEmail();
        this.message = form.getMessage();
        String pass = BCrypt.hashpw(raw_pass,BCrypt.gensalt());
        String login = PasswordGenerator.generate();
        this.login = login;
        this.password = pass;
        System.out.println(login);
        System.out.println(raw_pass);
    }
    
    public void dataChange(String name, String phone, String email, String message){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.message = message;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    
    public String getLogin() { return login; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

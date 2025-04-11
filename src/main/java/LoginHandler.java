/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import authorization.Secure;
import authorization.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Артем
 */
@WebServlet("/login")
public class LoginHandler extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user==null)
        {
            pw.println("null");
            pw.close();
        }
        else
        {
            try{
            String pass = Secure.hashPassword(password,user.getSalt(),65536,256,"PBKDF2WithHmacSHA512");
            if(user.getLogin().equals(login) && user.getHash("PBKDF2WithHmacSHA512").equals(pass))
            {
                pw.println("good");
            }
            else
            {
                pw.println("incorrect");
            }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                pw.close();
            }
        }
    }
}
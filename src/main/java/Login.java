/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.UserAccount;

/**
 *
 * @author Артем
 */
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserAccount user = (UserAccount)session.getAttribute("user");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
            
        if(user==null)
        {
            pw.print("error");
        }
        else
        {
            if(user.getLogin().equals(login) && user.getPassword().equals(password))
                pw.print("you are in");
            else
                pw.print("wrong data");
        }
        pw.close();
    }

}

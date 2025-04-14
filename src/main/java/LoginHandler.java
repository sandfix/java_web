/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import authorization.Secure;
import authorization.User;
import instruments.Util;
import instruments.Util.DButil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.ResultSet;


/**
 *
 * @author Артем
 */
@WebServlet("/login")
public class LoginHandler extends HttpServlet {
    
    private boolean authCheck(String pass1, String pass2_hash, byte[] salt, String algorithm)
    {
        try {
            String pass1_hash = Secure.hashPassword(pass1, salt, algorithm);
            if(pass1_hash.equals(pass2_hash))
                return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession();
        
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        try (Connection conn = Util.DButil.getConnection())
        {
            ResultSet rs = DButil.getAuthInfo(conn, login);
            if(rs.next())
            {
                int id = rs.getInt("user_id");
                String db_password_hash = rs.getString("password");
                String hash_algo = rs.getString("hash_algorithm");
                byte[] salt = rs.getBytes("salt");
                String fio = DButil.getFio(conn, id);
                
                if(authCheck(password, db_password_hash, salt, hash_algo))
                {
                    session.setAttribute("user_id",id);
                    session.setAttribute("user_fio", fio);
                }
            }
            pw.println("wrong pass or user");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally{
            String responseLink = "/web_proj/index.jsp";
            response.sendRedirect(responseLink);
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.UUID;

/**
 *
 * @author Артем
 */
public class UserAccount {
    public UserAccount(){
        login = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        password = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
    private String login;
    private String password;
    public String getLogin(){
        return login;
    }
    public String getPassword(){
        return password;
    }
}

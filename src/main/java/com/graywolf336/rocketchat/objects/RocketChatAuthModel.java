package com.graywolf336.rocketchat.objects;

import java.util.HashMap;

import com.google.gson.annotations.Expose;

public class RocketChatAuthModel {
    @Expose()
    private String password;
    @Expose()
    private String token;
    @Expose()
    private HashMap<String, String> user;
    
    public RocketChatAuthModel(String usernameOrEmail, String passwordOrToken, RocketChatAuthMethod method) {
        this.user = new HashMap<String, String>();
        switch (method) {
            case EMAIL:
                this.password = passwordOrToken;
                this.user.put("email", usernameOrEmail);
                break;
            case USERNAME:
                this.password = passwordOrToken;
                this.user.put("username", usernameOrEmail);
                break;
            case TOKEN:
                this.token = passwordOrToken;
                break;
            default:
                throw new RuntimeException("Invalid authenctiation method.");
        }
    }
    
    public enum RocketChatAuthMethod {
        EMAIL,
        USERNAME,
        TOKEN;
    }
}

package com.test.recipeapp;

public class Users {
    String user_id,name,email;

    public Users(String user_id,String name, String email) {
        this.user_id=user_id;
        this.name = name;
        this.email = email;
    }

    public Users() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

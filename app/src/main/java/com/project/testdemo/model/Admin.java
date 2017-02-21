package com.project.testdemo.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Admin {

    private int id;
    @Expose
    private String name;
    @Expose
    private String pass;

    public Admin(String name,String pass){
        this.name = name;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

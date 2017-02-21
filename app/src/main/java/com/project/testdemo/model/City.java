package com.project.testdemo.model;

/**
 * Created by Administrator on 2017/2/14.
 */

public class City {

    private String id;
    private String name;
    private String pid;

    public City(String id, String name, String pid){
        this.id = id;
        this.name = name;
        this.pid =pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}

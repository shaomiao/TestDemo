package com.project.testdemo.model;

/**
 * Created by Administrator on 2017/2/14.
 */

public class Product {

    private String name;

    private String desc;

    private String pic;

    private String url;

    public Product(String name,String desc, String pic,String url) {
        this.name = name;
        this.desc = desc;
        this.pic = pic;
        this.url = url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }




}

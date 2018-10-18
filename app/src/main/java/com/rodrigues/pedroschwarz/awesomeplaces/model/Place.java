package com.rodrigues.pedroschwarz.awesomeplaces.model;

import java.util.Calendar;

public class Place {

    private String id;
    private String title;
    private String desc;
    private Double price;
    private String country;
    private String image;
    private User author;
    private Long createdAt;

    public Place() {
    }

    public Place(String id, String title, String desc, Double price, String country, String image, User author) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.country = country;
        this.image = image;
        this.author = author;
        this.createdAt = Calendar.getInstance().getTimeInMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}

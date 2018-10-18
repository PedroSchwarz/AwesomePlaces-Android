package com.rodrigues.pedroschwarz.awesomeplaces.model;

import java.util.Calendar;

public class Comment {

    private String id;
    private String placeId;
    private String body;
    private User author;
    private Long createdAt;

    public Comment() {
    }

    public Comment(String id, String placeId, String body, User author) {
        this.id = id;
        this.placeId = placeId;
        this.body = body;
        this.author = author;
        this.createdAt = Calendar.getInstance().getTimeInMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

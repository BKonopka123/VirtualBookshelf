package com.example.virtualbookshelf.model;

public class Book {
    Integer id;
    Integer photoId;
    Integer userId;
    String title;
    String author;
    byte[] photo;
    String description;
    String genre;
    String date;
    String link;
    String status;
    String editInformation;
    Boolean isAdded;

    public Book(Integer id, Integer photoId, Integer userId, String title, String author, byte[] photo, String description, String genre, String date, String link, String status, String editInformation, Boolean isAdded) {
        this.id = id;
        this.photoId = photoId;
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.photo = photo;
        this.description = description;
        this.genre = genre;
        this.date = date;
        this.link = link;
        this.status = status;
        this.editInformation = editInformation;
        this.isAdded = isAdded;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) {this.id = id; }
    public Integer getPhotoId() { return photoId; }
    public void setPhotoId(Integer photoId) { this.photoId = photoId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) {this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) {this.author = author; }
    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }
    public String getDescription() { return description; }
    public void setDescription(String description) {this.description = description; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) {this.genre = genre; }
    public String getDate() { return date; }
    public void setDate(String date) {this.date = date; }
    public String getLink() { return link; }
    public void setLink(String link) {this.link = link; }
    public String getStatus() { return status; }
    public void setStatus(String status) {this.status = status; }
    public String getEditInformation() { return editInformation; }
    public void setEditInformation(String editInformation) {this.editInformation = editInformation; }
    public Boolean getIsAdded() { return isAdded; }
    public void setIsAdded(Boolean isAdded) {this.isAdded = isAdded; }


}

package com.example.virtualbookshelf.model;

public class Photo {
    Integer id;
    Integer photoNumber;
    byte[] photo;
    String date;

    public Photo(Integer id, Integer photoNumber, byte[] photo, String date) {
        this.id = id;
        this.photoNumber = photoNumber;
        this.photo = photo;
        this.date = date;
    }

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id; }
    public Integer getPhotoNumber() { return photoNumber;}
    public void setPhotoNumber(Integer photoNumber) { this.photoNumber = photoNumber; }
    public byte[] getPhoto() { return photo;}
    public void setPhoto(byte[] photo) { this.photo = photo; }
    public String getDate() { return date;}
    public void setDate(String date) { this.date = date; }
}
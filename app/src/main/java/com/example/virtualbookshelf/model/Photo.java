package com.example.virtualbookshelf.model;

/**
 * A class describing a photo.
 */
public class Photo {

    /** Photo id */
    Integer id;
    /** Number of added books */
    Integer photoNumber;
    /** Photo made by user */
    byte[] photo;
    /** Date of photo */
    String date;

    /**
     * Constructor.
     *
     * @param id Photo id.
     * @param photoNumber Number of added books.
     * @param photo Photo made by user.
     * @param date Date of photo.
     */
    public Photo(Integer id, Integer photoNumber, byte[] photo, String date) {
        this.id = id;
        this.photoNumber = photoNumber;
        this.photo = photo;
        this.date = date;
    }

    /** Getters and setters */
    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id; }
    public Integer getPhotoNumber() { return photoNumber;}
    public void setPhotoNumber(Integer photoNumber) { this.photoNumber = photoNumber; }
    public byte[] getPhoto() { return photo;}
    public void setPhoto(byte[] photo) { this.photo = photo; }
    public String getDate() { return date;}
    public void setDate(String date) { this.date = date; }
}

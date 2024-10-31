package com.example.virtualbookshelf.model;

/**
 * A class describing a book.
 */
public class Book {

    /** Book id */
    Integer id;
    /** Photo id  - photo with the book added - foreign key to photo table*/
    Integer photoId;
    /** User id - user who added the book - foreign key to user table*/
    Integer userId;
    /** Book title */
    String title;
    /** Book author */
    String author;
    /** Book photo */
    byte[] photo;
    /** Book description */
    String description;
    /** Book genre */
    String genre;
    /** Date the book was written */
    String date;
    /** Book reading status */
    String status;
    /** Information about whether the book was added by the user*/
    Boolean isAdded;

    /**
     * Constructor.
     *
     * @param id Book id.
     * @param photoId Photo id.
     * @param userId User id.
     * @param title Book title.
     * @param author Book author.
     * @param photo Book photo.
     * @param description Book description.
     * @param genre Book genre.
     * @param date Date the book was written.
     * @param status Book reading status.
     * @param isAdded Information about whether the book was added by the user.
     */
    public Book(Integer id, Integer photoId, Integer userId, String title, String author, byte[] photo, String description, String genre, String date, String status, Boolean isAdded) {
        this.id = id;
        this.photoId = photoId;
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.photo = photo;
        this.description = description;
        this.genre = genre;
        this.date = date;
        this.status = status;
        this.isAdded = isAdded;
    }

    /** Getters and setters */
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
    public String getStatus() { return status; }
    public void setStatus(String status) {this.status = status; }
    public Boolean getIsAdded() { return isAdded; }
    public void setIsAdded(Boolean isAdded) {this.isAdded = isAdded; }


}

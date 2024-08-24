package com.example.virtualbookshelf.model;

public class User {
    Integer id;
    String username;
    byte[] profilePhoto;
    Integer booksNumber;
    Integer booksReadNumber;
    Integer booksUnreadNumber;
    Integer booksCurrentlyNumber;
    Integer booksQueueNumber;

    public User(Integer id, String username, byte[] profilePhoto, Integer booksNumber, Integer booksReadNumber, Integer booksUnreadNumber, Integer booksCurrentlyNumber, Integer booksQueueNumber) {
        this.id = id;
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.booksNumber = booksNumber;
        this.booksReadNumber = booksReadNumber;
        this.booksUnreadNumber = booksUnreadNumber;
        this.booksCurrentlyNumber = booksCurrentlyNumber;
        this.booksQueueNumber = booksQueueNumber;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public byte[] getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(byte[] profilePhoto) { this.profilePhoto = profilePhoto; }
    public Integer getBooksNumber() { return booksNumber; }
    public void setBooksNumber(Integer booksNumber) { this.booksNumber = booksNumber; }
    public Integer getBooksReadNumber() { return booksReadNumber; }
    public void setBooksReadNumber(Integer booksReadNumber) { this.booksReadNumber = booksReadNumber; }
    public Integer getBooksUnreadNumber() { return booksUnreadNumber; }
    public void setBooksUnreadNumber(Integer booksUnreadNumber) { this.booksUnreadNumber = booksUnreadNumber; }
    public Integer getBooksCurrentlyNumber() { return booksCurrentlyNumber; }
    public void setBooksCurrentlyNumber(Integer booksCurrentlyNumber) { this.booksCurrentlyNumber = booksCurrentlyNumber; }
    public Integer getBooksQueueNumber() { return booksQueueNumber; }
    public void setBooksQueueNumber(Integer booksQueueNumber) { this.booksQueueNumber = booksQueueNumber; }
}

package com.example.virtualbookshelf.model;

/**
 * A class describing a user.
 */
public class User {

    /** User id */
    Integer id;
    /** User name */
    String username;
    /** User profile photo */
    byte[] profilePhoto;
    /** Number of books owned by user */
    Integer booksNumber;
    /** Number of books read by user */
    Integer booksReadNumber;
    /** Number of books unread by user */
    Integer booksUnreadNumber;
    /** Number of books currently being read by user */
    Integer booksCurrentlyNumber;
    /** Number of books in the queue */
    Integer booksQueueNumber;

    /**
     * Constructor.
     *
     * @param id User id.
     * @param username User name.
     * @param profilePhoto User profile photo.
     * @param booksNumber Number of books owned by user.
     * @param booksReadNumber Number of books read by user.
     * @param booksUnreadNumber Number of books unread by user.
     * @param booksCurrentlyNumber Number of books currently being read by user.
     * @param booksQueueNumber Number of books in the queue.
     */
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

    /** Getters and setters */
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
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

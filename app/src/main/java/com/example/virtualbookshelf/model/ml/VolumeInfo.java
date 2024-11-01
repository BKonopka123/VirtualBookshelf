package com.example.virtualbookshelf.model.ml;

import java.util.List;

/**
 * The VolumeInfo class represents the volume information of a book.
 */
public class VolumeInfo {
    /**
     * The title of the book.
     */
    private String title;
    /**
     * The authors of the book.
     */
    private List<String> authors;
    /**
     * The publisher of the book.
     */
    private String publisher;
    /**
     * The published date of the book.
     */
    private String publishedDate;
    /**
     * The description of the book.
     */
    private String description;
    /**
     * The categories of the book.
     */
    private List<String> categories;

    /**
     * Getters and setters
     */
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getPublishedDate() { return publishedDate; }
    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

}

package com.example.virtualbookshelf.model.ml;

import java.util.List;

/**
 * A class that holds a response from the Google Books API.
 */
public class BookResponse {

    /**
     * List of Volume items in the response.
     */
    private List<Volume> items;

    /**
     * Get the list of Volume items in the response.
     *
     * @return List of Volume items
     */
    public List<Volume> getItems() { return items; }

    /**
     * Set the list of Volume items in the response.
     *
     * @param items List of Volume items
     */
    public void setItems(List<Volume> items) { this.items = items; }
}

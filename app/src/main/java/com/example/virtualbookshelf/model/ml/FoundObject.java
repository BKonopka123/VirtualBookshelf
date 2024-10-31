package com.example.virtualbookshelf.model.ml;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * The FoundObject class serves as a wrapper for storing data found by detecting text with Tessreact, and data received from the Google Books API.
 * The class implements Parcelable so that its objects can be transferred between Activities.
 */
public class FoundObject implements Parcelable {

    /**
     * A photo from which we detect books
     */
    byte[] image = null;
    /**
     * The text detected by Tesseract
     */
    String foundText = null;
    /**
     * Book title
     */
    String title;
    /**
     * Book authors
     */
    List<String> authors;
    /**
     * Book published date
     */
    String publishedDate;
    /**
     * Book description
     */
    String description;
    /**
     * Book categories
     */
    List<String> categories;

    /**
     * A boolean to check if the book is in the database
     */
    boolean isInDatabase = false;

    /**
     * Default constructor for the FoundObject class
     */
    public FoundObject() {
        this.image = null;
        this.foundText = null;
        this.title = null;
        this.authors = null;
        this.publishedDate = null;
        this.description = null;
        this.categories = null;
        this.isInDatabase = false;
    }

    /**
     * Constructor for the FoundObject class
     * @param image A photo from which we detect books
     * @param foundText The text detected by Tesseract
     * @param title Book title
     * @param authors Book authors
     * @param publishedDate Book published date
     * @param description Book description
     * @param categories Book categories
     * @param isInDatabase A boolean to check if the book is in the database
     */
    public FoundObject(byte[] image, String foundText, String title, List<String> authors, String publishedDate, String description, List<String> categories, boolean isInDatabase) {
        this.image = image;
        this.foundText = foundText;
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.description = description;
        this.categories = categories;
        this.isInDatabase = isInDatabase;
    }

    /**
     * Constructor for the FoundObject class
     * @param in Parcel
     */
    protected FoundObject(Parcel in) {
        this.image = in.createByteArray();
        this.foundText = in.readString();
        this.title = in.readString();
        this.authors = in.createStringArrayList();
        this.publishedDate = in.readString();
        this.description = in.readString();
        this.categories = in.createStringArrayList();
        this.isInDatabase = in.readByte() != 0;
    }

    /**
     * Creator for the FoundObject class needed to transfer objects between Activities
     */
    public static final Creator<FoundObject> CREATOR = new Creator<FoundObject>() {
        @Override
        public FoundObject createFromParcel(Parcel in) {
            return new FoundObject(in);
        }

        @Override
        public FoundObject[] newArray(int size) {
            return new FoundObject[size];
        }
    };

    /**
     * Method needed to transfer objects between Activities
     * @return int
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method needed to transfer objects between Activities. Writes data to parcel
     * @param parcel Parcel
     * @param i int
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeByteArray(image);
        parcel.writeString(foundText);
        parcel.writeString(title);
        parcel.writeStringList(authors);
        parcel.writeString(publishedDate);
        parcel.writeString(description);
        parcel.writeStringList(categories);
        parcel.writeByte((byte) (isInDatabase ? 1 : 0));
    }

    /**
     * Getters and setters
     */
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
    public String getFoundText() { return foundText; }
    public void setFoundText(String foundText) { this.foundText = foundText; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }
    public String getPublishedDate() { return publishedDate; }
    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    public boolean getIsInDatabase() { return isInDatabase; }
    public void setIsInDatabase(boolean isInDatabase) {this.isInDatabase = isInDatabase; }
}
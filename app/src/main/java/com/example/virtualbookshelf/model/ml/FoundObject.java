package com.example.virtualbookshelf.model.ml;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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
     * The book info received from the Google Books API
     */
    String bookInfo = null;
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
        this.bookInfo = null;
        this.isInDatabase = false;
    }

    /**
     * Constructor for the FoundObject class
     * @param image A photo from which we detect books
     * @param foundText The text detected by Tesseract
     * @param bookInfo The book info received from the Google Books API
     * @param isInDatabase A boolean to check if the book is in the database
     */
    public FoundObject(byte[] image, String foundText, String bookInfo, boolean isInDatabase) {
        this.image = image;
        this.foundText = foundText;
        this.bookInfo = bookInfo;
        this.isInDatabase = isInDatabase;
    }

    /**
     * Constructor for the FoundObject class
     * @param in Parcel
     */
    protected FoundObject(Parcel in) {
        this.image = in.createByteArray();
        this.foundText = in.readString();
        this.bookInfo = in.readString();
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
        parcel.writeString(bookInfo);
        parcel.writeByte((byte) (isInDatabase ? 1 : 0));
    }

    /**
     * Getters and setters
     */
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
    public String getFoundText() { return foundText; }
    public void setFoundText(String foundText) { this.foundText = foundText; }
    public String getBookInfo() { return bookInfo; }
    public void setBookInfo(String bookInfo) { this.bookInfo = bookInfo; }
    public boolean getIsInDatabase() { return isInDatabase; }
    public void setIsInDatabase(boolean isInDatabase) {this.isInDatabase = isInDatabase; }
}

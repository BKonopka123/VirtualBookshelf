package com.example.virtualbookshelf.model.ml;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FoundObject implements Parcelable {

    byte[] image = null;
    String foundText = null;
    String bookInfo = null;
    boolean isInDatabase = false;

    public FoundObject() {
        this.image = null;
        this.foundText = null;
        this.bookInfo = null;
        this.isInDatabase = false;
    }

    public FoundObject(byte[] image, String foundText, String bookInfo, boolean isInDatabase) {
        this.image = image;
        this.foundText = foundText;
        this.bookInfo = bookInfo;
        this.isInDatabase = isInDatabase;
    }

    protected FoundObject(Parcel in) {
        this.image = in.createByteArray();
        this.foundText = in.readString();
        this.bookInfo = in.readString();
        this.isInDatabase = in.readByte() != 0;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeByteArray(image);
        parcel.writeString(foundText);
        parcel.writeString(bookInfo);
        parcel.writeByte((byte) (isInDatabase ? 1 : 0));
    }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
    public String getFoundText() { return foundText; }
    public void setFoundText(String foundText) { this.foundText = foundText; }
    public String getBookInfo() { return bookInfo; }
    public void setBookInfo(String bookInfo) { this.bookInfo = bookInfo; }
    public boolean getIsInDatabase() { return isInDatabase; }
    public void setIsInDatabase(boolean isInDatabase) {this.isInDatabase = isInDatabase; }
}

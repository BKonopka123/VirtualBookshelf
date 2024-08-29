package com.example.virtualbookshelf.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class BlobManager {
    byte[] blobObject;

    public BlobManager(Context context, int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        this.blobObject = byteArrayOutputStream.toByteArray();
    }

    public void setBlobObject(Context context, int drawableId) {
        Arrays.fill(this.blobObject, (byte) 0);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        this.blobObject = byteArrayOutputStream.toByteArray();
    }

    public byte[] getBlobObject() {
        return this.blobObject;
    }

    public void loadToUser(User user) {
        user.setProfilePhoto(this.blobObject);
    }

    public void loadToBook(Book book) {
        book.setPhoto(this.blobObject);
    }

    public void loadToPhoto(Photo photo) {
        photo.setPhoto(this.blobObject);
    }

    public static Bitmap getBitmapFromBlob(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }
}

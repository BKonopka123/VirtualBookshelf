package com.example.virtualbookshelf.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A class dealing with blob management - image type conversions and management
 */
public class BlobManager {

    /** Managed image */
    byte[] blobObject;

    /**
     * Constructor. Creates a blob from a drawable.
     *
     * @param context Context of application.
     * @param drawableId Drawable id of image to be converted.
     */
    public BlobManager(Context context, int drawableId) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            this.blobObject = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e("ImageError", "Error getting image bytes - " + e.getMessage(), e);
        }
    }

    /**
     * Setter of blob. Creates a blob from a drawable.
     *
     * @param context Context of application.
     * @param drawableId Drawable id of image to be converted.
     */
    public void setBlobObject(Context context, int drawableId) {
        try {
            Arrays.fill(this.blobObject, (byte) 0);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            this.blobObject = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e("ImageError", "Error getting image bytes - " + e.getMessage(), e);
        }
    }

    /**
     * Getter of blob.
     *
     * @return Blob object.
     */
    public byte[] getBlobObject() {
        return this.blobObject;
    }

    /**
     * Blob setting in user.
     *
     * @param user User object.
     */
    public void loadToUser(User user) {
        user.setProfilePhoto(this.blobObject);
    }

    /**
     * Blob setting in book.
     *
     * @param book Book object.
     */
    public void loadToBook(Book book) {
        book.setPhoto(this.blobObject);
    }

    /**
     * Blob setting in photo.
     *
     * @param photo Photo object.
     */
    public void loadToPhoto(Photo photo) {
        photo.setPhoto(this.blobObject);
    }

    /**
     * byte[] conversion to Bitmap.
     *
     * @param blob Byte array.
     * @return Bitmap object.
     */
    public static Bitmap getBitmapFromBlob(byte[] blob) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return bitmap;
    }

    /**
     * Bitmap conversion to byte[].
     *
     * @param bitmap Bitmap object.
     * @return Byte array.
     */
    public static byte[] getByteFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            Log.e("ImageError", "Error getting image bytes - " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * InputStream from uri conversion to byte[].
     *
     * @param uri InputStream.
     * @return Byte array.
     */
    public static byte[] getByteFromUri(InputStream uri) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = uri.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Log.e("ImageError", "Error getting image bytes - " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Rotate image.
     *
     * @param image Byte array.
     * @param angle Angle at which the photo is rotated.
     */
    public static byte[] rotateImage(byte[] image, float angle) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            image = getByteFromBitmap(bitmap);
            return image;
        } catch (Exception e) {
            Log.e("ImageError", "Error rotating image - " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Resize image.
     *
     * @param original Original image.
     * @param maxWidth Max width of new image.
     * @param maxHeight Max height of new image.
     * @return New resized image.
     */
    public static Bitmap resizeImage(Bitmap original, int maxWidth, int maxHeight){
        try {
            int originalWidth = original.getWidth();
            int originalHeight = original.getHeight();

            float scale = Math.min((float) maxWidth / originalWidth, (float) maxHeight / originalHeight);

            int newWidth = Math.round(originalWidth * scale);
            int newHeight = Math.round(originalHeight * scale);

            return Bitmap.createScaledBitmap(original, newWidth, newHeight, true);
        } catch (Exception e) {
            Log.e("BlobManager", "Could not resize image - " + e.getMessage(), e);
            return null;
        }
    }
}

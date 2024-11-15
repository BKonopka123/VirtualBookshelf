package com.example.virtualbookshelf.model.ml;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.virtualbookshelf.model.BlobManager;
import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * TesseractManager is a class responsible for managing the Tesseract OCR engine.
 */
public class TesseractManager {

    /**
     * Bitmap of the image to be processed.
     */
    private Bitmap photo;
    /**
     * Miniature of the image to be saved to database.
     */
    private byte[] miniature = null;
    /**
     * TessBaseAPI object for processing the image.
     */
    private TessBaseAPI tessBaseAPI;
    /**
     * Language code for English.
     */
    private final String languageEng = "eng";
    /**
     * Language code for Polish.
     */
    private final String languagePol = "pol";
    /**
     * Path to the data directory with language models.
     */
    private final String dataPath;
    /**
     * AssetManager object for accessing assets.
     */
    private final AssetManager assetManager;

    /**
     * Constructor for TesseractManager.
     *
     * @param photo Bitmap of the image to be processed.
     * @param dataPath Path to the data directory with language models.
     * @param assetManager Asset Manager.
     */
    public TesseractManager(Bitmap photo, String dataPath, AssetManager assetManager) {
        this.photo = photo;
        this.dataPath = dataPath;
        this.assetManager = assetManager;
        checkIfLanguageDataExists(new File(dataPath + "tessdata/"));
        initializeTesseract();
        createMiniature();
    }

    /**
     * Finds books in the image.
     *
     * @return List of found books.
     */
    public ArrayList<FoundObject> findBooks() {
        ArrayList<FoundObject> foundObjectsList = new ArrayList<>();

        processImage();

        Bitmap photo0degrees = rotateBitmap(photo, 0);
        Bitmap photo90degrees = rotateBitmap(photo, 90);
        Bitmap photo180degrees = rotateBitmap(photo, 180);
        Bitmap photo270degrees = rotateBitmap(photo, 270);

        tessBaseAPI.setImage(photo0degrees);
        String foundText0degrees = tessBaseAPI.getUTF8Text();
        tessBaseAPI.setImage(photo90degrees);
        String foundText90degrees = tessBaseAPI.getUTF8Text();
        tessBaseAPI.setImage(photo180degrees);
        String foundText180degrees = tessBaseAPI.getUTF8Text();
        tessBaseAPI.setImage(photo270degrees);
        String foundText270degrees = tessBaseAPI.getUTF8Text();
        String foundText = foundText0degrees + "\n" + foundText90degrees + "\n" + foundText180degrees + "\n" + foundText270degrees;

        splitFoundText(foundText, foundObjectsList);

        logFoundText(foundObjectsList);
        closeTesseract();
        return foundObjectsList;
    }

    /**
     * Checks if language data exists.
     * @param directory Directory with language data.
     */
    private void checkIfLanguageDataExists(File directory) {
        if(!directory.exists() && directory.mkdirs()){
            copyLanguageDataFiles();
        }
        if(directory.exists()){
            String dataPathEng = dataPath + "tessdata/" + languageEng + ".traineddata";
            String dataPathPol = dataPath + "tessdata/" + languagePol + ".traineddata";
            File dataFileEng = new File(dataPathEng);
            File dataFilePol = new File(dataPathPol);
            if(!dataFileEng.exists()){
                copyLanguageDataFiles();
            }
            if(!dataFilePol.exists()){
                copyLanguageDataFiles();
            }
        }
    }

    /**
     * Copies language data files to mobile device.
     */
    private void copyLanguageDataFiles() {
        try {
            InputStream inputStreamEng = assetManager.open("tessdata/" + languageEng + ".traineddata");
            InputStream inputStreamPol = assetManager.open("tessdata/" + languagePol + ".traineddata");
            String dataPathEng = dataPath + "tessdata/" + languageEng + ".traineddata";
            String dataPathPol = dataPath + "tessdata/" + languagePol + ".traineddata";
            FileOutputStream outputStreamEng = new FileOutputStream(dataPathEng);
            FileOutputStream outputStreamPol = new FileOutputStream(dataPathPol);

            byte[] bufferEng = new byte[1024];
            int lengthEng;
            while ((lengthEng = inputStreamEng.read(bufferEng)) > 0) {
                outputStreamEng.write(bufferEng, 0, lengthEng);
            }
            outputStreamEng.close();
            inputStreamEng.close();

            byte[] bufferPol = new byte[1024];
            int lengthPol;
            while ((lengthPol = inputStreamPol.read(bufferPol)) > 0) {
                outputStreamPol.write(bufferPol, 0, lengthPol);
            }
            outputStreamPol.close();
            inputStreamPol.close();

        } catch (IOException e) {
            Log.e("TesseractManager", "Error copying language data files - " + e.getMessage(), e);
        }
    }

    /**
     * Initializes Tesseract.
     */
    private void initializeTesseract() {
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(dataPath, languageEng + "+" + languagePol);
        tessBaseAPI.setVariable("tessedit_char_whitelist", "AĄBCĆDEĘFGHIJKLŁMNŃOÓPQRSŚTUVWXYZŻŹaąbcćdeęfghijklłmnoópqrsśtuvwxyzżź ");
        tessBaseAPI.setVariable("user_defined_dpi", "300");
        tessBaseAPI.setVariable("min_characters_to_try", "3");
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
    }

    /**
     * Closes Tesseract.
     */
    private void closeTesseract() {
        tessBaseAPI.recycle();
    }

    /**
     * Logs found text.
     * @param foundObjectsList List of found objects.
     */
    private void logFoundText(ArrayList<FoundObject> foundObjectsList) {
        for (FoundObject foundObject : foundObjectsList) {
            Log.d("TesseractManager", "--------------------------------------------------------");
            if (foundObject.getImage() != null)
                Log.d("TesseractManager", "Found image length: " + foundObject.getImage().length);
            else
                Log.d("TesseractManager", "Found image is null");
            if (foundObject.getFoundText() != null)
                Log.d("TesseractManager", "Found text: " + foundObject.getFoundText());
            else
                Log.d("TesseractManager", "Found text is null");
            if (foundObject.getTitle() != null)
                Log.d("TesseractManager", "Title: " + foundObject.getTitle());
            else
                Log.d("TesseractManager", "Book Title is null");
            if (foundObject.getAuthors() != null)
                Log.d("TesseractManager", "Authors: " + String.join(", ", foundObject.getAuthors()));
            else
                Log.d("TesseractManager", "Authors are null");
            if (foundObject.getPublishedDate() != null)
                Log.d("TesseractManager", "Published date: " + foundObject.getPublishedDate());
            else
                Log.d("TesseractManager", "Published date is null");
            if (foundObject.getDescription() != null)
                Log.d("TesseractManager", "Description: " + foundObject.getDescription());
            else
                Log.d("TesseractManager", "Description is null");
            if (foundObject.getCategories() != null)
                Log.d("TesseractManager", "Categories: " + String.join(", ", foundObject.getCategories()));
            else
                Log.d("TesseractManager", "Categories are null");
            if (foundObject.getIsInDatabase())
                Log.d("TesseractManager", "Is in database: true");
            else
                Log.d("TesseractManager", "Is in database: false");
        }
    }

    /**
     * Process image. Changes the image to gray scale and increases its contrast.
     */
    private void processImage() {
        try {
            // Convert the image to gray scale
            Bitmap photo_GrayScale = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas_GrayScale = new Canvas(photo_GrayScale);
            Paint paint_GrayScale = new Paint();
            ColorMatrix colorMatrix_GrayScale = new ColorMatrix();
            colorMatrix_GrayScale.setSaturation(0);
            ColorMatrixColorFilter colorFilter_GrayScale = new ColorMatrixColorFilter(colorMatrix_GrayScale);
            paint_GrayScale.setColorFilter(colorFilter_GrayScale);
            canvas_GrayScale.drawBitmap(photo, 0, 0, paint_GrayScale);
            photo = photo_GrayScale;

            // Change image contrast and brightness
            float contrast_value = 1.5f;
            float brightness_value = -50;
            Bitmap photo_Contrast = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas_Contrast = new Canvas(photo_Contrast);
            Paint paint_Contrast = new Paint();
            ColorMatrix colorMatrix_Contrast = new ColorMatrix();
            colorMatrix_Contrast.set(new float[]{
                    contrast_value, 0, 0, 0, brightness_value,
                    0, contrast_value, 0, 0, brightness_value,
                    0, 0, contrast_value, 0, brightness_value,
                    0, 0, 0, 1, 0
            });
            ColorMatrixColorFilter colorFilter_Contrast = new ColorMatrixColorFilter(colorMatrix_Contrast);
            paint_Contrast.setColorFilter(colorFilter_Contrast);
            canvas_Contrast.drawBitmap(photo, 0, 0, paint_Contrast);
            photo = photo_Contrast;
        } catch(Exception e) {
            Log.e("TesseractManager", "Error processing image - " + e.getMessage(), e);
        }
    }

    /**
     * Splits found text into lines.
     * @param foundText Found text.
     * @param foundObjectsList List of found objects.
     */
    private void splitFoundText(String foundText, ArrayList<FoundObject> foundObjectsList) {
        try {
            StringBuilder foundLine = new StringBuilder();
            for (int i = 0; i < foundText.length(); i++) {
                char currentChar = foundText.charAt(i);
                if (currentChar == '\n' || i == foundText.length() - 1) {
                    String processedLine = checkFoundText(foundLine.toString());
                    if (!processedLine.isEmpty()) {
                        FoundObject foundObject = new FoundObject(miniature, processedLine, null, null, null, null, null, null, null, false);
                        foundObjectsList.add(foundObject);
//                    Log.d("TesseractManager", "Found text: " + foundLine.toString());
                    }
                    foundLine.setLength(0);
                    continue;
                }
                foundLine.append(currentChar);
            }
        } catch (Exception e) {
            Log.e("TesseractManager", "Error splitting found text - " + e.getMessage(), e);
            foundObjectsList.clear();
        }
    }

    /**
     * Checks if found text is valid.
     * @param foundLine Line with found text.
     * @return Processed line with found text.
     */
    private String checkFoundText(String foundLine) {
        try {
            ArrayList<String> foundWordsList = new ArrayList<>();
            StringBuilder foundWord = new StringBuilder();
            for (int i = 0; i < foundLine.length(); i++) {
                char currentChar = foundLine.charAt(i);
                if (currentChar == ' ' || i == foundLine.length() - 1) {
                    if (currentChar != ' ') {
                        foundWord.append(currentChar);
                    }
                    foundWordsList.add(foundWord.toString());
                    foundWord.setLength(0);
                    continue;
                }
                foundWord.append(currentChar);
            }

            int foundWordNumber = foundWordsList.size();
            int foundWordNumberFalse = 0;

            if (foundWordNumber == 0) {
                return "";
            }

            if (foundWordsList.get(foundWordsList.size() - 1).equals(" ")) {
                foundWordsList.remove(foundWordsList.size() - 1);
            }

            for (String word : foundWordsList) {
                if (word.length() <= 3) {
                    foundWordNumberFalse++;
                }
            }
            if (!((float) foundWordNumberFalse / (float) foundWordNumber >= 0.7)) {
                if (foundWordsList.get(0).length() == 1) {
                    foundWordsList.remove(0);
                }
                if (foundWordsList.get(foundWordsList.size() - 1).length() == 1) {
                    foundWordsList.remove(foundWordsList.size() - 1);
                }
                return String.join(" ", foundWordsList);
            } else {
                return "";
            }
        } catch (Exception e) {
            Log.e("TesseractManager", "Error checking found text - " + e.getMessage(), e);
            return "";
        }
    }

    /**
     * Creates miniature of the image.
     */
    private void createMiniature() {
        try {
            int width = photo.getWidth();
            int height = photo.getHeight();
            int size = Math.min(width, height);

            int x = (width - size) / 2;
            int y = (height - size) / 2;

            Bitmap photoCropped = Bitmap.createBitmap(photo, x, y, size, size);
            Bitmap photoResized = Bitmap.createScaledBitmap(photoCropped, 100, 100, true);

            miniature = BlobManager.getByteFromBitmap(photoResized);
        } catch(Exception e) {
            Log.e("TesseractManager", "Error creating miniature - " + e.getMessage(), e);
        }
    }

    /**
     * Rotates the bitmap.
     * @param bitmap Bitmap to be rotated.
     * @param degree Degree of rotation.
     * @return Rotated bitmap.
     */
    public Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}

package com.example.virtualbookshelf.model.ml;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TesseractManager {

    private final Bitmap photo;
    private TessBaseAPI tessBaseAPI;
    private final String languageEng = "eng";
    private final String languagePol = "pol";
    private final String dataPath;
    private final AssetManager assetManager;

    public TesseractManager(Bitmap photo, String dataPath, AssetManager assetManager) {
        this.photo = photo;
        this.dataPath = dataPath;
        this.assetManager = assetManager;
        checkIfLanguageDataExists(new File(dataPath + "tessdata/"));
        initializeTesseract();

    }

    public ArrayList<FoundObject> findBooks() {
        ArrayList<FoundObject> foundObjectsList = new ArrayList<>();

        tessBaseAPI.setImage(photo);
        String foundText = tessBaseAPI.getUTF8Text();

        FoundObject foundObject = new FoundObject(null, foundText, null, false);
        foundObjectsList.add(foundObject);

        logFoundText(foundObjectsList);
        closeTesseract();
        return foundObjectsList;
    }

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

    private void initializeTesseract() {
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(dataPath, languageEng + "+" + languagePol);
    }

    private void closeTesseract() {
        tessBaseAPI.end();
    }

    private void logFoundText(ArrayList<FoundObject> foundObjectsList) {
        for (FoundObject foundObject : foundObjectsList) {
            if (foundObject.getImage() != null)
                Log.d("TesseractManager", "Found image length: " + foundObject.getImage().length);
            else
                Log.d("TesseractManager", "Found image is null");
            if (foundObject.getFoundText() != null)
                Log.d("TesseractManager", "Found text: " + foundObject.getFoundText());
            else
                Log.d("TesseractManager", "Found text is null");
            if (foundObject.getBookInfo() != null)
                Log.d("TesseractManager", "Book info: " + foundObject.getBookInfo());
            else
                Log.d("TesseractManager", "Book info is null");
            if (foundObject.getIsInDatabase())
                Log.d("TesseractManager", "Is in database: true");
            else
                Log.d("TesseractManager", "Is in database: false");
        }
    }
}

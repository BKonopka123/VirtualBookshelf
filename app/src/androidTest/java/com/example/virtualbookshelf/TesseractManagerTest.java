package com.example.virtualbookshelf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.virtualbookshelf.model.ml.FoundObject;
import com.example.virtualbookshelf.model.ml.TesseractManager;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class TesseractManagerTest {

    private TesseractManager tesseractManager;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.virtualbookshelf", appContext.getPackageName());

        AssetManager assetManager = appContext.getAssets();
        assertNotNull(assetManager);

        String dataPath = appContext.getFilesDir().getAbsolutePath() + "/tesseract/";
        assertNotNull(dataPath);

        Bitmap photo = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(photo);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, 100, 200, paint);

        tesseractManager = new TesseractManager(photo, dataPath, assetManager);
    }

    //void processImage()
    @Test
    public void testProcessImage() {
        Bitmap photo_tmp = tesseractManager.getPhoto();

        Bitmap photo = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(photo);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(0, 0, 100, 200, paint);

        tesseractManager.setPhoto(photo);
        tesseractManager.processImage();
        Bitmap grayScalePhoto = tesseractManager.getPhoto();

        for (int x = 0; x < grayScalePhoto.getWidth(); x++) {
            for (int y = 0; y < grayScalePhoto.getHeight(); y++) {
                int pixel = grayScalePhoto.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                if (red != green || green != blue) {
                    fail();
                }
            }
        }
        assertTrue(true);

        tesseractManager.setPhoto(photo_tmp);
    }

    //void splitFoundText(String foundText, ArrayList<FoundObject> foundObjectsList)
    @Test
    public void testSplitFoundText_WithoutGarbage() {
        String foundText = "This is a test string\n" +
                "This is second test string\n" +
                "This is third test string\n" +
                "This is fourth test string";
        ArrayList<FoundObject> foundObjectsListExpected = new ArrayList<>();
        ArrayList<FoundObject> foundObjectsListTrue = new ArrayList<>();

        foundObjectsListExpected.add(new FoundObject(null, "This is a test string", null, null, null, null, null, null, null, false));
        foundObjectsListExpected.add(new FoundObject(null, "This is second test string", null, null, null, null, null, null, null, false));
        foundObjectsListExpected.add(new FoundObject(null, "This is third test string", null, null, null, null, null, null, null, false));
        foundObjectsListExpected.add(new FoundObject(null, "This is fourth test string", null, null, null, null, null, null, null, false));

        tesseractManager.splitFoundText(foundText, foundObjectsListTrue);

        assertEquals(foundObjectsListExpected.size(), foundObjectsListTrue.size());
        for (int i = 0; i < foundObjectsListExpected.size(); i++) {
            assertEquals(foundObjectsListExpected.get(i).getFoundText(), foundObjectsListTrue.get(i).getFoundText());
        }
    }

    @Test
    public void testSplitFoundText_WithGarbage() {
        String foundText = "This is a test string\n" +
                "a\n" +
                "This is second test string\n" +
                "bc\n" +
                "This is third test string\n" +
                "aa a c\n" +
                "This is fourth test string";
        ArrayList<FoundObject> foundObjectsListExpected = new ArrayList<>();
        ArrayList<FoundObject> foundObjectsListTrue = new ArrayList<>();

        foundObjectsListExpected.add(new FoundObject(null, "This is a test string", null, null, null, null, null, null, null, false));
        foundObjectsListExpected.add(new FoundObject(null, "This is second test string", null, null, null, null, null, null, null, false));
        foundObjectsListExpected.add(new FoundObject(null, "This is third test string", null, null, null, null, null, null, null, false));
        foundObjectsListExpected.add(new FoundObject(null, "This is fourth test string", null, null, null, null, null, null, null, false));

        tesseractManager.splitFoundText(foundText, foundObjectsListTrue);

        assertEquals(foundObjectsListExpected.size(), foundObjectsListTrue.size());
        for (int i = 0; i < foundObjectsListExpected.size(); i++) {
            assertEquals(foundObjectsListExpected.get(i).getFoundText(), foundObjectsListTrue.get(i).getFoundText());
        }
    }

    //String checkFoundText(String foundLine)
    @Test
    public void testCheckFoundText_GoodText() {
        String foundText = "This is a test string";
        String processedText = tesseractManager.checkFoundText(foundText);
        assertEquals(foundText, processedText);
    }

    @Test
    public void testCheckFoundText_EmptyText() {
        String foundText = "    ";
        String processedText = tesseractManager.checkFoundText(foundText);
        assertEquals("", processedText);
    }

    @Test
    public void testCheckFoundText_SpaceEndText() {
        String foundText = "This is a test string ";
        String processedText = tesseractManager.checkFoundText(foundText);
        assertEquals("This is a test string", processedText);
    }

    @Test
    public void testCheckFoundText_GarbageText() {
        String foundText = " This a b cc ddd a b c";
        String processedText = tesseractManager.checkFoundText(foundText);
        assertEquals("", processedText);
    }

    @Test
    public void testCheckFoundText_GarbageTextEndStart() {
        String foundText = "b This a test string which is good a";
        String processedText = tesseractManager.checkFoundText(foundText);
        assertEquals("This a test string which is good", processedText);
    }

    //void createMiniature()
    @Test
    public void testCreateMiniature() {
        Bitmap miniatureExpectedBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(miniatureExpectedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, 100, 100, paint);

        tesseractManager.createMiniature();
        byte[] miniatureTrue = tesseractManager.getMiniature();
        Bitmap miniatureTrueBitmap = BitmapFactory.decodeByteArray(miniatureTrue, 0, miniatureTrue.length);

        assertEquals(miniatureExpectedBitmap.getWidth(), miniatureTrueBitmap.getWidth());
        assertEquals(miniatureExpectedBitmap.getHeight(), miniatureTrueBitmap.getHeight());
    }

    //Bitmap rotateBitmap(Bitmap bitmap, int degree)
    @Test
    public void testRotateBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);

        Bitmap rotatedBitmap = tesseractManager.rotateBitmap(bitmap, 90);

        assertNotNull(rotatedBitmap);
        assertEquals(200, rotatedBitmap.getWidth());
        assertEquals(100, rotatedBitmap.getHeight());
    }

    //void checkIfLanguageDataExists(File directory)
    @Test
    public void testCheckIfLanguageDataExists_DoesNot() {
        String dataPath = tesseractManager.getDataPath();
        File directory = new File(dataPath + "tessdata_tmp/");
        assertFalse(directory.exists());
    }

    @Test
    public void testCheckIfLanguageDataExists_Does() {
        String dataPath = tesseractManager.getDataPath();
        File directory = new File(dataPath + "tessdata/");
        assertTrue(directory.exists());
        String dataPathEng = dataPath + "tessdata/eng.traineddata";
        String dataPathPol = dataPath + "tessdata/pol.traineddata";
        File dataFileEng = new File(dataPathEng);
        File dataFilePol = new File(dataPathPol);
        assertTrue(dataFileEng.exists());
        assertTrue(dataFilePol.exists());
    }

    //void copyLanguageDataFiles()
    @Test
    public void testCopyLanguageDataFiles() {
        String dataPath = tesseractManager.getDataPath();
        File directoryToDelete = new File(dataPath + "tessdata/");
        File dataPathEngToDelete = new File(dataPath + "tessdata/eng.traineddata");
        File dataPathPolToDelete = new File(dataPath + "tessdata/pol.traineddata");
        assertTrue(directoryToDelete.exists());
        assertTrue(dataPathEngToDelete.exists());
        assertTrue(dataPathPolToDelete.exists());
        boolean deleteResultEng = dataPathEngToDelete.delete();
        boolean deleteResultPol = dataPathPolToDelete.delete();
        boolean deleteResultDirectory = directoryToDelete.delete();
        assertTrue(deleteResultEng);
        assertTrue(deleteResultPol);
        assertTrue(deleteResultDirectory);

        File directory = new File(dataPath + "tessdata/");
        tesseractManager.checkIfLanguageDataExists(directory);
        String dataPathEng = dataPath + "tessdata/eng.traineddata";
        String dataPathPol = dataPath + "tessdata/pol.traineddata";
        File dataFileEng = new File(dataPathEng);
        File dataFilePol = new File(dataPathPol);
        assertTrue(dataFileEng.exists());
        assertTrue(dataFilePol.exists());
    }

}

package com.example.virtualbookshelf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.virtualbookshelf.model.ml.FoundObject;
import com.example.virtualbookshelf.model.ml.RetrofitManager;
import com.example.virtualbookshelf.model.ml.TesseractManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(AndroidJUnit4.class)
public class PerformanceTest {

    private String dataPath;
    private Context context;
    private AssetManager assetManager;

    private ArrayList<String[]> inputDataLines;
    private FileWriter outputCsvFileWriter;

    AtomicInteger counter = new AtomicInteger(0);

    private TesseractManager tesseractManager;
    private RetrofitManager retrofitManager;

    private int passedGlobal = 0;
    private int garbageGlobal = 0;
    private int runTest = 0;

    CountDownLatch latchTestRunning;
    AtomicBoolean testSuccess = new AtomicBoolean(true);

    @Before
    public void setUp() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.virtualbookshelf", appContext.getPackageName());
        this.context = appContext;

        AssetManager assetManager = appContext.getAssets();
        assertNotNull(assetManager);
        this.assetManager = assetManager;

        String dataPath = appContext.getFilesDir().getAbsolutePath() + "/tesseract/";
        assertNotNull(dataPath);
        this.dataPath = dataPath;

        InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream("Data/DataInput.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.inputDataLines = new ArrayList<>();
        String line;
        line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            inputDataLines.add(data);
        }
        reader.close();
        assertFalse(inputDataLines.isEmpty());

        File outputCsvFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "DataOutput.csv");
        if (outputCsvFile.exists()) {
            boolean deleted = outputCsvFile.delete();
            if (!deleted) {
                Log.e("PerformanceTest", "Error deleting output file");
                fail();
            }
        }
        this.outputCsvFileWriter = new FileWriter(outputCsvFile);
        this.outputCsvFileWriter.append("Image_name,Test case Description,Books number,Expected Titles,Expected Authors,Percentage,Garbage\n");
        Log.d("PerformanceTest", "Output file path: " + outputCsvFile.getAbsolutePath());
        assertNotNull(outputCsvFile);
    }

    @Test
    public void testPerformance() throws IOException, InterruptedException {
        for(int i = 0; i < inputDataLines.size(); i++) {
            this.latchTestRunning = new CountDownLatch(1);

            Bitmap photoToProcess = loadPhoto(this.inputDataLines.get(counter.get())[0]);
            assertNotNull(photoToProcess);
            this.tesseractManager = new TesseractManager(photoToProcess, this.dataPath, this.assetManager);
            ArrayList<FoundObject> foundTitles = tesseractManager.findBooks();

            boolean shouldApiBeCalled = true;
            if (foundTitles == null || foundTitles.isEmpty()) {
                shouldApiBeCalled = false;
                saveEmptyTesseract(inputDataLines.get(counter.get()));
            }

            if (shouldApiBeCalled) {
                RetrofitManager retrofitManager = new RetrofitManager(foundTitles);
                retrofitManager.findBooks(new Callback<ArrayList<FoundObject>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<FoundObject>> call, @NonNull Response<ArrayList<FoundObject>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            Log.d("PerformanceTest", "Found books: " + response.body().size() + " - saving response to csv file");
                            try {
                                checkOutputAndSaveData(response.body(), inputDataLines.get(counter.get()));
                            } catch (IOException e) {
                                Log.e("PerformanceTest", "Error saving response to csv file");
                                testSuccess.set(false);
                            }
                            if (counter.incrementAndGet() == inputDataLines.size()) {
                                try {
                                    saveFileEnd();
                                } catch (IOException e) {
                                    Log.e("PerformanceTest", "Error saving response to csv file");
                                    testSuccess.set(false);
                                }
                            }
                        } else {
                            Log.e("PerformanceTest", "Error calling API - no response - saving error to csv file");
                            try {
                                saveErrorToCSV(inputDataLines.get(counter.get()));
                            } catch (IOException e) {
                                Log.e("PerformanceTest", "Error saving response to csv file");
                                testSuccess.set(false);
                            }
                        }
                        latchTestRunning.countDown();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<FoundObject>> call, @NonNull Throwable t) {
                        Log.e("PerformanceTest", "Error calling API - onFailure - saving error to csv file");
                        try {
                            saveErrorToCSV(inputDataLines.get(counter.get()));
                        } catch (IOException e) {
                            Log.e("PerformanceTest", "Error saving response to csv file");
                            testSuccess.set(false);
                        }
                        if (Objects.equals(t.getMessage(), "CODE_429")) {
                            Log.e("PerformanceTest", "Error calling API - too many requests - ending Test");
                            try {
                                saveFileEnd();
                            } catch (IOException e) {
                                Log.e("PerformanceTest", "Error saving response to csv file");
                                testSuccess.set(false);
                            }
                        }
                        latchTestRunning.countDown();
                    }
                });

                Log.d("PerformanceTest", "Waiting");
                latchTestRunning.await();
                Log.d("PerformanceTest", "Waiting finished");
                Log.d("PerformanceTest", "Sleep");
                Thread.sleep(5000);
                Log.d("PerformanceTest", "Sleep finished");
                Log.d("PerformanceTest", "Counter: " + counter.get());
                Log.d("PerformanceTest", "passed global: " + passedGlobal);
                Log.d("PerformanceTest", "garbage global: " + garbageGlobal);
                Log.d("PerformanceTest", "run test: " + runTest);
                Log.d("PerformanceTest", "File name:" + inputDataLines.get(counter.get())[0]);
            }

            if (!testSuccess.get()) {
                fail();
            }
            this.latchTestRunning = null;
            this.tesseractManager = null;
            this.retrofitManager = null;
        }
    }

    private Bitmap loadPhoto(String photoName) throws IOException {
        Log.e("PerformanceTest", "Loading photo: " + photoName);
        InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream("Data/Images/" + photoName);
        assertNotNull(inputStream);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        assertNotNull(bitmap);
        return bitmap;
    }

    private void checkOutputAndSaveData(ArrayList<FoundObject> foundBooks, String[] testData) throws IOException  {
        String[] expectedTitles = testData[3].split(";");
        String[] expectedAuthors = testData[4].split(";");
        int passedExpected = Integer.parseInt(testData[2]);
        int passed = 0;
        int garbage;
        int numberOfFoundBooks = foundBooks.size();

        for (int i = 0; i < expectedTitles.length; i++) {
            for (FoundObject foundBook : foundBooks) {
                if(foundBook.getTitle().equals(expectedTitles[i]) && foundBook.getAuthors().contains(expectedAuthors[i])) {
                    passed++;
                    break;
                }
            }
        }

        double percentage = (double)passed / (double)passedExpected;
        garbage = numberOfFoundBooks - passed;

        this.outputCsvFileWriter.append(testData[0]).append(",").append(testData[1]).append(",").append(testData[2]).append(",").append(testData[3]).append(",").append(testData[4]).append(",").append(String.valueOf(percentage)).append(",").append(String.valueOf(garbage)).append("\n");
        passedGlobal += passed;
        garbageGlobal += garbage;
        runTest += passedExpected;
    }

    private void saveErrorToCSV(String[] testData) throws IOException {
        this.outputCsvFileWriter.append(testData[0]).append(",").append(testData[1]).append(",").append(testData[2]).append(",").append(testData[3]).append(",").append(testData[4]).append(",").append("ERROR GETTING DATA").append(",").append("ERROR GETTING DATA").append("\n");
        if (counter.incrementAndGet() == inputDataLines.size()) {
            try {
                saveFileEnd();
            } catch (IOException e) {
                Log.e("PerformanceTest", "Error saving response to csv file");
                fail();
            }
        }
    }

    private void saveFileEnd() throws IOException  {
        double percentage;
        if (runTest == 0)
            percentage = 0;
        else
            percentage = (double)passedGlobal / (double)runTest;
        this.outputCsvFileWriter.append("\n").append("\n").append("Percentage Total").append(",").append("Garbage Total").append("\n");
        this.outputCsvFileWriter.append(String.valueOf(percentage)).append(",").append(String.valueOf(garbageGlobal)).append("\n");
        this.outputCsvFileWriter.close();
        fail();
    }

    private void saveEmptyTesseract(String[] testData) throws IOException {
        this.outputCsvFileWriter.append(testData[0]).append(",").append(testData[1]).append(",").append(testData[2]).append(",").append(testData[3]).append(",").append(testData[4]).append(",").append("0").append(",").append("0").append("\n");
        if (counter.incrementAndGet() == inputDataLines.size()) {
            try {
                saveFileEnd();
            } catch (IOException e) {
                Log.e("PerformanceTest", "Error saving response to csv file");
                fail();
            }
        }
    }
}

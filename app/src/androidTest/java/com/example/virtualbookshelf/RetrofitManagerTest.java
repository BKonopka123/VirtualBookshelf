package com.example.virtualbookshelf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.virtualbookshelf.model.ml.FoundObject;
import com.example.virtualbookshelf.model.ml.RetrofitManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RetrofitManagerTest {

    private RetrofitManager retrofitManager;

    @Before
    public void setUp() {
        ArrayList<FoundObject> foundTitles = new ArrayList<>();
        retrofitManager = new RetrofitManager(foundTitles);
    }

    //boolean checkBookIsValid(FoundObject foundObject)
    @Test
    public void testCheckBookIsValid_OK() {
        List<String> authors = new ArrayList<>();
        authors.add("Joanna Kuciel-Frydryszak");
        FoundObject foundObject = new FoundObject(null, "Chłopki", "aab", "Chłopki: opowieść o naszych babkach", authors, "Marginesy", "2023", null, null, false);
        assertTrue(retrofitManager.checkBookIsValid(foundObject));
    }

    @Test
    public void testCheckBookIsValid_Garbage() {
        List<String> authors = new ArrayList<>();
        authors.add("Jan Sowa");
        FoundObject foundObject = new FoundObject(null, "Chłopki", "aac", "Inna Rzeczpospolita jest możliwa! Widma przeszłości, wizje przyszłości", authors, null, null, null, null, false);
        assertFalse(retrofitManager.checkBookIsValid(foundObject));
    }

    @Test
    public void testCheckBookIsValid_Author() {
        List<String> authors = new ArrayList<>();
        authors.add("Joanna Kuciel-Frydryszak");
        FoundObject foundObject = new FoundObject(null, "Joanna Kuciel-Frydryszak", "aac", "Chłopki: opowieść o naszych babkach", authors, "Marginesy", "2023", null, null, false);
        assertFalse(retrofitManager.checkBookIsValid(foundObject));
    }

    @Test
    public void testCheckBookIsValid_Publisher() {
        List<String> authors = new ArrayList<>();
        authors.add("Joanna Kuciel-Frydryszak");
        FoundObject foundObject = new FoundObject(null, "Marginesy", "aac", "Chłopki: opowieść o naszych babkach", authors, "Marginesy", "2023", null, null, false);
        assertFalse(retrofitManager.checkBookIsValid(foundObject));
    }


    //void removeDuplicateBooks(ArrayList<FoundObject> foundBooks)
    @Test
    public void testRemoveDuplicateBooks() {
        ArrayList<FoundObject> foundBooksSizeBeforeRemoval = new ArrayList<>();
        foundBooksSizeBeforeRemoval.add(new FoundObject(null, "Chłopki", "aaa", null, null, null, null, null, null, false));
        foundBooksSizeBeforeRemoval.add(new FoundObject(null, "Chłopki", "aaa", null, null, null, null, null, null, false));
        assertEquals(foundBooksSizeBeforeRemoval.size(), 2);
        retrofitManager.removeDuplicateBooks(foundBooksSizeBeforeRemoval);
        assertEquals(foundBooksSizeBeforeRemoval.size(), 1);
    }


}

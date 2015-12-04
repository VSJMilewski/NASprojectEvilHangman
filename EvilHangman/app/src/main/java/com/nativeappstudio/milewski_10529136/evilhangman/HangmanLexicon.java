package com.nativeappstudio.milewski_10529136.evilhangman;

import android.app.Activity;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by victor on 24-11-2015.
 *
 * In this class is the lexicon stored. You can call it to get
 * information or words from the lexicon
 */
public class HangmanLexicon {

    /** An array list is created where al the words can be stored in. */
    private List<String> lexicon = new ArrayList<>();

    /** In this variables the number of words is counted. */
    private int lines = 0;

    /** In this variable the size of the longest word is stored. */
    private static int longest = 0;

    /***/
    public HangmanLexicon(XmlResourceParser xrp) {
        try {
            lexicon = WordlistXmlParser.parse(xrp);
            lines = lexicon.size();
            setLongest();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** Finds the length of the longest word in the lexicon. */
    private void setLongest() {
        for(String word : lexicon) {
            if(word.length() > longest) {
                longest = word.length();
            }
        }
    }

    /** Returns the number of words in the lexicon. */
    public int getWordCount() {
        return lines;
    }

    /** returns the length of the longest word in the lexicon. */
    public static int getLongest() {
        return longest;
    }

    /** Returns the word at the specified index. The words stand in the arraylist.  */
    public String getWord(int index) {
        return lexicon.get(index);
    }
}

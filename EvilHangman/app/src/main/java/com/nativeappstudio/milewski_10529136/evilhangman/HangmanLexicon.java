package com.nativeappstudio.milewski_10529136.evilhangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by victor on 24-11-2015.
 *
 * In this class is the lexicon stored. You can call it to get
 * information or words from the lexicon
 */
public class HangmanLexicon {

    /**an array list is created where al the words can be stored in.*/
    private ArrayList<String> lexicon = new ArrayList<String>();

    /**in this variables the number of words is counted.*/
    private int lines = 0;

    /**here the list is read into the araylist. first the file is imported. this file is put in a scanner so you can read the lines. */
    public HangmanLexicon() {
        try {
            File f = new File("HangmanLexicon.txt");
            Scanner s = new Scanner(f);
            while (s.hasNext())//it stots is there are no lines anymore
            {
                lexicon.add(s.next().toUpperCase());//the word is put in the arraylist
                lines++;//one is added to the count of words
            }
            s.close();
        }
        catch(FileNotFoundException e) {
            lexicon.add("KEES");
            lexicon.add("KAAS");
            lexicon.add("PIET");
            lexicon.add("SOEP");
            lexicon.add("POEP");
            lexicon.add("GOED");
            lexicon.add("RAAK");
            lines += 7;
        }
    }
    /** Returns the number of words in the lexicon. */
    public int getWordCount() {
        return lines;
    }

    /** Returns the word at the specified index. The words stand in the arraylist.  */
    public String getWord(int index) {
        return lexicon.get(index);
    }
}

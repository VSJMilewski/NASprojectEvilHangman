package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.content.res.XmlResourceParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by victor on 24-11-2015.
 * Extends the default gameplay.
 * In evil gameplay the system takes all the words from the given length.
 * When the user guesses a letter the system checks all the possible
 * combinations of locations the letter can be in. It chooses the combination
 * where the most words fit in.
 * This way the possible words it can be gets smaller after every guess until
 * only one word remains which the user has to guess.
 */
public class EvilGameplay extends Gameplay {

    /**The array with possible words it can be*/
    public static ArrayList<String> words;

    /**Here is the powerset for the given wordlength stored*/
    private List<Set<Integer>> powerset;

    public EvilGameplay(XmlResourceParser xrp, int length, int guesses) {

        super(xrp, length, guesses);
        createWords();
        selectWord(words.get(0));

        // an original set is created with a range for the length of the word
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < setLength; i++){
            set.add(i);
        }

        //the powerset for the original set is created
        powerset = powerSet(set);
        //the powerset is sorted from small sets to large sets
        Collections.sort(powerset, new Comparator<Set<?>>() {
            @Override
            public int compare(Set<?> o1, Set<?> o2) {
                return Integer.valueOf(o1.size()).compareTo(o2.size());
            }
        });
    }

    /**
     * on construction replaces all the variables with the progress so far
     * @param w         The array with remaining possible words
     * @param guessed   The guessed letters so far
     * @param letters   The word array in bars and guessed letters
     * @param left      The number of left guesses
     * @param sc        The score of the player so far
     * @param set       The number of guesses from the settings
     */
    public EvilGameplay(ArrayList<String> w, char guessed[], char letters[], int left, int sc, int set) {
        super(w.get(0),guessed,letters,left,sc,set);
        words = w;

        // an original set is created with a range for the length of the word
        Set<Integer> s = new HashSet<>();
        for(int i = 0; i < setLength; i++){
            s.add(i);
        }

        //the powerset for the original set is created
        powerset = powerSet(s);
        //the powerset is sorted from small sets to large sets
        Collections.sort(powerset, new Comparator<Set<?>>() {
            @Override
            public int compare(Set<?> o1, Set<?> o2) {
                return Integer.valueOf(o1.size()).compareTo(o2.size());
            }
        });
    }

    /**
     * All the words from the lexicon with the chosen length are placed in a list
     */
    private void createWords() {
        words = new ArrayList<>();
        for(int i = 0; i < lexicon.getWordCount(); i++) {
            String w = lexicon.getWord(i);
            if(w.length() == setLength) {
                words.add(w);
            }
        }
    }

    /**
     * a letter is guessed and the remaining word list is divided and the letter is handled
     * @param letter    The letter that is guessed
     * @param context   The context of the current activity
     */
    @Override
    public void guess(char letter, Context context) {
        ArrayList<ArrayList<String>> newLists = createPowerLists(letter);

        //choose the largest set
        words.clear();
        for(ArrayList<String> list : newLists) {
            if(list.size() > words.size()) {
                words = list;
            }
        }
        //the word is changed and the guess further handled on the ordinary way
        word = words.get(0);
        super.guess(letter,context);
    }

    /**
     * divides all the possible words into the different powersets
     * for the guessed letter.
     * @param letter    The letter that is guessed
     * @return          An arraylist where the words are divided over arraylists according to
     *                  the locations of the letters
     */
    private ArrayList<ArrayList<String>> createPowerLists(char letter) {
        ArrayList<ArrayList<String>> newLists = new ArrayList<>();
        ArrayList<String> temp;

        for(Set<Integer> comb : powerset) {
            temp = new ArrayList<>();
            //test for each word if it belongs to this combination
            for(String w : words) {
                boolean add = true;
                for(int i = 0; i < setLength; i++) {
                    //tests if all the character in this combination are the given letter
                    //and the other characters are not
                    if(comb.contains(i)){
                        if(w.charAt(i) != letter){
                            add=false;
                            break;
                        }
                    } else {
                        if(w.charAt(i) == letter){
                            add=false;
                            break;
                        }
                    }
                }
                //the word belongs to this combination and is added
                if(add){
                    temp.add(w);
                }
            }
            words.removeAll(temp);
            newLists.add(temp);
            if(words.isEmpty()){
                break;
            }
        }

        /*
        int range = powerset.size();
        for(int i = 0; i < range; i++) {
            temp = new ArrayList<>();
            newLists.add(temp);
        }
        for(String w : words) {
            boolean add = true;
            int start = 0;
            int loc = w.indexOf(letter,start);
            Set<Integer> tmp = new HashSet<>();
            while(loc >= 0) {
                tmp.add(loc);
            }
            for(int i = 0; i < range; i++){
                if(tmp.equals(powerset.get(i))) {
                    newLists.get(i).add(w);
                }
            }
        }*/

        return newLists;
    }


    /**
     * Creates the powerset from a given set.
     * if a given set   = [1,2,3]
     * the power set    = [[],[1],[2],[3],[1,2],[1,3],[2,3],[1,2,3]]
     * @param originalSet   A set with all the elements that need to be in the powerset
     * @return  The created powerset
     *
     * Created by: João Silva
     * Copied from: http://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
     *              - first answer
     */
    public static List<Set<Integer>> powerSet(Set<Integer> originalSet) {
        Set<Set<Integer>> sets = new HashSet<>();
        if (originalSet.isEmpty()) {
            sets.add(new TreeSet<Integer>());
            List<Set<Integer>> ls = new ArrayList<>();
            for(Set s : sets) {
                ls.add(s);
            }
            return ls;
        }
        List<Integer> list = new ArrayList<>(originalSet);
        int head = list.get(0);
        Set<Integer> rest = new TreeSet<>(list.subList(1, list.size()));
        for (Set<Integer> set : powerSet(rest)) {
            Set<Integer> newSet = new TreeSet<>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        List<Set<Integer>> ls = new ArrayList<>();
        for(Set s : sets) {
            ls.add(s);
        }
        return ls;
    }
}

package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by victo on 24-11-2015.
 */
public class EvilGameplay extends Gameplay {

    public static ArrayList<String> words;
    private List<Set<Integer>> powerset;

    public EvilGameplay(XmlResourceParser xrp, int length, int guesses) {

        super(xrp, length, guesses);
        createWords();
        selectWord(words.get(0));

        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < setLength; i++){
            set.add(i);
        }
        powerset = powerSet(set);
        Collections.sort(powerset, new Comparator<Set<?>>() {
            @Override
            public int compare(Set<?> o1, Set<?> o2) {
                return Integer.valueOf(o1.size()).compareTo(o2.size());
            }
        });
    }

    public EvilGameplay(ArrayList<String> w, char guessed[], char letters[], int left, int sc, int set) {
        super(w.get(0),guessed,letters,left,sc,set);
        words = w;
    }

    private void createWords() {
        words = new ArrayList<>();
        for(int i = 0; i < lexicon.getWordCount(); i++) {
            String w = lexicon.getWord(i);
            if(w.length() == setLength) {
                words.add(w);
            }
        }
    }

    @Override
    public void guess(char letter, Context context) {
        ArrayList<ArrayList<String>> newLists = new ArrayList<>();
        ArrayList<String> temp;


        for(Set<Integer> comb : powerset) {
            temp = new ArrayList<>();
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
                //all the given positions had the right character and the word is added
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

        //choose the largest set
        words.clear();
        for(ArrayList<String> list : newLists) {
            if(list.size() > words.size()) {
                words = list;
            }
        }
        word = words.get(0);

        super.guess(letter,context);
    }

    /** Function to create powersets:
     * Created by: João Silva
     * Copied from: http://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
     *              - first answer */
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

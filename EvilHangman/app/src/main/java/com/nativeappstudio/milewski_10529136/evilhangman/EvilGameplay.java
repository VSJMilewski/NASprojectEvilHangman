package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by victo on 24-11-2015.
 */
public class EvilGameplay extends Gameplay {

    private ArrayList<String> words;
    private int word_size;
    private Set<Set<Integer>> powerset;

    public EvilGameplay() {
        lexicon = new HangmanLexicon();
        leftGuesses = 8;
        selectWord();
        word_size = word.length();
        createWords();

        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < word_size; i++){
            set.add(i);
        }
        powerset = powerSet(set);
    }

    private void createWords() {
        words = new ArrayList<>();
        for(int i = 0; i < lexicon.getWordCount(); i++) {
            String w = lexicon.getWord(i);
            if(w.length() == word_size) {
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
                for(int i = 0; i < word_size; i++) {
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
            newLists.add(temp);
        }

        //choose the largest set
        words.clear();
        for(ArrayList<String> list : newLists) {
            if(list.size() > words.size()) {
                System.out.println(list);
                words = list;
            }
        }
        word = words.get(0);

        super.guess(letter,context);
    }

    public static Set<Set<Integer>> powerSet(Set<Integer> originalSet) {
        Set<Set<Integer>> sets = new HashSet<Set<Integer>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<Integer>());
            return sets;
        }
        List<Integer> list = new ArrayList<Integer>(originalSet);
        int head = list.get(0);
        Set<Integer> rest = new HashSet<Integer>(list.subList(1, list.size()));
        for (Set<Integer> set : powerSet(rest)) {
            Set<Integer> newSet = new HashSet<Integer>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }
}

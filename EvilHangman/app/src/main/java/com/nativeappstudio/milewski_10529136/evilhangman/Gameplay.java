package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.widget.Toast;
import java.util.Random;

/**
 * Created by victo on 24-11-2015.
 */
public class Gameplay {

    /**the RandomGenerator is imported*/
    protected Random rgen = new Random();

    protected String word;
    protected char wordLetters[];
    protected char lettersGuessed[];
    protected int leftGuesses;
    protected int score;

    protected HangmanLexicon lexicon;

    protected int setGuesses;
    protected int setLength;

    public Gameplay(XmlResourceParser xrp,int length, int guesses) {

        setLength = length;
        setGuesses = guesses;

        score = 0;
        lexicon = new HangmanLexicon(xrp);
        resetGuesses();
    }

    public Gameplay(String w, char guessed[], char letters[], int left, int sc, int set) {

        setLength = letters.length;
        setGuesses = set;
        score = sc;
        leftGuesses = left;
        lettersGuessed = guessed;
        wordLetters = letters;
        word = w;
    }

    public void resetGuesses() {
        lettersGuessed = new char[26];
        leftGuesses = setGuesses;
    }

    public void selectWord() {
        do {
            word = lexicon.getWord(rgen.nextInt(lexicon.getWordCount()));
        } while (word.length() != setLength);
        wordLetters = new char[word.length()];
        for(int i = 0; i < word.length(); i++) {
            wordLetters[i] = '-';
        }
    }

    public void selectWord(String w) {

        word = w;
        wordLetters = new char[word.length()];
        for(int i = 0; i < word.length(); i++) {
            wordLetters[i] = '-';
        }
    }

    public String guessedString() {
        if(lettersGuessed[0] == '\0') {
            return "Nothing Guessed.";
        } else {
            String letters = "";
            for(int i = 0; i < lettersGuessed.length; i++) {
                if(lettersGuessed[i] == '\0') {
                    break;
                } else {
                    letters += "-"+lettersGuessed[i];
                }
            }
            return letters;
        }
    }

    public String guessesString() {
        return "You have "+ leftGuesses+" left.";
    }

    public String wordString() {
        String wordstripes = "";
        for(int i = 0; i < wordLetters.length; i++) {
            wordstripes += wordLetters[i]+" ";
        }
        return wordstripes;
    }

    public boolean guessed(char letter) {
        for(char temp : lettersGuessed) {
            if(temp == letter) {
                return true;
            }
        }
        return false;
    }

    protected void addGuessedLetter(char letter) {
        for(int i = 0; i < lettersGuessed.length; i++) {
            if(lettersGuessed[i] == '\0'){
                lettersGuessed[i] = letter;
                break;
            }
        }
    }

    public void guess(char letter, Context context) {
        if(word.indexOf(letter) < 0) {
            leftGuesses -= 1;
            Toast.makeText(context, "There are no " + letter + "'s in the word",
                    Toast.LENGTH_SHORT).show();
        } else {
            for(int i = 0; i < word.length(); i++)//it checks every letter
            {
                if(word.charAt(i) == letter)
                {
                    wordLetters[i] = letter;//the dash is removed and the letter is placed
                }

            }
        }
        addGuessedLetter(letter);
    }

    public void addScore(int i) {
        score += i;
    }
}

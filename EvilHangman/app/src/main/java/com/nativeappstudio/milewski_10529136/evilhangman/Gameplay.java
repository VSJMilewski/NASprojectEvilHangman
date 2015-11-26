package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
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
    protected char lettersGuessed[] = new char[26];
    protected int leftGuesses;

    protected HangmanLexicon lexicon;

    public Gameplay() {
        lexicon = new HangmanLexicon();
        leftGuesses = 8;
        selectWord();
    }

    public void selectWord() {
        word = lexicon.getWord(rgen.nextInt(lexicon.getWordCount()));
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
}

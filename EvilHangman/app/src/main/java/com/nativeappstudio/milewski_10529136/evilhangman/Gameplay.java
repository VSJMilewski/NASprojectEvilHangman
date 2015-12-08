package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.widget.Toast;
import java.util.Random;

/**
 * Created by victor on 24-11-2015.
 * This is where all the variables and the actions in the game are handled.
 * It is a normal version of hangman where one word is chosen and the player
 * has to guess it.
 */
public class Gameplay {

    /**The RandomGenerator is imported*/
    protected Random rgen = new Random();

    /**The chosen word*/
    protected String word;

    /**The word array filled with bars and guessed letters*/
    protected char wordLetters[];

    /**All the letters the player has guessed*/
    protected char lettersGuessed[];

    /**The ammount of left guesses*/
    protected int leftGuesses;

    /**The score of the player so far*/
    protected int score;

    /**The lexicon where the entire word list is stored*/
    protected HangmanLexicon lexicon;

    /**The number of guesses as set in the settings*/
    protected int setGuesses;

    /**The word length as set in the settings*/
    protected int setLength;


    public Gameplay(XmlResourceParser xrp,int length, int guesses) {

        setLength = length;
        setGuesses = guesses;

        score = 0;
        lexicon = new HangmanLexicon(xrp);
        resetGuesses();
    }

    /**
     * on construction replaces all the variables with the progress so far
     * @param w         The chosen word for this game
     * @param guessed   The guessed letters so far
     * @param letters   The word array in bars and guessed letters
     * @param left      The number of left guesses
     * @param sc        The score of the player so far
     * @param set       The number of guesses from the settings
     */
    public Gameplay(String w, char guessed[], char letters[], int left, int sc, int set) {

        setLength = letters.length;
        setGuesses = set;
        score = sc;
        leftGuesses = left;
        lettersGuessed = guessed;
        wordLetters = letters;
        word = w;
    }

    /**
     * The variables for the guesses are reset for a new game
     */
    public void resetGuesses() {
        lettersGuessed = new char[26];
        leftGuesses = setGuesses;
    }

    /**
     * A random word with the given length is chosen from the lexicon
     */
    public void selectWord() {
        do {
            word = lexicon.getWord(rgen.nextInt(lexicon.getWordCount()));
        } while (word.length() != setLength);
        //fill in the word array with bars, because no letters are guessed yet
        wordLetters = new char[word.length()];
        for(int i = 0; i < word.length(); i++) {
            wordLetters[i] = '-';
        }
    }

    /**
     * Set the given word as the chosen word for this game
     * @param w     The word that is chosen for this game
     */
    public void selectWord(String w) {
        word = w;
        //fill in the word array with bars, because no letters are guessed yet
        wordLetters = new char[word.length()];
        for(int i = 0; i < word.length(); i++) {
            wordLetters[i] = '-';
        }
    }

    /**
     * Goes through the array of guessed letters and places them into a string
     * @return A string made from the letters that are guessed
     */
    public String guessedString() {
        if(lettersGuessed[0] == '\0') {
            return "Nothing Guessed.";
        } else {
            String letters = "";
            for(int i = 0; i < lettersGuessed.length; i++) {
                //if so many letters are not yet guessed stop the loop
                if(lettersGuessed[i] == '\0') {
                    break;
                } else {
                    letters += "-"+lettersGuessed[i];
                }
            }
            return letters;
        }
    }

    /**
     * Makes from the number of left guesses a message
     * @return A string with the left number of guesses
     */
    public String guessesString() {
        return "You have "+ leftGuesses+" left.";
    }

    /**
     * Goes through the array of word letters and places them into a string
     * @return A string made from the word with bars and correct guesses so far
     */
    public String wordString() {
        String wordstripes = "";
        for(int i = 0; i < wordLetters.length; i++) {
            wordstripes += wordLetters[i]+" ";
        }
        return wordstripes;
    }

    /**
     * Test if a given letter is guessed before
     * @param letter    The letter that is guessed
     * @return          If the letter was guessed before
     */
    public boolean guessed(char letter) {
        for(char temp : lettersGuessed) {
            if(temp == letter) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the given letter to the array of guessed letters
     * @param letter    The guessed letter
     */
    protected void addGuessedLetter(char letter) {
        for(int i = 0; i < lettersGuessed.length; i++) {
            //Place the letter in the first empty spot
            if(lettersGuessed[i] == '\0'){
                lettersGuessed[i] = letter;
                break;
            }
        }
    }

    /**
     * Handles the guess for this letter and reports of wrong and/or places it in the arrays
     * for word letters and guessed letters
     * @param letter    The letter that is guessed
     * @param context   The context of the current activity
     */
    public void guess(char letter, Context context) {
        //If it is not in the word it is reported to the player and a guess is lost
        if(word.indexOf(letter) < 0) {
            leftGuesses -= 1;
            Toast.makeText(context, "There are no " + letter + "'s in the word",
                    Toast.LENGTH_SHORT).show();
        //The letter is in the word and is added to the word array
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

    /**
     * A number of points is added to the score
     * @param i The won number of points
     */
    public void addScore(int i) {
        score += i;
    }
}

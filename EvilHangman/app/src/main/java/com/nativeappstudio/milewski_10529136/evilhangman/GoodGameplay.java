package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by victo on 24-11-2015.
 */
public class GoodGameplay extends Gameplay {

    public GoodGameplay() {
        lexicon = new HangmanLexicon();
        leftGuesses = 8;
        selectWord();
    }

    @Override
    public void guess(char letter, Context context) {
        if(word.indexOf(letter) < 0) {
            leftGuesses -= 1;
            Toast.makeText(context, "There are no " + letter + "'s in the word, from good",
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

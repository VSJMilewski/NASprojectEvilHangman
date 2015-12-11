package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.res.XmlResourceParser;

/**
 * Created by victor on 24-11-2015.
 * Good gameplay is exactly the same as normal gameplay
 */
public class GoodGameplay extends Gameplay {

    public GoodGameplay(int length, int guesses, HangmanLexicon lex) {
        super(length, guesses, lex);
        selectWord();
    }

    /**
     * on construction replaces all the variables with the progress so far
     */
    public GoodGameplay(String w, char guessed[], char letters[], int left, int sc, int set,HangmanLexicon lex) {
        super(w,guessed,letters,left,sc,set,lex);
    }

}

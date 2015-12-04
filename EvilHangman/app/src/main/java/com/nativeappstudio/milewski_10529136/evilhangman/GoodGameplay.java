package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.widget.Toast;

/**
 * Created by victo on 24-11-2015.
 */
public class GoodGameplay extends Gameplay {

    public GoodGameplay(XmlResourceParser xrp, int length, int guesses) {
        super(xrp, length, guesses);
        selectWord();
    }

    public GoodGameplay(String w, char guessed[], char letters[], int left, int sc, int set) {
        super(w,guessed,letters,left,sc,set);
    }

}

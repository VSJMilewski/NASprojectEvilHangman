package com.nativeappstudio.milewski_10529136.evilhangman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private TextView guessesView;
    private TextView guessedView;
    private TextView wordView;
    private EditText enterView;

    private Gameplay game;
    private boolean evilType;

    private SharedPreferences preferences;
    private static final String prefSettings = "settings";
    private static final String prefType = "type";
    private static final String prefLength = "wordLength";
    private static final String prefGuess = "guesses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        guessedView = (TextView) findViewById(R.id.guessedLetters);
        guessesView = (TextView) findViewById(R.id.guessesLeft);
        wordView = (TextView) findViewById(R.id.word);
        enterView = (EditText) findViewById(R.id.enterLetter);

        XmlResourceParser xrp = this.getResources().getXml(R.xml.words);
        preferences = getSharedPreferences(prefSettings, Context.MODE_PRIVATE);
        int length = preferences.getInt(prefLength,5);
        int guesses = preferences.getInt(prefGuess,8);
        evilType = preferences.getBoolean(prefType,true);
        if(evilType) {
            game = new EvilGameplay(xrp, length, guesses);
        } else {
            game = new GoodGameplay(xrp, length, guesses);
        }

        setText();
    }

    public void guessLetter(View view) {
        String guess = enterView.getText().toString();
        if(guess.length() != 1) {
            Toast.makeText(getApplicationContext(), "Please enter 1 letter",
                    Toast.LENGTH_SHORT).show();
        } else {

            char letter = guess.toUpperCase().charAt(0);

            if(game.guessed(letter)) {
                Toast.makeText(getApplicationContext(), "Already guessed this letter",
                        Toast.LENGTH_SHORT).show();
            } else {
                game.guess(letter, getApplicationContext());
            }
        }
        setText();
        gameEnd();
    }

    private void setText() {

        guessedView.setText(game.guessedString());
        guessesView.setText(game.guessesString());
        wordView.setText(game.wordString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_highscores) {
            Intent intent = new Intent(this,HighscoreActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_newgame) {
            Intent intent = new Intent(this,GameActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void gameEnd() {
        if(game.leftGuesses == 0) {
            AlertDialog.Builder end = new AlertDialog.Builder(GameActivity.this);
            end.setTitle(R.string.gameover);
            end.setMessage(getResources().getString(R.string.end) + game.word);
            end.setPositiveButton(R.string.end_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(GameActivity.this, HighscoreActivity.class);
                    intent.putExtra("gamescore", game.score);
                    intent.putExtra("wordlength", game.setLength);
                    if(evilType) {
                        intent.putExtra("gametype", "evil");
                    } else {
                        intent.putExtra("gametype", "good");
                    }
                    startActivity(intent);
                    finish();
                }
            });
            end.setCancelable(false);
            end.create();
            end.show();
        } else if(game.wordString().indexOf("-") < 0) {
            game.addScore(1);
            AlertDialog.Builder end = new AlertDialog.Builder(GameActivity.this);
            end.setTitle(R.string.guessed);
            end.setMessage(getResources().getString(R.string.guess_end) + game.word);
            end.setPositiveButton(R.string.next_word, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nextWord();
                }
            });
            end.setCancelable(false);
            end.create();
            end.show();
        }
    }

    public void nextWord() {
        game.selectWord();
        game.resetGuesses();
        setText();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("evil", evilType);
        if(evilType){
            outState.putSerializable("words", EvilGameplay.words);
        } else {
            outState.putString("word", game.word);
        }
        outState.putCharArray("guessed", game.lettersGuessed);
        outState.putCharArray("letters", game.wordLetters);
        outState.putInt("left", game.leftGuesses);
        outState.putInt("score", game.score);
        outState.putInt("setGuesses", game.setGuesses);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        evilType = inState.getBoolean("evil");
        char guessed[] = inState.getCharArray("guessed");
        char letters[] = inState.getCharArray("letters");
        int score = inState.getInt("score");
        int left = inState.getInt("left");
        int set = inState.getInt("setGuesses");
        if(evilType) {
            ArrayList<String> words = (ArrayList<String>) inState.getSerializable("words");
            game = new EvilGameplay(words,guessed,letters,left,score,set);
        } else {
            String word = inState.getString("word");
            game = new GoodGameplay(word,guessed,letters,left,score,set);
        }
        setText();
    }
}

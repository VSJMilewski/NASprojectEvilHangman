package com.nativeappstudio.milewski_10529136.evilhangman;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private TextView guessesView;
    private TextView guessedView;
    private TextView wordView;
    private EditText enterView;

    private Gameplay game;

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

        game = new EvilGameplay();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

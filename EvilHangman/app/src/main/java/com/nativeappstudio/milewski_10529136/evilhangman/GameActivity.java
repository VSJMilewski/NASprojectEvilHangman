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

    private String word;
    private char wordLetters[];
    private char lettersGuessed[] = new char[26];
    private int leftGuesses;

    private TextView guessesView;
    private TextView guessedView;
    private TextView wordView;
    private EditText enterView;

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

        prepareGame();
    }

    public void guessLetter(View view) {
        String guess = enterView.getText().toString();
        if(guess.length() != 1) {
            Toast.makeText(getApplicationContext(), "Please enter 1 letter",
                    Toast.LENGTH_SHORT).show();
        } else {

            char letter = guess.charAt(0);
            boolean guessed = false;
            for(char temp : lettersGuessed) {
                if(temp == letter) {
                    guessed = true;
                }
            }

            if(guessed)  {
                Toast.makeText(getApplicationContext(), "Already guessed this letter",
                        Toast.LENGTH_SHORT).show();
            } else {
                if(word.indexOf(letter) < 0) {
                    leftGuesses -= 1;
                    Toast.makeText(getApplicationContext(), "There are no "+letter+"'s in the word",
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
                for(int i = 0; i < lettersGuessed.length; i++) {
                    if(lettersGuessed[i] == '\0'){
                        lettersGuessed[i] = letter;
                        break;
                    }
                }
            }
        }
        setText();
    }

    private void prepareGame() {
        leftGuesses = 8;
        selectWord();
        setText();
    }

    private void selectWord() {
        word = "sinterklaas";
        wordLetters = new char[word.length()];
        for(int i = 0; i < word.length(); i++) {
            wordLetters[i] = '-';
        }
    }

    private void setText() {
        if(lettersGuessed[0] == '\0') {
            guessedView.setText("Nothing Guessed.");
        } else {
            String letters = "";
            for(int i = 0; i < lettersGuessed.length; i++) {
                if(lettersGuessed[i] == '\0') {
                    break;
                } else {
                    letters += "-"+lettersGuessed[i];
                }
            }
            guessedView.setText(letters);
        }

        guessesView.setText("You have "+ leftGuesses+" left.");

        String wordstripes = "";
        for(int i = 0; i < wordLetters.length; i++) {
            wordstripes += wordLetters[i]+" ";
        }
        wordView.setText(wordstripes);
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

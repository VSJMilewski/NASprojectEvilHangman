package com.nativeappstudio.milewski_10529136.evilhangman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the activity where the app starts. In here is the game loaded
 * and with use of an object from class Gameplay, the game is
 * played.
 */
public class GameActivity extends AppCompatActivity {

    /** The name of the save file for the game */
    private final String SaveFile = "gameSave";

    /** All the different views in this activity */
    private TextView guessesView;
    private TextView guessedView;
    private TextView wordView;
    private EditText enterView;

    /** Where the game is stored and played*/
    private Gameplay game;

    /** true if the game type is evil */
    private boolean evilType;

    /** Names for the sharedpreferences variables */
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

        preferences = getSharedPreferences(prefSettings, Context.MODE_PRIVATE);
        int length = preferences.getInt(prefLength,5);
        if(length > 14) {
            /*if the length is larger, it takes very long to make the powerset.
            The game might look stuck, to make sure it does not happen the next
            time playing.
             */
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(prefLength, 5);
            editor.commit();
        }
        int guesses = preferences.getInt(prefGuess,8);
        evilType = preferences.getBoolean(prefType,true);
        final HangmanLexicon lexicon = (HangmanLexicon) getApplicationContext();

        //create the game depending on the settings
        if(evilType) {
            game = new EvilGameplay(length, guesses, lexicon);
        } else {
            game = new GoodGameplay(length, guesses, lexicon);
        }

        //if it is a new game, don't read in a file
        if(getIntent().getBooleanExtra("new",false)) {
            getIntent().removeExtra("new");
        } else {
            readFile();
        }
        setText();
    }

    /**
     * Reads in the filled EditText, tests if correct
     * and handles it correctly
     * @param view The button that was pressed
     */
    public void guessLetter(View view) {
        String guess = enterView.getText().toString();
        //It must only be one character
        if(guess.length() != 1) {
            Toast.makeText(getApplicationContext(), "Please enter 1 letter",
                    Toast.LENGTH_SHORT).show();
        } else {
            //All the characters are tested on uppercase
            char letter = guess.toUpperCase().charAt(0);

            //tests if it wasn't guessed before
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

    /**
     * Changes all the textviews so they have the updated text
     */
    private void setText() {

        guessedView.setText(game.guessedString());
        guessesView.setText(game.guessesString());
        wordView.setText(game.wordString());
        setImage();
    }

    /**
     * Changes the image of the gallow depending on the left
     * guesses.
     */
    private void setImage() {
        ImageView hang = (ImageView) findViewById(R.id.hangImg);
        if(game.leftGuesses >= 7) {
            hang.setImageResource(R.drawable.img7);
        } else if(game.leftGuesses == 6) {
            hang.setImageResource(R.drawable.img6);
        } else if(game.leftGuesses == 5) {
            hang.setImageResource(R.drawable.img5);
        } else if(game.leftGuesses == 4) {
            hang.setImageResource(R.drawable.img4);
        } else if(game.leftGuesses == 3) {
            hang.setImageResource(R.drawable.img3);
        } else if(game.leftGuesses == 2) {
            hang.setImageResource(R.drawable.img2);
        } else if(game.leftGuesses == 1) {
            hang.setImageResource(R.drawable.img1);
        } else if(game.leftGuesses == 0) {
            hang.setImageResource(R.drawable.img0);
        }
    }

    /**
     * Tests if the game has ended. It tests if you have lost or you won
     * and you can continue to the next word. It gives the message to the
     * user with a dialog.
     */
    public void gameEnd() {
        //the player has no guesses left and lost
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
                    if (evilType) {
                        intent.putExtra("gametype", "evil");
                    } else {
                        intent.putExtra("gametype", "good");
                    }
                    startActivity(intent);
                    finish();
                }
            });
            //he must go to the highscorescreen
            end.setCancelable(false);
            end.create();
            end.show();
        //All the letters of the word have been guessed
        } else if(game.wordString().indexOf("-") < 0) {
            //The player gets a point
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
            //The player must go to the next word
            end.setCancelable(false);
            end.create();
            end.show();
        }
    }

    /**
     * Prepares the game for the next round. Next word is loaded in
     * and the guesses are reset
     */
    public void nextWord() {
        game.selectWord();
        game.resetGuesses();
        setText();
    }

    /**
     * Reads in a file where the game was saved. If the player was in the
     * middle of a game, he can continue from this point.
     */
    private void readFile() {
        Scanner scan = null;
        try {
            scan = new Scanner(openFileInput(SaveFile));
            //There is a previous game to continue
            if (scan.hasNextLine() && !scan.nextLine().equals("<NO>")) {
                String type = scan.nextLine();
                ArrayList<String> words = new ArrayList<>();
                String word = "";
                if(type.equals("evil")) {
                    String line = scan.nextLine();
                    while(!line.equals("<STOP>")) {
                        words.add(line);
                        line = scan.nextLine();
                    }
                } else {
                    word = scan.nextLine();
                }
                int score = Integer.parseInt(scan.nextLine());
                int leftGuesses = Integer.parseInt(scan.nextLine());
                int setLength = Integer.parseInt(scan.nextLine());
                int setGuesses = Integer.parseInt(scan.nextLine());
                char wordLetters[] = new char[setLength];
                for (int i = 0; i < setLength; i++) {
                    wordLetters[i] = scan.nextLine().charAt(0);
                }
                char lettersGuessed[] = new char[26];
                for(int i = 0; i < 26; i++) {
                    lettersGuessed[i] = scan.nextLine().charAt(0);
                }
                HangmanLexicon lexicon = (HangmanLexicon) getApplicationContext();
                //Everything is readed in and the loaded game is created
                if(type.equals("evil")) {
                    game = new EvilGameplay(words,lettersGuessed,wordLetters,leftGuesses,score,setGuesses,lexicon);
                } else {
                    game = new GoodGameplay(word,lettersGuessed,wordLetters,leftGuesses,score,setGuesses,lexicon);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the game so that the player can continue at a different time
     */
    private void saveFile() {
        PrintStream out = null;
        try {
            out = new PrintStream(openFileOutput(SaveFile, MODE_PRIVATE));
            //there is no game to be saved
            if(game.leftGuesses == 0 || (game.score == 0 && game.lettersGuessed[0] == '\0')){
                out.println("<NO>");
            //there is a gam to be saved
            } else {
                out.println("<YES>");
                if(game.getClass().equals(EvilGameplay.class)) {
                    out.println("evil");
                    for(String w : EvilGameplay.words) {
                        out.println(w);
                    }
                    out.println("<STOP>");
                } else {
                    out.println("good");
                    out.println(game.word);
                }
                out.println(game.score);
                out.println(game.leftGuesses);
                out.println(game.setLength);
                out.println(game.setGuesses);
                for(char c : game.wordLetters) {
                    out.println(c);
                }
                for(char c : game.lettersGuessed) {
                    out.println(c);
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        saveFile();
        super.onPause();
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
            intent.putExtra("new",true);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The state is saved so that it is save to rotate the screen
     */
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

    /**
     * The state is restored so the player can continue playing
     */
    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        evilType = inState.getBoolean("evil");
        char guessed[] = inState.getCharArray("guessed");
        char letters[] = inState.getCharArray("letters");
        int score = inState.getInt("score");
        int left = inState.getInt("left");
        int set = inState.getInt("setGuesses");
        final HangmanLexicon lexicon = (HangmanLexicon) getApplicationContext();
        //the correct type of game is created
        if(evilType) {
            ArrayList<String> words = (ArrayList<String>) inState.getSerializable("words");
            game = new EvilGameplay(words,guessed,letters,left,score,set,lexicon);
        } else {
            String word = inState.getString("word");
            game = new GoodGameplay(word,guessed,letters,left,score,set,lexicon);
        }
        setText();
    }
}

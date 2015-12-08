package com.nativeappstudio.milewski_10529136.evilhangman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class HighscoreActivity extends AppCompatActivity {

    /**The name of the save file for the highscores*/
    private final String SaveFile = "HighScoresSave";

    /**The object where all the highscores are saved*/
    private HighScores highScores;

    /**The switch that tells if the good or the evil scores need to be shown*/
    private Switch GoodEvil;

    /**The table where the highscores are printed in*/
    private TableLayout scoreTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        highScores = new HighScores();
        //add all the highscores from the file
        readFile();

        Intent intent = getIntent();
        //test if a new score is passed
        if(intent.hasExtra("gamescore")) {
            int points = intent.getIntExtra("gamescore", 0);
            String type = intent.getStringExtra("gametype");
            //if it is a new highscore the player can add their name for the score in an dialog
            if (newHighScore(points, type)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(R.string.gameover);

                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View promptView = layoutInflater.inflate(R.layout.highscore_prompt, null);
                dialog.setView(promptView);
                final EditText nameScore = (EditText) promptView.findViewById(R.id.name_score);
                dialog.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = nameScore.getText().toString();
                        addScore(text);
                    }
                });
                dialog.setCancelable(false);
                dialog.create();
                dialog.show();
            }
        }

        scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        GoodEvil = (Switch) findViewById(R.id.scoreType);
        GoodEvil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fillTable(isChecked);
            }
        });

        fillTable(GoodEvil.isChecked());

    }

    /**
     * adds the new score to the highscores with all the needed infromation from the intent
     * @param name  The name of the user who got this score
     */
    public void addScore(String name) {
        Intent intent = getIntent();
        int points = intent.getIntExtra("gamescore", 0);
        String type = intent.getStringExtra("gametype");
        int length = intent.getIntExtra("wordlength", 4);
        highScores.newScore(new Score(points, name, length, type));
        fillTable(GoodEvil.isChecked());
    }

    @Override
    protected void onPause() {
        saveFile();
        super.onPause();
    }

    /**
     * The old highscores are loaded into the game from the text file
     */
    public void readFile() {
        Scanner scan = null;
        try {
            scan = new Scanner(openFileInput(SaveFile));
            //if there are scores in the file, empty the highscore list
            if(scan.hasNextLine()) {
                highScores.clear();
            }
            //keep reading as long as there are scores
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                //this is the next score and the next 4 lines are needed data for 1 score
                if(line.equals("<S>")) {
                    int points = Integer.parseInt(scan.nextLine());
                    String name = scan.nextLine();
                    int length = Integer.parseInt(scan.nextLine());
                    String type = scan.nextLine();
                    highScores.newScoreRead(new Score(points, name, length, type));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The highscores are saved into a textfile
     */
    public void saveFile() {
        String start = "<S>";
        String stop = "</S>";
        PrintStream out = null;
        try {
            out = new PrintStream(openFileOutput(SaveFile, MODE_PRIVATE));
            //all the socres in the table have 6 line, 4 with data, a start and a stop line
            for (Score score : highScores.getScores()) {
                out.println(start);
                out.println(score.getPoints());
                out.println(score.getName());
                out.println(score.getWordLength());
                out.println(score.getGameType());
                out.println(stop);
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * All the highscores are placed in tablerows and are added to the table
     * @param evil  Boolean if the evil or the good highscores need to be shown
     */
    private void fillTable(boolean evil) {
        scoreTable.removeAllViews();

        //the first table row is filled with the column titles
        TableRow titles = new TableRow(this);
        titles.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));

        //add the name to the row
        TextView n = new TextView(this);
        n.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
        n.setText("NAME: ");
        titles.addView(n);
        //add the word length to the row
        TextView l = new TextView(this);
        l.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
        l.setText("WORD LENGTH: ");
        titles.addView(l);
        //add the points to the row
        TextView p = new TextView(this);
        p.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
        p.setText("POINTS: ");
        titles.addView(p);

        //add the finished row to the table
        scoreTable.addView(titles);

        ArrayList<Score> scores;
        //test if the user wants to see the evil or the good highscores
        if(evil) {
            scores = highScores.getEvilScores();
        } else {
            scores = highScores.getGoodScores();
        }

        //add rows for the top 10 scores to the table
        for(Score score : scores) {
            TableRow topScore = new TableRow(this);
            topScore.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
            //add the name to the row
            TextView name = new TextView(this);
            name.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
            name.setText(score.getName());
            topScore.addView(name);
            //add the word length to the row
            TextView length = new TextView(this);
            length.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
            length.setText(String.valueOf(score.getWordLength()));
            topScore.addView(length);
            //add the points to the row
            TextView points = new TextView(this);
            points.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));
            points.setText(String.valueOf(score.getPoints()));
            topScore.addView(points);
            //add the finished row to the table
            scoreTable.addView(topScore);
        }
    }

    /**
     * tests if the given score is a new highscore
     * @param points    The new number of points achieved
     * @param type      The gametype the score was achieved on
     * @return          If it is a new highscore or not
     */
    private boolean newHighScore(int points, String type) {
        ArrayList<Score> test;
        //test of which type this highscore is
        if(type.equals("evil")) {
            test = highScores.getEvilScores();
        } else {
            test = highScores.getGoodScores();
        }
        //if the score is larger then the lowest score in the list, it is a new highscore
        if(test.get(test.size()-1).getPoints() <= points) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score, menu);
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
            finish();
        } else if(id == R.id.action_newgame) {
            Intent intent = new Intent(this,GameActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

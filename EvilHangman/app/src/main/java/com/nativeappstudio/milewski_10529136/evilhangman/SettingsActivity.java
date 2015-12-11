package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    /** the views for the seekbars*/
    private SeekBar lengthPick, guessPick;

    /** True if the gametype is evil*/
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
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lengthPick = (SeekBar) findViewById(R.id.length_picker);
        guessPick = (SeekBar) findViewById(R.id.guesses_picker);

        preferences = getSharedPreferences(prefSettings, Context.MODE_PRIVATE);

        setSettingsValues();
    }

    /**
     * When pausing this activity, the values are stored
     */
    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(prefType, evilType);
        editor.putInt(prefLength, lengthPick.getProgress()+1);
        editor.putInt(prefGuess, guessPick.getProgress()+1);
        editor.commit();
        super.onPause();
    }

    /**
     * All the values and ranges for the controllers are set. Also the listeners
     * for changing the controllers are set here.
     */
    private void setSettingsValues() {
        final TextView stateSwitch = (TextView) findViewById(R.id.setSwitchState);
        final TextView lengthVal = (TextView) findViewById(R.id.length_num);
        final TextView guessVal = (TextView) findViewById(R.id.guesses_num);

        //Everything for the length of the word setting
        lengthPick.setMax(HangmanLexicon.getLongest()-1);
        lengthPick.setProgress(preferences.getInt(prefLength, 5)-1);
        lengthPick.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = progress+1;
                lengthVal.setText(""+val);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        int val = lengthPick.getProgress()+1;
        lengthVal.setText(""+val);

        //Every thing for the number of guesses setting
        guessPick.setMax(25);
        guessPick.setProgress(preferences.getInt(prefGuess, 8)-1);
        guessPick.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = progress+1;
                guessVal.setText(""+val);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        val = guessPick.getProgress()+1;
        guessVal.setText(""+val);

        //Everything for the gametype setting
        evilType = preferences.getBoolean(prefType, true);
        Switch GoodEvil = (Switch) findViewById(R.id.scoreType);
        GoodEvil.setChecked(evilType);
        GoodEvil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stateSwitch.setText("Evil");
                    evilType = true;
                } else {
                    stateSwitch.setText("Good");
                    evilType = false;
                }
            }
        });
        if(GoodEvil.isChecked()){
            stateSwitch.setText("Evil");
        } else {
            stateSwitch.setText("Good");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_highscores) {
            Intent intent = new Intent(this,HighscoreActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.action_newgame) {
            Intent intent = new Intent(this,GameActivity.class);
            intent.putExtra("new",true);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

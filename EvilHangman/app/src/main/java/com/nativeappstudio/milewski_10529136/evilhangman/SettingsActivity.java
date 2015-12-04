package com.nativeappstudio.milewski_10529136.evilhangman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {

    private NumberPicker lengthPick, guessPick;
    private boolean evilType;

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

        lengthPick = (NumberPicker) findViewById(R.id.length_picker);
        guessPick = (NumberPicker) findViewById(R.id.guesses_picker);

        preferences = getSharedPreferences(prefSettings, Context.MODE_PRIVATE);

        setSettingsValues();
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(prefType, evilType);
        editor.putInt(prefLength, lengthPick.getValue());
        editor.putInt(prefGuess, guessPick.getValue());
        editor.commit();
        super.onPause();
    }

    private void setSettingsValues() {
        lengthPick.setMinValue(1);
        lengthPick.setMaxValue(HangmanLexicon.getLongest());
        lengthPick.setValue(preferences.getInt(prefLength, 5));

        guessPick.setMinValue(1);
        guessPick.setMaxValue(26);
        guessPick.setValue(preferences.getInt(prefGuess, 8));

        evilType = preferences.getBoolean(prefType, true);
        if(evilType){
            RadioButton radio = (RadioButton) findViewById(R.id.radioEvil);
            radio.setChecked(true);
        } else {
            RadioButton radio = (RadioButton) findViewById(R.id.radioGood);
            radio.setChecked(true);
        }
    }

    public void onRadioButtonClicked(View view) {
        // If the button now checked
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioEvil:
                if (checked) {
                    evilType = true;
                    break;
                }
            case R.id.radioGood:
                if (checked) {
                    evilType = false;
                    break;
                }
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
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

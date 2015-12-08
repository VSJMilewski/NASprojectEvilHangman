package com.nativeappstudio.milewski_10529136.evilhangman;

import java.util.ArrayList;

/**
 * Created by victor on 27-11-2015.
 * Here are the top 10 highscores for each game type stored in an ordered
 * array from highest to lowest
 */
public class HighScores {

    /**Ther array where all the highscores for gametype evil are stored*/
    private ArrayList<Score> evilScores;

    /**Ther array where all the highscores for gametype good are stored*/
    private ArrayList<Score> goodScores;

    /**The arrays are created and are filled with empty scores so it won't
     * be empty
     */
    public HighScores() {
        evilScores = new ArrayList<>();
        goodScores = new ArrayList<>();
        fillScores();
    }

    /**
     * Both array lists are filled with 10 empty scores
     */
    private void fillScores() {
        for(int i = 0; i < 10; i++){
            Score scoreG = new Score("good");
            goodScores.add(scoreG);
            Score scoreE = new Score("evil");
            evilScores.add(scoreE);
        }
    }

    /**
     * All the saved highscores are removed from the arraylists
     */
    public void clear() {
        evilScores.clear();
        goodScores.clear();
    }

    /**
     * The good and the evil highscores are placed in a single arraylist and is returned
     * @return An arraylist with both the good and the evil highscores
     */
    public ArrayList<Score> getScores() {
        ArrayList<Score> tmp = new ArrayList<>();
        tmp.addAll(evilScores);
        tmp.addAll(goodScores);
        return tmp;
    }

    /**
     * @return  the arraylist with evil highscores
     */
    public ArrayList<Score> getEvilScores() {
        return evilScores;
    }

    /**
     * @return  the arraylist with good highscores
     */
    public ArrayList<Score> getGoodScores() {
        return goodScores;
    }

    /**
     * Tests if the new score needs to be added to the
     * evil or the good highscores
     * @param score     The new highscore to be added
     */
    public void newScore(Score score) {
        if(score.getGameType().equals("evil")) {
            newEvilScore(score);
        } else {
            newGoodScore(score);
        }
    }

    /**
     * Tests if the new highscores is good or evil and immediatly adds it to the
     * corresponding array
     * @param score The score that is read in from a file and can be stored straight away
     */
    public void newScoreRead(Score score) {
        if(score.getGameType().equals("evil")) {
            evilScores.add(score);
        } else {
            goodScores.add(score);
        }
    }

    /**
     * Tests at which location the score needs to be added and adds it.
     * The scores below it are moved one location down
     * @param score     The new evil highscore to be added
     */
    private void newEvilScore(Score score) {
        int points = score.getPoints();
        ArrayList<Score> tmpScores = new ArrayList<>();

        //compare with all the scores from top to bottom
        for(int i = 0; i < evilScores.size() && i < 10; i++) {
            Score sc = evilScores.get(i);
            /*If it is higher save it at this spot, and copy
            the previous score at this spot to move it down*/
            if(points >= sc.getPoints()) {
                Score tmp = new Score(score);
                tmpScores.add(tmp);
                points = sc.getPoints();
                score = sc;
            //if it is not higher, the original score at this spot remains at this spot
            } else {
                tmpScores.add(sc);
            }
        }
        evilScores = tmpScores;
    }

    /**
     * Tests at which location the score needs to be added and adds it.
     * The scores below it are moved one location down
     * @param score     The new good highscore to be added
     */
    private void newGoodScore(Score score) {
        int points = score.getPoints();
        ArrayList<Score> tmpScores = new ArrayList<>();

        //compare with all the scores from top to bottom
        for(int i = 0; i < goodScores.size() && i < 10; i++) {
            Score sc = goodScores.get(i);
            /*If it is higher save it at this spot, and copy
            the previous score at this spot to move it down*/
            if(sc.getPoints() <= points) {
                Score tmp = new Score(score);
                tmpScores.add(tmp);
                points = sc.getPoints();
                score = sc;
            //if it is not higher, the original score at this spot remains at this spot
            } else {
                tmpScores.add(sc);
            }
        }
        goodScores = tmpScores;
    }
}

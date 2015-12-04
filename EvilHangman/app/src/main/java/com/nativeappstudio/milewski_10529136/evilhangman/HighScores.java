package com.nativeappstudio.milewski_10529136.evilhangman;

import java.util.ArrayList;

/**
 * Created by victo on 27-11-2015.
 */
public class HighScores {

    private ArrayList<Score> evilScores;
    private ArrayList<Score> goodScores;

    public HighScores() {
        evilScores = new ArrayList<>();
        goodScores = new ArrayList<>();
        fillScores();
    }

    private void fillScores() {
        for(int i = 0; i < 10; i++){
            Score scoreG = new Score("good");
            goodScores.add(scoreG);
            Score scoreE = new Score("evil");
            evilScores.add(scoreE);
        }
    }

    public void clear() {
        evilScores.clear();
        goodScores.clear();
    }

    public ArrayList<Score> getScores() {
        ArrayList<Score> tmp = new ArrayList<>();
        tmp.addAll(evilScores);
        tmp.addAll(goodScores);
        return tmp;
    }

    public ArrayList<Score> getEvilScores() {
        return evilScores;
    }

    public ArrayList<Score> getGoodScores() {
        return goodScores;
    }

    public void newScore(Score score) {
        if(score.getGameType().equals("evil")) {
            newEvilScore(score);
        } else {
            newGoodScore(score);
        }
    }

    public void newScoreRead(Score score) {
        if(score.getGameType().equals("evil")) {
            evilScores.add(score);
        } else {
            goodScores.add(score);
        }
    }

    private void newEvilScore(Score score) {
        int points = score.getPoints();
        ArrayList<Score> tmpScores = new ArrayList<>();
        for(int i = 0; i < evilScores.size() && i < 10; i++) {
            Score sc = evilScores.get(i);
            if(points >= sc.getPoints()) {
                Score tmp = new Score(score);
                tmpScores.add(tmp);
                points = sc.getPoints();
                score = sc;
            } else {
                tmpScores.add(sc);
            }
        }
        evilScores = tmpScores;
    }

    private void newGoodScore(Score score) {
        int points = score.getPoints();
        ArrayList<Score> tmpScores = new ArrayList<>();
        for(int i = 0; i < goodScores.size() && i < 10; i++) {
            Score sc = goodScores.get(i);
            if(sc.getPoints() <= points) {
                Score tmp = new Score(score);
                tmpScores.add(tmp);
                points = sc.getPoints();
                score = sc;
            } else {
                tmpScores.add(sc);
            }
        }
        goodScores = tmpScores;
    }
}

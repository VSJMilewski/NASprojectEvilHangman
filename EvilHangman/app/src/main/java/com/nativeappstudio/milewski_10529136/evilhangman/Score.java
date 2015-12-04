package com.nativeappstudio.milewski_10529136.evilhangman;

/**
 * Created by victo on 27-11-2015.
 */
public class Score {
    private int points;
    private String name;
    private int wordLength;
    private String gameType;

    public Score() {
        points = 0;
        name = "<No Name>";
        wordLength = 0;
        gameType = "evil";
    }

    public Score(String type) {
        points = 0;
        name = "<No Name>";
        wordLength = 0;
        gameType = type;
    }

    public Score(int points) {
        this.points = points;
        name = "<No Name>";
        wordLength = 0;
        gameType = "evil";
    }

    public Score(int points, String name) {
        this.points = points;
        this.name = name;
        wordLength = 0;
        gameType = "evil";
    }

    public Score(int points, String name, int length, String type) {
        this.points = points;
        if(name == null || name.isEmpty()) {
            name = "<No Name>";
        }
        this.name = name;
        wordLength = length;
        gameType = type;
    }

    public Score(Score sc) {
        this.points = sc.getPoints();
        this.name = sc.name;
        wordLength = sc.getWordLength();
        gameType = sc.getGameType();
    }

    public int getPoints() {
        return points;
    }

    public int getWordLength() {
        return wordLength;
    }

    public String getName() {
        return name;
    }

    public String getGameType() {
        return gameType;
    }
}

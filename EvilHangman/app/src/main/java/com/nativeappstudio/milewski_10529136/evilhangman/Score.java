package com.nativeappstudio.milewski_10529136.evilhangman;

/**
 * Created by victor on 27-11-2015.
 * Here are all the variables stored for a single score.
 */
public class Score {
    /**The number of points won in the game*/
    private int points;

    /**The name of the player who achieved this score*/
    private String name;

    /**The length of the words for the game where the score was achieved on*/
    private int wordLength;

    /**The game type for where the score was achieved on*/
    private String gameType;

    public Score(String type) {
        points = 0;
        name = "<No Name>";
        wordLength = 0;
        gameType = type;
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

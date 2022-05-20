package com.golan.amit.xmdrix;

public class XMDrixHelper {

    public static final int SQUARE = 3;
    public static final String PLAYS = "XO";

    private int play_indicator;
    private int game_rounds;

    public XMDrixHelper() {
        this.play_indicator = 0;
        this.game_rounds = 0;
    }

    /**
     * Getters & Setters
     * @return
     */

    public int getPlay_indicator() {
        return play_indicator;
    }

    public void setPlay_indicator(int play_indicator) {
        this.play_indicator = play_indicator;
    }

    public void increasePlay_indicator() {
        this.play_indicator++;
    }

    public int getTruePlayIndicator () {
        return this.play_indicator % 2;
    }

    public String getCurrentPlayString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PLAYS.charAt(getTruePlayIndicator()));
        return sb.toString();
    }

    public int getGame_rounds() {
        return game_rounds;
    }

    public void setGame_rounds(int game_rounds) {
        this.game_rounds = game_rounds;
    }

    public void resetGame_rounds() {
        this.game_rounds = 0;
    }

    public void increaseGame_rounds() {
        this.game_rounds++;
    }
}

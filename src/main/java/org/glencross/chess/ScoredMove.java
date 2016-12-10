package org.glencross.chess;

/**
 * Created by Chris on 22/12/2014.
 */
public class ScoredMove {

    private Move move;

    private int score;

    public ScoredMove(Move move, int score) {
        this.move = move;
        this.score = score;
    }

    public Move getMove() {
        return move;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        if (score == 0) {
            return move + " (score=drawn)";
        } else if (score > 0) {
            return move + " (score=" + score + " to white)";
        } else {
            return move + " (score=" + (0 - score) + " to black)";
        }
    }
}

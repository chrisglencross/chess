package org.glencross.chess;

/**
 * Created by Chris on 22/12/2014.
 */
public class Move {

    private Piece piece;
    private Square source;
    private Square target;

    public Move(Piece piece, Square source, Square target) {
        this.piece = piece;
        this.source = source;
        this.target = target;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getTarget() {
        return target;
    }

    public Square getSource() {
        return source;
    }

    public String toString() {
        return "Move " + piece + " from " + source + " to " + target;
    }
}

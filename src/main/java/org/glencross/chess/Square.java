package org.glencross.chess;

import java.util.Optional;

/**
 * Created by Chris on 21/12/2014.
 */
public class Square {

    private final int x;
    private final int y;
    private final boolean black;

    private boolean hasMoved;
    private Optional<Piece> piece;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        this.black = ((x + y) % 2) == 0;
        this.piece = Optional.empty();
        this.hasMoved = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isBlack() {
        return black;
    }

    public Optional<Piece> getPiece() {
        return piece;
    }

    void setPiece(Piece piece) {
        if (this.piece.isPresent() && piece.isBlack() == this.piece.get().isBlack()) {
            throw new IllegalStateException("Square " + this + " already contains piece " + this.piece.get() + " so cannot move " + piece + " there");
        };
        this.piece = Optional.ofNullable(piece);
    }

    void removePiece(Piece piece) {
        if (this.piece.orElseThrow(() -> new IllegalStateException("No piece at " + this + " when trying to remove " + piece)) != piece) {
            throw new IllegalStateException("Wrong piece " + this.piece.get() + " found at " + this + " when trying to remove " + piece);
        };
        this.piece = Optional.empty();
        this.hasMoved = true;
    }

    @Override
    public String toString() {
        return "square " + (char)('a' + x)  + (char)('1' + y);
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public Square clone() {
        Square square = new Square(x, y);
        square.piece = piece;
        return square;
    }


}

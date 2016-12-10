package org.glencross.chess;

import pi.Block;
import pi.Color;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public abstract class Piece {

    private final String name;

    private final int startX;
    private final int startY;

    private final int points;
    private final boolean black;
    private final char character;

    private final List<Block> blocks;

    protected static Block woolBlock(boolean black) {
        return Block.wool(black ? Color.BLACK : Color.WHITE);
    }

    public Piece(String name, char c, int startX, int startY, boolean black, int points, List<Block> blocks) {
        this.name = name;
        this.character = c;
        this.startX = startX;
        this.startY = startY;
        this.black = black;
        this.points = points;
        this.blocks = blocks;
    }

    public String getName() {
        return name;
    }

    public boolean isBlack() {
        return black;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getPoints() {
        return points;
    }

    public char getCharacter() {
        return character;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public abstract Stream<Square> getTargetSquares(Square square, Board board);

    /**
     * Method which can be used by subclasses to get a list of squares when moving an indefinite
     * distance in a straight line (rook, bishop, queen).
     * @param list
     * @param square
     * @param board
     * @param x
     * @param y
     */
    protected void addTargetSquaresInLine(List<Square> list, Square square, Board board, int x, int y) {
        while (true) {
            Optional<Square> nextSquare = board.getRelativeSquare(square, x, y);
            if (!nextSquare.isPresent()) {
                break; // Hit the edge of the board
            }
            Optional<Piece> piece = nextSquare.get().getPiece();
            if (piece.isPresent() && piece.get().isBlack() == this.isBlack()) {
                break; // Hit one of our own pieces
            }
            square = nextSquare.get();
            list.add(square);
            if (piece.isPresent()) {
                break; // Taking another piece - can't hop over it
            }

        }
    }

    @Override
    public String toString() {
        return (isBlack() ? "Black " : "White ") + getName();
    }

}

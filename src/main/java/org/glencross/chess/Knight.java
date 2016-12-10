package org.glencross.chess;

import pi.Block;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public class Knight extends Piece {

    public Knight(int startX, int startY, boolean black) {
        super("knight", 'k', startX, startY, black, 3,
                Arrays.asList(woolBlock(black), Block.GLOWING_OBSIDIAN));
    }

    @Override
    public Stream<Square> getTargetSquares(Square currentSquare, Board board) {
        return Stream.of(
                board.getRelativeSquare(currentSquare, 2, 1),
                board.getRelativeSquare(currentSquare, 2, -1),
                board.getRelativeSquare(currentSquare, 1, 2),
                board.getRelativeSquare(currentSquare, 1, -2),
                board.getRelativeSquare(currentSquare, -1, -2),
                board.getRelativeSquare(currentSquare, -1, 2),
                board.getRelativeSquare(currentSquare, -2, 1),
                board.getRelativeSquare(currentSquare, -2, -1))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(square -> !square.getPiece().isPresent() || square.getPiece().get().isBlack() != this.isBlack());

    }
}

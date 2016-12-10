package org.glencross.chess;

import pi.Block;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public class King extends Piece {

    public King(int startX, int startY, boolean black) {
        super("king", 'K', startX, startY, black, Integer.MAX_VALUE,
                Arrays.asList(woolBlock(black), woolBlock(black), Block.GOLD_BLOCK));
    }

    @Override
    public Stream<Square> getTargetSquares(Square currentSquare, Board board) {

        // Castling moves
        Optional<Square> kingSideCastle = Optional.empty();
        Optional<Square> queenSideCastle = Optional.empty();
        if (isKingEligibleToCastle(currentSquare, board)) {
            Square kingRookSquare = board.getSquare(7, currentSquare.getY());
            Square queenRookSquare = board.getSquare(0, currentSquare.getY());
            if (!kingRookSquare.hasMoved() &&
                    kingRookSquare.getPiece().orElse(null) instanceof Rook &&
                    IntStream.rangeClosed(5, 6).
                            allMatch(x -> !board.getSquare(x, currentSquare.getY()).getPiece().isPresent())) {
                kingSideCastle = Optional.of(board.getSquare(6, currentSquare.getY()));
            }
            if (!queenRookSquare.hasMoved() &&
                    queenRookSquare.getPiece().orElse(null) instanceof Rook &&
                    IntStream.rangeClosed(1, 3).
                            allMatch(x -> !board.getSquare(x, currentSquare.getY()).getPiece().isPresent())) {
                queenSideCastle = Optional.of(board.getSquare(2, currentSquare.getY()));
            }
        }

        return Stream.of(
                board.getRelativeSquare(currentSquare, 0, 1),
                board.getRelativeSquare(currentSquare, 0, -1),
                board.getRelativeSquare(currentSquare, 1, 0),
                board.getRelativeSquare(currentSquare, -1, 0),
                board.getRelativeSquare(currentSquare, 1, 1),
                board.getRelativeSquare(currentSquare, 1, -1),
                board.getRelativeSquare(currentSquare, -1, 1),
                board.getRelativeSquare(currentSquare, -1, -1),
                kingSideCastle,
                queenSideCastle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(square -> !square.getPiece().isPresent() || square.getPiece().get().isBlack() != this.isBlack());
    }

    private boolean isKingEligibleToCastle(Square currentSquare, Board board) {
        return getStartX() == currentSquare.getX() && getStartY() == currentSquare.getY() &&
                !currentSquare.hasMoved() && !board.isPlayerInCheck(this.isBlack());
    }
}

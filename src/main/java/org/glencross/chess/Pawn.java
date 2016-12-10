package org.glencross.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public class Pawn extends Piece {

    public Pawn(int startX, int startY, boolean black) {
        super("pawn", 'p', startX, startY, black, 1,
                Arrays.asList(woolBlock(black)));
    }

    @Override
    public Stream<Square> getTargetSquares(Square currentSquare, Board board) {

        List<Optional<Square>> list = new ArrayList<>();

        int forwardY = isBlack() ? -1 : 1;

        // Can move forward if there's nothing there
        list.add(board.getRelativeSquareIf(currentSquare, 0, forwardY, piece -> !piece.isPresent()));

        // Can also move forward two places if on starting row and there's nothing there
        if (currentSquare.getY() == getStartY() && !list.isEmpty()) {
            list.add(board.getRelativeSquareIf(currentSquare, 0, 2 * forwardY, piece -> !piece.isPresent()));
        }

        // Can move diagonally left or right if there's a piece to take
        list.add(board.getRelativeSquareIf(currentSquare, -1, forwardY,
                piece -> piece.isPresent() && piece.get().isBlack() != this.isBlack()));
        list.add(board.getRelativeSquareIf(currentSquare, 1, forwardY,
                piece -> piece.isPresent() && piece.get().isBlack() != this.isBlack()));

        // TODO En-passent

        return list.stream().filter(Optional::isPresent).map(Optional::get);
    }
}

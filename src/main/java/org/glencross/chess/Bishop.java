package org.glencross.chess;

import pi.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public class Bishop extends Piece {

    public Bishop(int startX, int startY, boolean black) {
        super("bishop", 'B', startX, startY, black, 3,
                Arrays.asList(woolBlock(black), Block.BOOKSHELF));
    }

    @Override
    public Stream<Square> getTargetSquares(Square square, Board board) {
        List<Square> result = new ArrayList<>();
        addTargetSquaresInLine(result, square, board, 1, 1);
        addTargetSquaresInLine(result, square, board, 1, -1);
        addTargetSquaresInLine(result, square, board, -1, 1);
        addTargetSquaresInLine(result, square, board, -1, -1);
        return result.stream();
    }
}

package org.glencross.chess;

import pi.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public class Queen extends Piece {

    public Queen(int startX, int startY, boolean black) {
        super("queen", 'Q', startX, startY, black, 9,
                Arrays.asList(woolBlock(black), woolBlock(black), Block.NETHER_REACTOR_CORE));
    }

    @Override
    public Stream<Square> getTargetSquares(Square square, Board board) {
        List<Square> result = new ArrayList<>();
        addTargetSquaresInLine(result, square, board, 0, 1);
        addTargetSquaresInLine(result, square, board, 1, 0);
        addTargetSquaresInLine(result, square, board, 0, -1);
        addTargetSquaresInLine(result, square, board, -1, 0);
        addTargetSquaresInLine(result, square, board, 1, 1);
        addTargetSquaresInLine(result, square, board, 1, -1);
        addTargetSquaresInLine(result, square, board, -1, 1);
        addTargetSquaresInLine(result, square, board, -1, -1);
        return result.stream();
    }

}

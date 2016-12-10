package org.glencross.chess;

import pi.Block;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public class Rook extends Piece {

    public Rook(int startX, int startY, boolean black) {
        super("rook", 'R', startX, startY, black, 5,
                Arrays.asList(woolBlock(black), Block.STONE_BRICK));
    }

    @Override
    public Stream<Square> getTargetSquares(Square square, Board board) {
        List<Square> result = new ArrayList<>();
        addTargetSquaresInLine(result, square, board, 0, 1);
        addTargetSquaresInLine(result, square, board, 1, 0);
        addTargetSquaresInLine(result, square, board, 0, -1);
        addTargetSquaresInLine(result, square, board, -1, 0);
        return result.stream();
    }

}

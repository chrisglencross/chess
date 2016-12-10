package org.glencross.chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Chris on 21/12/2014.
 */
public class Pieces {

    public static Collection<Piece> getStartingPieces() {
        List<Piece> pieces = new ArrayList<>();

        pieces.add(new Rook(0, 0, false));
        pieces.add(new Knight(1, 0, false));
        pieces.add(new Bishop(2, 0, false));
        pieces.add(new Queen(3, 0, false));
        pieces.add(new King(4, 0, false));
        pieces.add(new Bishop(5, 0, false));
        pieces.add(new Knight(6, 0, false));
        pieces.add(new Rook(7, 0, false));
        for (int x = 0; x < 8; x++) {
            pieces.add(new Pawn(x, 1, false));
        }

        pieces.add(new Rook(0, 7, true));
        pieces.add(new Knight(1, 7, true));
        pieces.add(new Bishop(2, 7, true));
        pieces.add(new Queen(3, 7, true));
        pieces.add(new King(4, 7, true));
        pieces.add(new Bishop(5, 7, true));
        pieces.add(new Knight(6, 7, true));
        pieces.add(new Rook(7, 7, true));
        for (int x = 0; x < 8; x++) {
            pieces.add(new Pawn(x, 6, true));
        }

        return pieces;
    }

}

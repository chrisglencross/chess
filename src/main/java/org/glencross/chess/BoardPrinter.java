package org.glencross.chess;

import java.util.Optional;

/**
 * Created by Chris on 21/12/2014.
 */
public class BoardPrinter {

    public void print(Board board) {
        int xCellSize = 5;
        int yCellSize = 2;
        System.out.print(' ');
        for (int i = 0; i < 8; i++) {
            System.out.print("  " + (char)('a'+i) + "  ");
        }
        System.out.println();
        for (int j = 0; j <= yCellSize*8; j++) {
            int y = 7 - (j / yCellSize);
            int yOffset = j % yCellSize;
            for (int i = 0; i <= xCellSize*8; i++) {
                int x = i / xCellSize;
                int xOffset = i % xCellSize;
                if (i == 0) {
                    System.out.print(yOffset == 1 ? (char)(y+'1') : ' ');
                }
                if (xOffset == 0 && yOffset == 0) {
                    System.out.print("+");
                } else if (yOffset == 0) {
                    System.out.print("-");
                } else if (xOffset == 0) {
                    System.out.print("|");
                } else {
                    Square square = board.getSquare(x, y);
                    Optional<Piece> piece = board.getSquare(x, y).getPiece();
                    if (xOffset == 2 && yOffset == 1 && piece.isPresent()) {
                        System.out.print(piece.get().isBlack() ? "b" : "w");
                    } else if (xOffset == 3 && yOffset == 1 && piece.isPresent()) {
                        System.out.print(piece.get().getCharacter());
                    } else if (square.isBlack()) {
                        System.out.print(".");
                    } else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }

}

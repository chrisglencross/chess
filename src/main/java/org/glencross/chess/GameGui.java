package org.glencross.chess;

import java.io.IOException;

/**
 * Created by Chris on 09/04/2015.
 */
public interface GameGui {
    
    void printMessage(String message);

    void displayMove(Move move, Board board, int score);

    Move getPlayerMove(ChessGame chessGame, boolean blackMove) throws IOException;

    void printBoard(Board board);
}

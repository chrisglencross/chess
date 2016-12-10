package org.glencross.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
* Created by Chris on 09/04/2015.
*/
class CommandLineGui implements GameGui {

    private final BoardPrinter printer;

    public CommandLineGui(BoardPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void printBoard(Board board) {
        printer.print(board);
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayMove(Move move, Board board, int score) {
        System.out.println();
        printMessage(move + " (score=" + score + ")");
        printer.print(board);
    }

    @Override
    public Move getPlayerMove(ChessGame game, boolean blackMove) throws IOException {
        System.out.println("Move from-to (e.g. 'b1-b3'): ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = in.readLine();
            if (line.equalsIgnoreCase("U")) {
                game.undo(2);
                continue;
            }
            String[] parts = line.toUpperCase().split("[-, ]+");
            if (parts.length == 2 && parts[0].matches("[A-H][1-8]") && parts[1].matches("[A-H][1-8]")) {
                int fromX = parts[0].charAt(0) - 'A';
                int fromY = parts[0].charAt(1) - '1';
                int toX = parts[1].charAt(0) - 'A';
                int toY = parts[1].charAt(1) - '1';
                Move move = game.newMove(fromX, fromY, toX, toY);
                Optional<String> validationError = game.getValidationError(blackMove, move);
                if (!validationError.isPresent()) {
                    return move;
                } else {
                    System.out.println("Bad move: " + validationError.get());
                }
            } else {
                System.out.println("Invalid command: " + line);
            }
        }
    }

}

package org.glencross.chess;

import java.io.IOException;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public class ChessGame {

    private final GameGui gui;
    private Board board;
    private Deque<Board> undoBuffer = new ArrayDeque<>();

    public ChessGame(GameGui gui) {
        this.gui = gui;
        board = new Board();
        for (Piece piece : Pieces.getStartingPieces()) {
            Square startSquare = board.getSquare(piece.getStartX(), piece.getStartY());
            board.addToBoard(piece, startSquare);
        }
    }

    public Board getBoard() {
        return board;
    }

    public static void main(String[] args) throws IOException {

        BoardPrinter printer = new BoardPrinter();
        GameGui gui = new CommandLineGui(printer);
        ChessGame game = new ChessGame(gui);
        game.playGame();

    }

    public Move newMove(int fromX, int fromY, int toX, int toY) {
        Square from = board.getSquare(fromX, fromY);
        Square to = board.getSquare(toX, toY);
        return newMove(from, to);
    }

    public Move newMove(Square from, Square to) {
        return new Move(from.getPiece().orElse(null), from, to);
    }

    public void playGame() throws IOException {

        gui.printBoard(board);

        boolean whiteComputer = true;
        boolean blackComputer = false;

        boolean blackMove = false;
        while (true) {
            boolean check = board.isPlayerInCheck(blackMove);
            if (!isMovePossible(blackMove)) {
                if (check) {
                    gui.printMessage("Checkmate! " + (blackMove ? "White" : "Black") + " wins!");
                } else {
                    gui.printMessage("Stalemate! It's a draw.");
                }
                break;
            }
            if (check) {
                gui.printMessage("Check!");
            }

            Move move;
            if ((blackMove && blackComputer) || (!blackMove && whiteComputer)) {
                move = getComputerMove(blackMove);
            }  else {
                move = gui.getPlayerMove(this, blackMove);
            }
            move(move);

            blackMove = !blackMove;
        }
    }


    private boolean isMovePossible(boolean blackMove) {
        return selectMove(board, blackMove, 0).isPresent();
    }

    private ScoredMove selectMove(boolean blackMove) {
        return selectMove(board, blackMove, 1).get();
    }

    private Move getComputerMove(boolean black) {
        ScoredMove scoredMove = selectMove(black);
        return scoredMove.getMove();
    }

    public Optional<String> getValidationError(boolean black, Move move) {
        Optional<Piece> piece = move.getSource().getPiece();
        if (!piece.isPresent()) {
            return Optional.of("No piece in source position " + move.getSource());
        }
        if (!piece.get().isBlack() == black) {
            return Optional.of(piece.get() + " at " + move.getSource() + " is not your piece");
        }
        Stream<Square> targetSquares = piece.get().getTargetSquares(move.getSource(), board);
        if (!targetSquares.anyMatch(possibleTarget -> possibleTarget == move.getTarget())) {
            return Optional.of(piece.get() + " cannot move from " + move.getSource() + " to " + move.getTarget());
        }
        Board boardCopy = board.clone().move(new Move(piece.get(), move.getSource(), move.getTarget()));
        if (boardCopy.isPlayerInCheck(black)) {
            if (board.isPlayerInCheck(black)) {
                return Optional.of("You must move out of check");
            } else {
                return Optional.of("You cannot move into check");
            }
        }
        return Optional.empty();

    }

    private void move(Move move) {
        undoBuffer.add(board.clone());
        board.move(move);

        int score = calculateScore(board);
        gui.displayMove(move, board, score);
    }

    public void undo(int count) {
        for (int i = 0; i < count; i++) {
            if (!undoBuffer.isEmpty()) {
                board = undoBuffer.removeLast();
            }
        }
        gui.printBoard(board);
    }


    // Minimax algorithm
    private static Optional<ScoredMove> selectMove(Board board, boolean blackMove, int remainingDepth) {

        Comparator<Integer> minOrMax = blackMove ? Comparator.reverseOrder() : Comparator.naturalOrder();

        List<Move> possibleMoves = board.getPossibleMoves(blackMove).collect(Collectors.toList());
        Collections.shuffle(possibleMoves);

        return possibleMoves.stream()
                .map(move -> new ScoredMove(move, calculateScoreAfterMove(board.clone().move(move), blackMove, remainingDepth)))
                .max((move1, move2) -> minOrMax.compare(move1.getScore(), move2.getScore()));
    }

    private static int calculateScoreAfterMove(Board boardCopy, boolean blackMove, int remainingDepth) {

        if (remainingDepth == 0) {
            return calculateScore(boardCopy);
        }

        // Minimax: Recurse to find the score after the next player's best move
        Optional<ScoredMove> nextPlayerBestMove = selectMove(boardCopy, !blackMove, remainingDepth -1);
        if (nextPlayerBestMove.isPresent()) {
            return nextPlayerBestMove.get().getScore();
        }

        // Other player cannot move. It's either stalemate or checkmate.
        if (boardCopy.isPlayerInCheck(!blackMove)) {
            return blackMove ? Integer.MIN_VALUE : Integer.MAX_VALUE; // Checkmate
        } else {
            return 0; // Stalemate
        }

    }

    private static int calculateScore(Board board) {

        if (board.isPlayerInCheckmate(false)) {
            return Integer.MIN_VALUE; // White is checkmated
        }
        if (board.isPlayerInCheckmate(true)) {
            return Integer.MAX_VALUE; // Black is checkmated
        }

        int whitePoints = 1000 * board.getAllPieces(false).stream().mapToInt(Piece::getPoints).sum();
        int blackPoints = 1000 * board.getAllPieces(true).stream().mapToInt(Piece::getPoints).sum();

        // Add minor points for pieces moving forward
        whitePoints += board.getAllSquares()
                .filter(square -> square.getPiece().isPresent())
                .filter(square -> !square.getPiece().get().isBlack())
                .mapToInt(Square::getY)
                .sum();

        blackPoints += board.getAllSquares()
                .filter(square -> square.getPiece().isPresent())
                .filter(square -> square.getPiece().get().isBlack())
                .mapToInt(square -> 7-square.getY())
                .sum();

        return whitePoints - blackPoints;
    }

}

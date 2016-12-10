package org.glencross.chess;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by Chris on 21/12/2014.
 */
public class Board {

    private final Square[][] squares = new Square[8][8];

    public Board() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares[x][y] = new Square(x, y);
            }
        }
    }

    public Square getSquare(int x, int y) {
        return squares[x][y];
    }

    public Board move(Move move) {
        Piece piece = move.getPiece();
        Square from = move.getSource();
        Square to = move.getTarget();
        from = getSquare(from.getX(), from.getY());
        to = getSquare(to.getX(), to.getY());
        from.removePiece(piece);

        // If pawn has reached the end, replace with a queen
        if (piece instanceof Pawn && to.getY() == (piece.isBlack() ? 0 : 7)) {
            piece = new Queen(to.getX(), to.getY(), piece.isBlack());
        }

        to.setPiece(piece);

        // If king is performing a castling move, also move the rook
        // We've already checked that the move is valid at this time
        if (piece instanceof King &&
                from.getX() == piece.getStartX() &&
                from.getY() == piece.getStartY() &&
                to.getY() == piece.getStartY()) {
            if (to.getX() == 6) {
                // King side castle
                Square rookFromSquare = getSquare(7, from.getY());
                Square rookToSquare = getSquare(5, from.getY());
                Rook rook = (Rook)rookFromSquare.getPiece().get();
                move(new Move(rook, rookFromSquare, rookToSquare));
            } else if (to.getX() == 2) {
                // Queen side castle
                Square rookFromSquare = getSquare(0, from.getY());
                Square rookToSquare = getSquare(3, from.getY());
                Rook rook = (Rook)rookFromSquare.getPiece().get();
                move(new Move(rook, rookFromSquare, rookToSquare));
            }

        }


        return this;
    }

    public void addToBoard(Piece piece, Square square) {
        if (square.getPiece().isPresent()) {
            throw new IllegalStateException(square + " already contains a piece");
        }
        square.setPiece(piece);
    }

    public Board clone() {
        Board board = new Board();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board.squares[x][y] = squares[x][y].clone();
            }
        }
        return board;
    }

    public Collection<Piece> getAllPieces(boolean black) {
        List<Piece> pieces = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = squares[x][y].getPiece().orElse(null);
                if (piece != null && piece.isBlack() == black) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    public Stream<Square> getAllSquares() {
        return Arrays.stream(squares).flatMap(row -> Arrays.stream(row));
    }

    public Optional<Square> getRelativeSquare(Square currentSquare, int x, int y) {
        return getRelativeSquareIf(currentSquare, x, y, piece -> true);
    }

    public Optional<Square> getRelativeSquareIf(Square currentSquare, int x, int y, Predicate<Optional<Piece>> targetPiecePredicate) {
        int newX = currentSquare.getX() + x;
        int newY = currentSquare.getY() + y;
        if (newX < 0 || newY < 0 || newX > 7 || newY > 7) {
            return Optional.empty();
        }
        Square targetSquare = getSquare(newX, newY);
        if (!targetPiecePredicate.test(targetSquare.getPiece())) {
            return Optional.empty();
        }
        return Optional.of(targetSquare);
    }

    public boolean isPlayerInCheckmate(boolean blackPlayer) {
        // The player is in check and no possible moves
        return isPlayerInCheck(blackPlayer) && !getPossibleMoves(blackPlayer).findAny().isPresent();
    }

    public  boolean isPlayerInCheck(boolean blackPlayer) {
        return getAllSquares()
                .filter(square -> square.getPiece().isPresent())
                .filter(square -> square.getPiece().get().isBlack() != blackPlayer)
                .filter(square -> !(square.getPiece().get() instanceof King)) // king cannot check king and avoids recursion
                .flatMap(square -> square.getPiece().get().getTargetSquares(square, this))
                .filter(targetSquare -> targetSquare.getPiece().isPresent())
                .map(targetSquare -> targetSquare.getPiece().get())
                .anyMatch(targetPiece -> targetPiece.isBlack() == blackPlayer && targetPiece instanceof King);
    }

    public Stream<Move> getPossibleMoves(boolean blackMove) {
        return getAllSquares()
                .filter(square -> square.getPiece().isPresent())
                .filter(square -> square.getPiece().get().isBlack() == blackMove)
                .flatMap(square -> {
                    Piece piece = square.getPiece().get();
                    return piece.getTargetSquares(square, this).map(target -> new Move(piece, square, target));
                })
                .filter(move -> !this.clone().move(move).isPlayerInCheck(blackMove));
    }

}

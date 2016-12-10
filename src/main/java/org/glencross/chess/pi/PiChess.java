package org.glencross.chess.pi;

import org.glencross.chess.*;
import pi.Block;
import pi.Minecraft;
import pi.Vec;
import pi.event.BlockHitEvent;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Chris on 08/04/2015.
 */
public class PiChess implements GameGui {

    private final Minecraft world;

    private final Vec boardCorner;
    private final int boardDepth;
    private Square selectedSquare = null;

    public PiChess(Minecraft world) {
        this.world = world;

        world.setting("world_immutable", true);

        Minecraft.Player player = world.player;
        Vec boardCentre = player.getPosition();
        Vec boardCorner = boardCentre.add(-14, 0, 14);

        // Get the cube representing the board
        System.out.println("Calculating position for board");
        int minHeight = Integer.MAX_VALUE;
        int maxHeight = Integer.MIN_VALUE;
        for (int x = 0; x < 8*3; x++) {
            for (int y = 0; y < 8*3; y++) {
                int height = world.getHeight(boardCorner.x + x, boardCorner.z - y);
                minHeight = Math.min(height, minHeight);
                maxHeight = Math.max(height, maxHeight);
            }
        }
        // Move the board corner to the top surface
        boardCentre = boardCentre.add(0, maxHeight - boardCentre.y, 0);
        this.boardCorner = boardCorner.add(0, maxHeight - boardCorner.y, 0);
        this.boardDepth = boardCentre.y - minHeight;

        // Move the player to somewhere above the height of the board
        world.player.setPosition(boardCentre.add(0, 20, 0));

    }

    private Vec squareToVec(int x, int y, int offsetX, int offsetY, int offsetHeight) {
        return boardCorner.add(3*x + 1 + offsetX, offsetHeight, -3*y - 1 - offsetY);
    }

    private void drawBoard() {

        System.out.println("Drawing board");
        world.autoFlush(false);

        // Draw the board
        world.setBlocks(squareToVec(0, 0, -2, -2, 0), squareToVec(7, 7, 2, 2, -boardDepth), Block.GOLD_BLOCK);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++ ) {
                boolean black = ((x + y) % 2 == 0);
                Vec tileCorner1 = squareToVec(x, y, -1, -1, 0);
                Vec tileCorner2 = squareToVec(x, y, 1, 1, 0);
                world.setBlocks(tileCorner1, tileCorner2, black ? Block.WOOL.withData(15) : Block.WOOL.withData(0));
            }
        }
        world.flush();
        world.autoFlush(true);

    }

    private void drawPieces(Board board) {
        System.out.println("Drawing pieces");
        world.autoFlush(false);
        world.setBlocks(squareToVec(0, 0, -1, -1, 1), squareToVec(7, 7, 1, 1, 10), Block.AIR);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++ ) {
                Square square = board.getSquare(x, y);
                drawPieceAt(square);
            }
        }
        world.flush();
        world.autoFlush(true);
    }

    private void drawSinglePieceAt(Square square) {
        world.autoFlush(false);
        world.setBlocks(
                squareToVec(square.getX(), square.getY(), -1, -1, 1),
                squareToVec(square.getX(), square.getY(), 1, 1, 10),
                Block.AIR);
        drawPieceAt(square);
        world.flush();
        world.autoFlush(true);
    }

    private void drawPieceAt(Square square) {
        Optional<Piece> piece = square.getPiece();
        if (piece.isPresent()) {
            List<Block> blocks = piece.get().getBlocks();
            Vec pieceBase = squareToVec(square.getX(), square.getY(), 0, 0, square == selectedSquare ? 2 : 1);
            for (int i = 0; i < blocks.size(); i++) {
                world.setBlock(pieceBase.add(0, i, 0), blocks.get(i));
            }
        }
    }

    @Override
    public void printMessage(String message) {
        world.postToChat(message);
    }

    @Override
    public void displayMove(Move move, Board board, int score) {
        world.postToChat(move.toString());
        drawPieces(board);
    }

    @Override
    public Move getPlayerMove(ChessGame chessGame, boolean blackMove) throws IOException {
        world.events.clearAll();
        printMessage("Your move! Right click a " + (blackMove ? "black" : "white") + " piece with your sword to select it");
        while (true) {
            selectSquare(null);
            Square fromSquare = waitForSwordHit(chessGame);
            if (!fromSquare.getPiece().isPresent() || fromSquare.getPiece().get().isBlack() != blackMove) {
                continue;
            }
            selectSquare(fromSquare);
            Piece selectedPiece = fromSquare.getPiece().get();
            printMessage("You selected a " + selectedPiece + ". Right click a square to move it there.");
            Square toSquare = waitForSwordHit(chessGame);
            if (toSquare == fromSquare) {
                // Cancel move
                continue;
            }
            Move move = chessGame.newMove(fromSquare, toSquare);
            Optional<String> validationError = chessGame.getValidationError(blackMove, move);
            if (!validationError.isPresent()) {
                selectSquare(null);
                return move;
            } else {
                printMessage("Bad move: " + validationError.get());
            }
        }

    }

    private void selectSquare(Square square) {
        Square previousSelectedSquare = selectedSquare;
        this.selectedSquare = square;
        if (previousSelectedSquare != null) {
            drawSinglePieceAt(previousSelectedSquare);
        }
        if (selectedSquare != null) {
            drawSinglePieceAt(selectedSquare);
        }
    }

    private Square waitForSwordHit(ChessGame chessGame) {
        while (true) {
            List<BlockHitEvent> events = world.events.pollBlockHits();
            for (BlockHitEvent event : events) {
                if (event.position.y >= boardCorner.y && event.position.y <= boardCorner.y + 4) {
                    int xSquare = (event.position.x - boardCorner.x) / 3;
                    int zSquare = (boardCorner.z - event.position.z) / 3;
                    if (xSquare >= 0 && xSquare < 8 && zSquare >= 0 && zSquare < 8) {
                        return chessGame.getBoard().getSquare(xSquare, zSquare);
                    }
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void printBoard(Board board) {
        drawBoard();
        drawPieces(board);
    }

    public static void main(String[] args) throws IOException {
        Minecraft world = Minecraft.connect(args);
        PiChess piChess = new PiChess(world);
        ChessGame game = new ChessGame(piChess);
        game.playGame();
    }

}

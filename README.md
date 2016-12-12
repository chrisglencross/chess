# Chess
A toy game of chess with a Minecraft Raspberry Pi Edition GUI, as well as simple text. As with most of my other
projects, it was written purely for fun. The basic chess engine was written over a couple of days in December 2014,
then the Raspberry Pi Minecraft integration was added in April 2015.

![Chess for Minecraft Pi Edition](https://raw.githubusercontent.com/chrisglencross/chess/master/images/in_game.png)

## Getting Started

* Download chess.jar onto your Raspberry Pi. I'm assuming you have a recent version of Raspbian, which includes Minecraft Pi Edition and Java 8 preinstalled.
* Start Minecraft Pi Edition on your Raspberry Pi.
* Start a game with a new map. The game will plonk a huge chess board in the middle of your world on top of anything in its way, and you don't want it destroying your village!
* Walk the character to the location on the map where you want to create the chess board.
* Open a Terminal window and run: java -jar chess.jar
* Wait a few seconds (or tens of seconds) while the board and chess pieces are created.

## To Play

* Hit a piece with your sword to pick it up, and hit a chess board square with your sword to put the piece there.
* You can hit a piece that you've picked up to put it back down without moving it, if you change your mind.

## Command Line Usage

Although a bit boring, you can also play the game at a text console on a computer that isn't a Raspberry Pi (but everyone has a drawer full, right??).
> java -cp chess.jar org.glencross.chess.ChessGame

## How it works

The chess engine demonstrates the use of a very standard [Minimax Algorithm](https://en.wikipedia.org/wiki/Minimax), which assigns a score to board positions, and makes moves to maximise the player's own score while providing minimal opportunity for the opponent to maximise their score.

The rules used by the the algorithm for scoring a board position are very simple:

* Achieving checkmate scores infinity.
* Being checkmated scores minus infinity.
* Add up the scores from all pieces remaining on the board, positive for your own pieces, negative for your opponent's: Queen 9, Rook 5, Bishop and Knight 3, Pawn 1.
* A small score for moving pieces towards the centre of the board.

The chess engine doesn't know anything about standard openings, and it only looks ahead around 3 moves. In other words, you should be able beat it!

## Minecraft Pi Edition Integration

This was provided by the following library:
https://github.com/martinohanlon/mcpi

Thanks to the author!

## Video

Here's some shaky-cam footage of the game that I recorded shortly after writing the code, featuring my dulcet tones.

[![Video](https://i.ytimg.com/vi/d1OafP67Z2s/hqdefault.jpg)](https://www.youtube.com/watch?v=d1OafP67Z2s)


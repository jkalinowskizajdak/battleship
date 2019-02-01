package com.battleship.entity;

import java.util.*;
import java.util.stream.Stream;

public class BattleBoard {

    public static final int FIRST_BOARD_INDEX = 0;
    public static final int BOARD_SIZE = 10;
    private final BoardPoint[][] board = createEmptyBoard();

    private static BoardPoint[][] createEmptyBoard() {
        BoardPoint[][] emptyBoard = new BoardPoint[BOARD_SIZE][BOARD_SIZE];
        for (int i = FIRST_BOARD_INDEX; i < BOARD_SIZE; i++) {
            for (int j = FIRST_BOARD_INDEX; j < BOARD_SIZE; j++) {
                emptyBoard[i][j] = BoardPoint.builder()
                        .type(PointType.EMPTY)
                        .x(i)
                        .y(j)
                        .build();
            }
        }
        return emptyBoard;
    }

    public BattleBoard(Collection<BoardPoint> shipPoints) {
        shipPoints.forEach(ship -> board[ship.x][ship.y] = ship);
    }

    public boolean isAnyActiveDecks() {
        return Arrays.stream(board)
                .flatMap(Stream::of)
                .filter(point -> point.getType() == PointType.DECK_ACTIVE)
                .findAny()
                .isPresent();
    }

    public BoardPoint[][] getBoard() {
        return board;
    }

    public BoardPoint checkPoint(int x, int y) {
        return board[x][y];
    }

    public void setPoint(int x, int y, BoardPoint newPointStatus) {
        board[x][y] = newPointStatus;
    }

}

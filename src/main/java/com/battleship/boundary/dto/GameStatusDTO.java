package com.battleship.boundary.dto;

import com.battleship.entity.GameStatus;

public class GameStatusDTO {

    private GameStatus gameStatus;
    private int yourScore;
    private int opponentScore;

    public GameStatusDTO(GameStatus gameStatus, int yourScore, int opponentScore) {
        this.gameStatus = gameStatus;
        this.yourScore = yourScore;
        this.opponentScore = opponentScore;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getYourScore() {
        return yourScore;
    }

    public void setYourScore(int yourScore) {
        this.yourScore = yourScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }
}

package com.battleship.control;

public class GameException extends RuntimeException {

    public static final int INCORRECT_GAME_CODE = 400;
    public static final int INCORRECT_PLAYER_CODE = 401;
    public static final int OUT_OF_BOARD_CODE = 402;
    public static final int NOT_ALLOWED_CODE = 403;

    private final int code;

    public GameException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

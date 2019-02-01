package com.battleship.entity;

import com.battleship.control.GameException;
import com.battleship.control.PlayerStrategy;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.UUID;

public class Game {

    public static final int BEGIN_SCORE = 0;
    public static final String INCORRECT_PLAYER_ID_MESSAGE = "Incorrect player id!";
    public static final String POINT_VALUE_OUT_OF_BOARD_EXCEPTION_MESSAGE = "Point out of the board!";
    private static final String NO_SECOND_PLAYER_EXCEPTION_MESSAGE = "No second player connected!";
    private static final String GAME_ALREADY_STARTED_EXCEPTION_MESSAGE = "Game already started exception!";
    private static final String NOT_YOUR_TURN_EXCEPTION_MESSAGE = "Not your turn!";
    private static final String PLAYER_ALREADY_EXISTS = "Second player already exists!";
    private static final String INCORRECT_ADDED_PLAYER_EXCEPTION_MESSAGE =
            "Added player should has SECOND type!";
    private static final String NO_ACTIVE_MOVES_EXCEPTION_MESSAGE = "No active moves!";
    private static final Logger logger = Logger.getLogger(Game.class);
    private final String id;
    private final Player firstPlayer;
    private Player secondPlayer;
    private boolean started;
    private Object lock = new Object();

    private Game(Builder builder) {
        id = builder.id;
        firstPlayer = builder.firstPlayer;
        secondPlayer = builder.secondPlayer;
        started = false;
    }

    public String getId() {
        return id;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public boolean isStarted() {
        return started;
    }

    public Optional<Player> getSecondPlayer() {
        return Optional.ofNullable(secondPlayer);
    }

    public void addSecondPlayer(Player secondPlayer) {
        if (secondPlayer == null || PlayerType.SECOND != secondPlayer.getType()) {
            logger.error(getErrorMessage(INCORRECT_ADDED_PLAYER_EXCEPTION_MESSAGE));
            throw new GameException(
                    INCORRECT_ADDED_PLAYER_EXCEPTION_MESSAGE, GameException.NOT_ALLOWED_CODE);
        }
        synchronized (lock) {
            if (this.secondPlayer == null) {
                this.secondPlayer = secondPlayer;
                firstPlayer.setGameStatus(GameStatus.WAITING_FOR_OPPONENT_MOVE);
            } else {
                logger.error(getErrorMessage(PLAYER_ALREADY_EXISTS));
                throw new GameException(PLAYER_ALREADY_EXISTS, GameException.NOT_ALLOWED_CODE);
            }
        }
    }

    public synchronized ShotResult turn(String playerId, int x, int y) {
        validatePoints(x, y);
        if (firstPlayer.getId().equals(playerId)) {
            return turn(firstPlayer.getType(), x, y);
        } else if (secondPlayer.getId().equals(playerId)) {
            return turn(secondPlayer.getType(), x, y);
        } else {
            logger.error(getErrorMessage(INCORRECT_PLAYER_ID_MESSAGE));
            throw new GameException(INCORRECT_PLAYER_ID_MESSAGE, GameException.INCORRECT_PLAYER_CODE);
        }
    }

    private void validatePoints(int x, int y) {
        if (!isPointInBoard(x) || !isPointInBoard(y)) {
            logger.error(getErrorMessage(POINT_VALUE_OUT_OF_BOARD_EXCEPTION_MESSAGE));
            throw new GameException(
                    POINT_VALUE_OUT_OF_BOARD_EXCEPTION_MESSAGE, GameException.OUT_OF_BOARD_CODE);
        }
    }

    private boolean isPointInBoard(int point) {
        return point >= BattleBoard.FIRST_BOARD_INDEX && point < BattleBoard.BOARD_SIZE;
    }

    private ShotResult turn(PlayerType player, int x, int y) {
        if (PlayerType.FIRST == player) {
            return playerShot(firstPlayer, secondPlayer, x, y);
        } else {
            return playerShot(secondPlayer, firstPlayer, x, y);
        }
    }

    private ShotResult playerShot(Player sourcePlayer, Player targetPlayer, int x, int y) {
        if (GameStatus.YOUR_TURN == sourcePlayer.getGameStatus()) {
            return shot(sourcePlayer, targetPlayer, x, y);
        } else if (GameStatus.AWAITING_PLAYERS == sourcePlayer.getGameStatus()) {
            logger.error(getErrorMessage(NO_SECOND_PLAYER_EXCEPTION_MESSAGE));
            throw new GameException(NO_SECOND_PLAYER_EXCEPTION_MESSAGE, GameException.NOT_ALLOWED_CODE);
        } else if (GameStatus.WAITING_FOR_OPPONENT_MOVE == sourcePlayer.getGameStatus()) {
            logger.error(getErrorMessage(NOT_YOUR_TURN_EXCEPTION_MESSAGE));
            throw new GameException(NOT_YOUR_TURN_EXCEPTION_MESSAGE, GameException.NOT_ALLOWED_CODE);
        } else {
            logger.error(getErrorMessage(NO_ACTIVE_MOVES_EXCEPTION_MESSAGE));
            throw new GameException(NO_ACTIVE_MOVES_EXCEPTION_MESSAGE, GameException.NOT_ALLOWED_CODE);
        }
    }

    private ShotResult shot(Player sourcePlayer, Player targetPlayer, int x, int y) {
        ShotResult result = targetPlayer.shotOnShip(x, y);
        if (ShotStatus.MISS == result.getShotStatus()) {
            sourcePlayer.setGameStatus(GameStatus.WAITING_FOR_OPPONENT_MOVE);
            targetPlayer.setGameStatus(GameStatus.YOUR_TURN);
            logger.info("Player " + sourcePlayer.getId() + " missed in game " + getId());
        } else {
            sourcePlayer.addPoint();
            logger.info("Player " + sourcePlayer.getId() + " hit in game " + getId());
            boolean sunken = result.getSunken().orElse(false);
            if (sunken && !targetPlayer.hasActiveShip()) {
                sourcePlayer.setGameStatus(GameStatus.YOU_WON);
                targetPlayer.setGameStatus(GameStatus.YOU_LOST);
                logger.info("Player " + sourcePlayer.getId() + " won in game " + getId());
            }
        }
        return result;
    }

    public synchronized void startGame() {
        if (secondPlayer == null) {
            logger.error(getErrorMessage(NO_SECOND_PLAYER_EXCEPTION_MESSAGE));
            throw new GameException(NO_SECOND_PLAYER_EXCEPTION_MESSAGE, GameException.NOT_ALLOWED_CODE);
        }
        if (started) {
            logger.error(getErrorMessage(GAME_ALREADY_STARTED_EXCEPTION_MESSAGE));
            throw new GameException(GAME_ALREADY_STARTED_EXCEPTION_MESSAGE, GameException.NOT_ALLOWED_CODE);
        }
        firstPlayer.buildBoard();
        firstPlayer.setGameStatus(GameStatus.WAITING_FOR_OPPONENT_MOVE);
        secondPlayer.buildBoard();
        started = true;
        logger.info("Game " + getId() + " was started");
    }

    private String getErrorMessage(String errorMessage) {
        return "Game id " + getId() + " - " + errorMessage;
    }

    public static Player createFirstPlayer(PlayerStrategy strategy) {
        return createPlayer(PlayerType.FIRST, GameStatus.AWAITING_PLAYERS, strategy);
    }

    public static Player createSecondPlayer(PlayerStrategy strategy) {
        return createPlayer(PlayerType.SECOND, GameStatus.YOUR_TURN, strategy);
    }

    private static Player createPlayer(
            PlayerType playerType, GameStatus status, PlayerStrategy strategy) {
        return Player.builder().type(playerType).GameStatus(status).strategy(strategy).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private Player firstPlayer;
        private Player secondPlayer;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder firstPlayer(Player firstPlayer) {
            this.firstPlayer = firstPlayer;
            return this;
        }

        public Builder secondPlayer(Player secondPlayer) {
            this.secondPlayer = secondPlayer;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }
}

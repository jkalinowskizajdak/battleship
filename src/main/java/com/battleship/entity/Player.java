package com.battleship.entity;

import com.battleship.control.GameException;
import com.battleship.control.PlayerStrategy;
import org.apache.log4j.Logger;

import java.util.UUID;

public class Player {

    private static final Logger logger = Logger.getLogger(Player.class);
    private final String id;
    private final PlayerType type;
    private final PlayerStrategy strategy;
    private BattleBoard board;
    private GameStatus gameStatus;
    private int points;

    private Player(Builder builder) {
        id = builder.id;
        type = builder.type;
        gameStatus = builder.gameStatus;
        strategy = builder.strategy;
        points = Game.BEGIN_SCORE;
    }

    public String getId() {
        return id;
    }

    public PlayerType getType() {
        return type;
    }

    public BattleBoard getBoard() {
        return board;
    }

    public void addPoint() {
        points++;
        logger.info("Player " + getId() + " score was increased to " + points);
    }

    public int getPoints() {
        return points;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        logger.info("Player " + getId() + " game status was changed to " + gameStatus);
    }

    public ShotResult shotOnShip(int x, int y) {
        BoardPoint pointStatus = board.checkPoint(x, y);
        logger.info("Shot on player " + getId() + " point " + pointStatus.getType());
        if (PointType.EMPTY == pointStatus.getType() ||
                PointType.DECK_HIT == pointStatus.getType()) {
            return ShotResult.builder()
                    .shotStatus(ShotStatus.MISS)
                    .build();
        } else {
            pointStatus.getShip().shot();
            pointStatus.setType(PointType.DECK_HIT);
            board.setPoint(x, y, pointStatus);
            boolean sunken = pointStatus.getShip().isSunken();
            return ShotResult.builder()
                    .shipType(pointStatus.getShip().getType())
                    .sunken(sunken)
                    .shotStatus(ShotStatus.HIT)
                    .build();
        }
    }

    public boolean hasActiveShip() {
        return board.isAnyActiveDecks();
    }

    public void buildBoard() {
        if (strategy.isStrategyReady()) {
            board = BattleBoardFactory.getBattleBoard(strategy, type);
        } else {
            logger.error("Player " + getId() + " strategy is not ready");
            throw new GameException("Player strategy is not ready!", GameException.NOT_ALLOWED_CODE);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private PlayerType type;
        private GameStatus gameStatus;
        private PlayerStrategy strategy;

        public Builder type(PlayerType type) {
            this.type = type;
            return this;
        }

        public Builder GameStatus(GameStatus gameStatus) {
            this.gameStatus = gameStatus;
            return this;
        }

        public Builder strategy(PlayerStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public Player build() {
            return new Player(this);
        }
    }

}

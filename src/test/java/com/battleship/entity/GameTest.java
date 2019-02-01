package com.battleship.entity;

import com.battleship.TestUtil;
import com.battleship.control.GameException;
import com.battleship.control.PlayerStrategy;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GameTest {

    private Game game;
    private Player firstPlayer;
    private Player secondPlayer;
    private PlayerStrategy playerStrategy;

    @Before
    public void initGame() {
        playerStrategy = new DefaultPlayerStrategy();
        firstPlayer = Game.createFirstPlayer(playerStrategy);
        secondPlayer = Game.createSecondPlayer(playerStrategy);
        game = Game.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();
        game.startGame();
    }

    @Test
    public void IsShotStatusHitAfterShotOnShipPointBySecondPlayer() {
        checkHitShot(secondPlayer, firstPlayer);
    }

    @Test
    public void IsShotStatusHitAfterShotOnShipPointByFirstPlayer() {
        missChangeTurn();
        checkHitShot(firstPlayer, secondPlayer);
    }

    @Test
    public void IsShipSunkenAfterShotOnShipPointBySecondPlayer() {
        checkSunkShot(secondPlayer, firstPlayer);
    }

    @Test
    public void IsShipSunkenAfterShotOnShipPointByFirstPlayer() {
        missChangeTurn();
        checkSunkShot(firstPlayer, secondPlayer);
    }

    @Test
    public void IsShotStatusMissAfterShotOnShipPointBySecondPlayer() {
        checkMissShot(secondPlayer, firstPlayer);
    }

    @Test
    public void IsShotStatusMissAfterShotOnShipPointByFirstPlayer() {
        missChangeTurn();
        checkMissShot(firstPlayer, secondPlayer);
    }

    @Test
    public void IsEndGameAfterShotsOnAllShipPointBySecondPlayer() {
        checkEndGameShots(secondPlayer, firstPlayer);
    }

    @Test
    public void IsEndGameAfterShotsOnAllShipPointByFirstPlayer() {
        missChangeTurn();
        checkEndGameShots(firstPlayer, secondPlayer);
    }

    @Test
    public void IsGameStatusWaitingForOpponentMoveForFirstPlayerAfterStartingGame() {
        Assertions.assertThat(firstPlayer.getGameStatus()).isEqualTo(GameStatus.WAITING_FOR_OPPONENT_MOVE);
    }

    @Test(expected = GameException.class)
    public void FailIfShotByUnknownPlayer() {
        Player player = Player.builder()
                .type(PlayerType.SECOND)
                .GameStatus(GameStatus.YOUR_TURN)
                .strategy(playerStrategy)
                .build();
        game.turn(player.getId(), 0, 0);
    }

    @Test(expected = GameException.class)
    public void FailIfShotByFirstPlayerWhenThereIsNoSecondPlayer() {
        Player player = Game.createFirstPlayer(playerStrategy);
        Game newGame = Game.builder().firstPlayer(player).build();
        newGame.turn(player.getId(), 0, 0);
    }

    @Test(expected = GameException.class)
    public void FailIfShotPointIsLessThenBoardStartIndex() {
        game.turn(firstPlayer.getId(), BattleBoard.FIRST_BOARD_INDEX - 1, BattleBoard.FIRST_BOARD_INDEX - 1);
    }

    @Test(expected = GameException.class)
    public void FailIfShotPointIsBiggerTheBoardSize() {
        game.turn(firstPlayer.getId(), BattleBoard.BOARD_SIZE, BattleBoard.BOARD_SIZE);
    }

    @Test(expected = GameException.class)
    public void FailToAddSecondPlayerMoreThenOnce() {
        Player player = Player.builder()
                .type(PlayerType.SECOND)
                .GameStatus(GameStatus.YOUR_TURN)
                .strategy(playerStrategy)
                .build();
        game.addSecondPlayer(player);
    }

    @Test(expected = GameException.class)
    public void FailToAddNullSecondPlayer() {
        Game game = Game.builder()
                .firstPlayer(firstPlayer)
                .build();
        game.addSecondPlayer(null);
    }

    @Test(expected = GameException.class)
    public void FailToAddSecondPlayerWithDifferentType() {
        Game game = Game.builder()
                .firstPlayer(firstPlayer)
                .build();
        game.addSecondPlayer(firstPlayer);
    }

    @Test(expected = GameException.class)
    public void FailIfFirstPlayerStartsGame() {
        game.turn(firstPlayer.getId(), 0, 0);
    }

    @Test(expected = GameException.class)
    public void FailShotByFirstPlayerIfItIsSecondPlayerTurn() {
        checkHitShot(firstPlayer, secondPlayer);
    }

    @Test(expected = GameException.class)
    public void FailShotBySecondPlayerIfItIsFirstPlayerTurn() {
        missChangeTurn();
        checkHitShot(secondPlayer, firstPlayer);
    }

    @Test(expected = GameException.class)
    public void FailIfGameIsStatedSecondTime() {
        game.startGame();
    }

    @Test(expected = GameException.class)
    public void FailIfThereIsNoSecondPlayerWhenGameStarting() {
        Game game = Game.builder()
                .firstPlayer(firstPlayer)
                .build();
        game.startGame();
    }


    private void missChangeTurn() {
        firstPlayer.setGameStatus(GameStatus.YOUR_TURN);
        secondPlayer.setGameStatus(GameStatus.WAITING_FOR_OPPONENT_MOVE);
    }

    private void checkHitShot(Player sourcePlayer, Player targetPlayer) {
        ShipType type = ShipType.TWO_DECKER;
        List<BoardPoint> firstPlayerTwoDeckerShipPoints = TestUtil.getAllShipPoints(targetPlayer)
                .stream()
                .filter(point -> type == point.getShip().getType())
                .collect(Collectors.toList());
        Assertions.assertThat(firstPlayerTwoDeckerShipPoints).hasSize(type.getDecks());
        BoardPoint firstPoint = firstPlayerTwoDeckerShipPoints.get(0);
        int oldPlayerPoints = sourcePlayer.getPoints();
        ShotResult firstShot = game.turn(sourcePlayer.getId(), firstPoint.getX(), firstPoint.getY());
        Assertions.assertThat(firstShot.getShotStatus()).isEqualTo(ShotStatus.HIT);
        Assertions.assertThat(firstShot.getShipType()).matches(getShipTypePredicate(type));
        boolean sunken = false;
        Assertions.assertThat(firstShot.getSunken()).matches(getSunkenPredicate(sunken));
        Assertions.assertThat(sourcePlayer.getGameStatus()).isEqualTo(GameStatus.YOUR_TURN);
        Assertions.assertThat(targetPlayer.getGameStatus()).isEqualTo(GameStatus.WAITING_FOR_OPPONENT_MOVE);
        Assertions.assertThat(sourcePlayer.getPoints()).isGreaterThan(oldPlayerPoints);
    }

    private void checkEndGameShots(Player sourcePlayer, Player targetPlayer) {
        List<BoardPoint> firstPlayerAllShipPoints = TestUtil.getAllShipPoints(targetPlayer);
        int lastIndex = firstPlayerAllShipPoints.size() - 1;
        BoardPoint lastPoint = firstPlayerAllShipPoints.get(lastIndex);
        firstPlayerAllShipPoints.remove(lastIndex);
        firstPlayerAllShipPoints.forEach(point -> game.turn(sourcePlayer.getId(), point.getX(), point.getY()));
        ShotResult lastShot = game.turn(sourcePlayer.getId(), lastPoint.getX(), lastPoint.getY());
        Assertions.assertThat(lastShot.getShotStatus()).isEqualTo(ShotStatus.HIT);
        boolean sunken = true;
        Assertions.assertThat(lastShot.getSunken()).matches(getSunkenPredicate(sunken));
        Assertions.assertThat(lastShot.getShipType()).isNotNull();
        Assertions.assertThat(sourcePlayer.getGameStatus()).isEqualTo(GameStatus.YOU_WON);
        Assertions.assertThat(targetPlayer.getGameStatus()).isEqualTo(GameStatus.YOU_LOST);
        Assertions.assertThat(sourcePlayer.getPoints()).isGreaterThan(targetPlayer.getPoints());
    }

    private void checkSunkShot(Player sourcePlayer, Player targetPlayer) {
        ShipType type = ShipType.ONE_DECKER;
        Optional<BoardPoint> firstPlayerOneDeckerShipPoints = TestUtil.getAllShipPoints(targetPlayer)
                .stream()
                .filter(point -> ShipType.ONE_DECKER == point.getShip().getType())
                .findAny();
        Assertions.assertThat(firstPlayerOneDeckerShipPoints).isNotEmpty();
        BoardPoint point = firstPlayerOneDeckerShipPoints.get();
        int oldPlayerPoints = sourcePlayer.getPoints();
        ShotResult shot = game.turn(sourcePlayer.getId(), point.getX(), point.getY());
        Assertions.assertThat(shot.getShotStatus()).isEqualTo(ShotStatus.HIT);
        Assertions.assertThat(shot.getShipType()).matches(getShipTypePredicate(type));
        boolean sunken = true;
        Assertions.assertThat(shot.getSunken()).matches(getSunkenPredicate(sunken));
        Assertions.assertThat(sourcePlayer.getGameStatus()).isEqualTo(GameStatus.YOUR_TURN);
        Assertions.assertThat(targetPlayer.getGameStatus()).isEqualTo(GameStatus.WAITING_FOR_OPPONENT_MOVE);
        Assertions.assertThat(sourcePlayer.getPoints()).isGreaterThan(oldPlayerPoints);
    }


    private void checkMissShot(Player sourcePlayer, Player targetPlayer) {
        Optional<BoardPoint> emptyPoint = TestUtil.getEmptyPoint(targetPlayer);
        Assertions.assertThat(emptyPoint).isNotEmpty();
        BoardPoint point = emptyPoint.get();
        int oldPlayerPoints = sourcePlayer.getPoints();
        ShotResult shot = game.turn(sourcePlayer.getId(), point.getX(), point.getY());
        Assertions.assertThat(shot.getShotStatus()).isEqualTo(ShotStatus.MISS);
        Assertions.assertThat(shot.getShipType()).isEmpty();
        Assertions.assertThat(shot.getSunken()).isEmpty();
        Assertions.assertThat(sourcePlayer.getGameStatus()).isEqualTo(GameStatus.WAITING_FOR_OPPONENT_MOVE);
        Assertions.assertThat(targetPlayer.getGameStatus()).isEqualTo(GameStatus.YOUR_TURN);
        Assertions.assertThat(sourcePlayer.getPoints()).isEqualTo(oldPlayerPoints);
    }

    public static Predicate getSunkenPredicate(boolean isSunken) {
        Predicate<Optional<Boolean>> sunkenPredicate = (sunken) -> {
            return sunken.isPresent() && sunken.get() == isSunken;
        };
        return sunkenPredicate;
    }

    public static Predicate getShipTypePredicate(ShipType shipType) {
        Predicate<Optional<ShipType>> shipTypePredicate = (type) -> {
            return type.isPresent() && type.get() == shipType;
        };
        return shipTypePredicate;
    }

}

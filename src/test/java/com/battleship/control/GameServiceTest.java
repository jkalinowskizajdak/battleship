package com.battleship.control;

import com.battleship.TestUtil;
import com.battleship.boundary.dto.GameStatusDTO;
import com.battleship.boundary.dto.MoveDTO;
import com.battleship.boundary.dto.ShotStatusDTO;
import com.battleship.entity.*;
import com.battleship.entity.GamesInMemoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class GameServiceTest {

    private GameService gameService;
    private GamesRepository gamesRepository;
    private Game game;

    @Before
    public void initGameService() {
        gamesRepository = GamesInMemoryRepository.getInstance();
        gameService = new GameService();
        game = gameService.generateGame();
    }

    @Test
    public void IsNewGameCreated() {
        Assertions.assertThat(game).isNotNull();
        Assertions.assertThat(gamesRepository.getGame(game.getId())).isNotEmpty();
    }

    @Test
    public void IsPlayerStatusAwaitingPlayerAfterGameCreation() {
        GameStatusDTO firstGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), game.getFirstPlayer().getId());
        Assertions.assertThat(firstGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.AWAITING_PLAYERS);
        Assertions.assertThat(firstGameStatusDTO.getYourScore()).isEqualTo(Game.BEGIN_SCORE);
        Assertions.assertThat(firstGameStatusDTO.getOpponentScore()).isEqualTo(Game.BEGIN_SCORE);
    }

    @Test
    public void IsSecondPlayerTurnAfterJoiningGameAndBothPlayerScoreHasBeginValue() {
        GameStatusDTO secondPlayerGameStatusDTO = gameService.join(game.getId());
        Assertions.assertThat(secondPlayerGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.YOUR_TURN);
        Assertions.assertThat(secondPlayerGameStatusDTO.getYourScore()).isEqualTo(Game.BEGIN_SCORE);
        Assertions.assertThat(secondPlayerGameStatusDTO.getOpponentScore()).isEqualTo(Game.BEGIN_SCORE);
        GameStatusDTO firstGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), game.getFirstPlayer().getId());
        Assertions.assertThat(firstGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.WAITING_FOR_OPPONENT_MOVE);
        Assertions.assertThat(firstGameStatusDTO.getYourScore()).isEqualTo(Game.BEGIN_SCORE);
        Assertions.assertThat(firstGameStatusDTO.getOpponentScore()).isEqualTo(Game.BEGIN_SCORE);
    }

    @Test
    public void IsFirstPlayerTurnAfterMissShotBySecondPlayer() {
        joinToGame();
        Player firstPlayer = game.getFirstPlayer();
        Optional<Player> secondPlayer = game.getSecondPlayer();
        Assertions.assertThat(secondPlayer).isNotEmpty();
        BoardPoint emptyPoint = TestUtil.getEmptyPoint(firstPlayer).get();
        MoveDTO moveDTO = TestUtil.getMoveDTO(emptyPoint.getX(), emptyPoint.getY());
        ShotStatusDTO result = gameService.shot(game.getId(), secondPlayer.get().getId(), moveDTO);
        Assertions.assertThat(result.getResult()).isEqualTo(ShotStatus.MISS);
        Assertions.assertThat(result.getShipType()).isNull();
        Assertions.assertThat(result.isSunken()).isNull();
        GameStatusDTO firstGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), firstPlayer.getId());
        Assertions.assertThat(firstGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.YOUR_TURN);
    }

    @Test
    public void IsSecondPlayerScoreIncreasedAfterShotOnShipAndIsStillSecondPlayerTurn() {
        ShipType type = ShipType.TWO_DECKER;
        joinToGame();
        Player firstPlayer = game.getFirstPlayer();
        Optional<Player> secondPlayer = game.getSecondPlayer();
        Assertions.assertThat(secondPlayer).isNotEmpty();
        BoardPoint shipPoint = TestUtil.getBoardPoint(firstPlayer, type).get();
        MoveDTO moveDTO = TestUtil.getMoveDTO(shipPoint.getX(), shipPoint.getY());
        ShotStatusDTO result = gameService.shot(game.getId(), secondPlayer.get().getId(), moveDTO);
        Assertions.assertThat(result.getResult()).isEqualTo(ShotStatus.HIT);
        Assertions.assertThat(result.getShipType()).matches(getShipTypePredicate(type));
        Assertions.assertThat(result.isSunken()).matches(getSunkenPredicate(Boolean.FALSE));
        GameStatusDTO firstGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), firstPlayer.getId());
        Assertions.assertThat(firstGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.WAITING_FOR_OPPONENT_MOVE);
        GameStatusDTO secondGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), secondPlayer.get().getId());
        Assertions.assertThat(secondGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.YOUR_TURN);
        Assertions.assertThat(secondGameStatusDTO.getYourScore()).isGreaterThan(Game.BEGIN_SCORE);
    }

    @Test
    public void IsSecondPlayerScoreIncreasedAndShipIsSunkenAfterShotOnOneDeckerShip() {
        ShipType type = ShipType.ONE_DECKER;
        joinToGame();
        Player firstPlayer = game.getFirstPlayer();
        Optional<Player> secondPlayer = game.getSecondPlayer();
        Assertions.assertThat(secondPlayer).isNotEmpty();
        BoardPoint shipPoint = TestUtil.getBoardPoint(firstPlayer, type).get();
        MoveDTO moveDTO = TestUtil.getMoveDTO(shipPoint.getX(), shipPoint.getY());
        ShotStatusDTO result = gameService.shot(game.getId(), secondPlayer.get().getId(), moveDTO);
        Assertions.assertThat(result.getResult()).isEqualTo(ShotStatus.HIT);
        Assertions.assertThat(result.getShipType()).matches(getShipTypePredicate(type));
        Assertions.assertThat(result.isSunken()).matches(getSunkenPredicate(Boolean.TRUE));
        GameStatusDTO firstGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), firstPlayer.getId());
        Assertions.assertThat(firstGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.WAITING_FOR_OPPONENT_MOVE);
        GameStatusDTO secondGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), secondPlayer.get().getId());
        Assertions.assertThat(secondGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.YOUR_TURN);
        Assertions.assertThat(secondGameStatusDTO.getYourScore()).isGreaterThan(Game.BEGIN_SCORE);
    }

    @Test
    public void IsSecondPlayerGameStatusWonAndFirstPlayerStatusLossAfterShotsOnAllShipsBySecondPlayer() {
        joinToGame();
        Player firstPlayer = game.getFirstPlayer();
        Player secondPlayer = game.getSecondPlayer().get();
        List<BoardPoint> firstPlayerAllShipPoints = TestUtil.getAllShipPoints(firstPlayer);
        int allPoints = firstPlayerAllShipPoints.size();
        int lastIndex = firstPlayerAllShipPoints.size() - 1;
        BoardPoint lastPoint = firstPlayerAllShipPoints.get(lastIndex);
        firstPlayerAllShipPoints.remove(lastIndex);
        firstPlayerAllShipPoints.forEach(point ->
                gameService.shot(game.getId(), secondPlayer.getId(), TestUtil.getMoveDTO(point.getX(), point.getY()))
        );
        MoveDTO moveDTO = TestUtil.getMoveDTO(lastPoint.getX(), lastPoint.getY());
        ShotStatusDTO result = gameService.shot(game.getId(), secondPlayer.getId(), moveDTO);
        Assertions.assertThat(result.getResult()).isEqualTo(ShotStatus.HIT);
        Assertions.assertThat(result.getShipType()).matches(getShipTypePredicate(lastPoint.getShip().getType()));
        Assertions.assertThat(result.isSunken()).matches(getSunkenPredicate(Boolean.TRUE));
        GameStatusDTO firstGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), firstPlayer.getId());
        Assertions.assertThat(firstGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.YOU_LOST);
        GameStatusDTO secondGameStatusDTO = gameService.getPlayerStatusGame(game.getId(), secondPlayer.getId());
        Assertions.assertThat(secondGameStatusDTO.getGameStatus()).isEqualTo(GameStatus.YOU_WON);
        Assertions.assertThat(secondGameStatusDTO.getYourScore()).isEqualTo(allPoints);
    }

    @Test(expected = GameException.class)
    public void FailIfJoinToNotExistingGame() {
        gameService.join(UUID.randomUUID().toString());
    }

    @Test(expected = GameException.class)
    public void FailIfShotOnPositionWhichContainsLettersOnly() {
        joinToGame();
        gameService.shot(game.getId(), game.getFirstPlayer().getId(), new MoveDTO("AB"));
    }

    @Test(expected = GameException.class)
    public void FailIfShotOnPositionWhichContainsNumbersOnly() {
        joinToGame();
        gameService.shot(game.getId(), game.getFirstPlayer().getId(), new MoveDTO("12"));
    }

    private void joinToGame() {
        gameService.join(game.getId());
        game.startGame();
    }

    public static Predicate getSunkenPredicate(boolean isSunken) {
        Predicate<Boolean> sunkenPredicate = (sunken) -> {
            return Objects.equals(sunken, isSunken);
        };
        return sunkenPredicate;
    }

    public static Predicate getShipTypePredicate(ShipType shipType) {
        Predicate<ShipType> shipTypePredicate = (type) -> {
            return Objects.equals(type, shipType);
        };
        return shipTypePredicate;
    }

}

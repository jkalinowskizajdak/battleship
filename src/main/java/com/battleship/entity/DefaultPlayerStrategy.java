package com.battleship.entity;

import com.battleship.control.PlayerStrategy;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultPlayerStrategy implements PlayerStrategy {

    private final Ship firstPlayerFourDeckerShip = new Ship(ShipType.FOUR_DECKER);
    private final Ship firstPlayerThreeDeckerShip = new Ship(ShipType.THREE_DECKER);
    private final Ship firstPlayerTwoDeckerShip = new Ship(ShipType.TWO_DECKER);
    private final Ship firstPlayerOneDeckerShip = new Ship(ShipType.ONE_DECKER);

    private final BoardPoint firstPlayerFourDeckerPoint1 = BoardPoint.builder()
            .ship(firstPlayerFourDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(0)
            .y(BattleBoard.BOARD_SIZE - 1)
            .build();

    private final BoardPoint firstPlayerFourDeckerPoint2 = BoardPoint.builder()
            .ship(firstPlayerFourDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(1)
            .y(BattleBoard.BOARD_SIZE - 1)
            .build();

    private final BoardPoint firstPlayerFourDeckerPoint3 = BoardPoint.builder()
            .ship(firstPlayerFourDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(2)
            .y(BattleBoard.BOARD_SIZE - 1)
            .build();

    private final BoardPoint firstPlayerFourDeckerPoint4 = BoardPoint.builder()
            .ship(firstPlayerFourDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(3)
            .y(BattleBoard.BOARD_SIZE - 1)
            .build();

    private final BoardPoint firstPlayerThreeDeckerPoint1 = BoardPoint.builder()
            .ship(firstPlayerThreeDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(0)
            .y(BattleBoard.BOARD_SIZE - 4)
            .build();

    private final BoardPoint firstPlayerThreeDeckerPoint2 = BoardPoint.builder()
            .ship(firstPlayerThreeDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(1)
            .y(BattleBoard.BOARD_SIZE - 4)
            .build();

    private final BoardPoint firstPlayerThreeDeckerPoint3 = BoardPoint.builder()
            .ship(firstPlayerThreeDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(2)
            .y(BattleBoard.BOARD_SIZE - 4)
            .build();

    private final BoardPoint firstPlayerTwoDeckerPoint1 = BoardPoint.builder()
            .ship(firstPlayerTwoDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(0)
            .y(BattleBoard.BOARD_SIZE - 7)
            .build();

    private final BoardPoint firstPlayerTwoDeckerPoint2 = BoardPoint.builder()
            .ship(firstPlayerTwoDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(1)
            .y(BattleBoard.BOARD_SIZE - 7)
            .build();

    private final BoardPoint firstPlayerOneDeckerPoint = BoardPoint.builder()
            .ship(firstPlayerOneDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(0)
            .y(0)
            .build();

    private final Ship secondPlayerFourDeckerShip = new Ship(ShipType.FOUR_DECKER);
    private final Ship secondPlayerThreeDeckerShip = new Ship(ShipType.THREE_DECKER);
    private final Ship secondPlayerTwoDeckerShip = new Ship(ShipType.TWO_DECKER);
    private final Ship secondPlayerOneDeckerShip = new Ship(ShipType.ONE_DECKER);

    private final BoardPoint secondPlayerFourDeckerPoint1 = BoardPoint.builder()
            .ship(secondPlayerFourDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(0)
            .y(0)
            .build();

    private final BoardPoint secondPlayerFourDeckerPoint2 = BoardPoint.builder()
            .ship(secondPlayerFourDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(0)
            .y(1)
            .build();

    private final BoardPoint secondPlayerFourDeckerPoint3 = BoardPoint.builder()
            .ship(secondPlayerFourDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(0)
            .y(2)
            .build();

    private final BoardPoint secondPlayerFourDeckerPoint4 = BoardPoint.builder()
            .ship(secondPlayerFourDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(0)
            .y(3)
            .build();

    private final BoardPoint secondPlayerThreeDeckerPoint1 = BoardPoint.builder()
            .ship(secondPlayerThreeDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(BattleBoard.BOARD_SIZE - 1)
            .y(3)
            .build();

    private final BoardPoint secondPlayerThreeDeckerPoint2 = BoardPoint.builder()
            .ship(secondPlayerThreeDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(BattleBoard.BOARD_SIZE - 2)
            .y(3)
            .build();

    private final BoardPoint secondPlayerThreeDeckerPoint3 = BoardPoint.builder()
            .ship(secondPlayerThreeDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(BattleBoard.BOARD_SIZE - 3)
            .y(3)
            .build();

    private final BoardPoint secondPlayerTwoDeckerPoint1 = BoardPoint.builder()
            .ship(secondPlayerTwoDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(BattleBoard.BOARD_SIZE - 1)
            .y(BattleBoard.BOARD_SIZE - 4)
            .build();

    private final BoardPoint secondPlayerTwoDeckerPoint2 = BoardPoint.builder()
            .ship(secondPlayerTwoDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(BattleBoard.BOARD_SIZE - 2)
            .y(BattleBoard.BOARD_SIZE - 4)
            .build();

    private final BoardPoint secondPlayerOneDeckerPoint = BoardPoint.builder()
            .ship(secondPlayerOneDeckerShip)
            .type(PointType.DECK_ACTIVE)
            .x(BattleBoard.BOARD_SIZE - 1)
            .y(BattleBoard.BOARD_SIZE - 1)
            .build();

    private final List<BoardPoint> firstPlayerPoints = ImmutableList.of(
            firstPlayerFourDeckerPoint1,
            firstPlayerFourDeckerPoint2,
            firstPlayerFourDeckerPoint3,
            firstPlayerFourDeckerPoint4,
            firstPlayerThreeDeckerPoint1,
            firstPlayerThreeDeckerPoint2,
            firstPlayerThreeDeckerPoint3,
            firstPlayerTwoDeckerPoint1,
            firstPlayerTwoDeckerPoint2,
            firstPlayerOneDeckerPoint);

    private final List<BoardPoint> secondPlayerPoints = ImmutableList.of(
            secondPlayerFourDeckerPoint1,
            secondPlayerFourDeckerPoint2,
            secondPlayerFourDeckerPoint3,
            secondPlayerFourDeckerPoint4,
            secondPlayerThreeDeckerPoint1,
            secondPlayerThreeDeckerPoint2,
            secondPlayerThreeDeckerPoint3,
            secondPlayerTwoDeckerPoint1,
            secondPlayerTwoDeckerPoint2,
            secondPlayerOneDeckerPoint);


    @Override
    public Collection<BoardPoint> getPlayerShipsOnBoard(PlayerType type) {
        if (PlayerType.FIRST == type) {
            return getDefaultStrategyForFirstPlayer();
        } else if (PlayerType.SECOND == type) {
            return getDefaultStrategyForSecondPlayer();
        }
        return new ArrayList<>();
    }

    //Not implemented
    @Override
    public void addShipPoint(PlayerType type, BoardPoint shipPoint) {

    }

    @Override
    public boolean isStrategyReady() {
        return true;
    }

    private Collection<BoardPoint> getDefaultStrategyForFirstPlayer() {
        return firstPlayerPoints;
    }

    private Collection<BoardPoint> getDefaultStrategyForSecondPlayer() {
        return secondPlayerPoints;
    }

}

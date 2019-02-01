package com.battleship.control;

import com.battleship.entity.BoardPoint;
import com.battleship.entity.PlayerType;

import java.util.Collection;

public interface PlayerStrategy {

    Collection<BoardPoint> getPlayerShipsOnBoard(PlayerType type);

    void addShipPoint(PlayerType type, BoardPoint shipPoint);

    boolean isStrategyReady();

}

package com.battleship.entity;

import com.battleship.control.PlayerStrategy;

public class BattleBoardFactory {

    private BattleBoardFactory() {

    }

    public static BattleBoard getBattleBoard(PlayerStrategy strategy, PlayerType playerType) {
        return new BattleBoard(strategy.getPlayerShipsOnBoard(playerType));
    }
}
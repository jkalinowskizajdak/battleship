package com.battleship.entity;

public enum ShipType {
    FOUR_DECKER(4), THREE_DECKER(3), TWO_DECKER(2), ONE_DECKER(1);

    private final int decks;

    ShipType(int decks) {
        this.decks = decks;
    }

    public int getDecks() {
        return decks;
    }
}

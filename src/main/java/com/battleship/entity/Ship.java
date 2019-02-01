package com.battleship.entity;

public class Ship {

    private final ShipType type;
    private int activeDecks;

    public Ship(ShipType type) {
        this.type = type;
        activeDecks = this.type.getDecks();
    }

    public void shot() {
        if (!isSunken()) {
            activeDecks--;
        }
    }

    public int getActiveDecks() {
        return activeDecks;
    }

    public ShipType getType() {
        return type;
    }

    public boolean isSunken() {
        return activeDecks == 0;
    }

}

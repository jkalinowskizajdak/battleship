package com.battleship.entity;


public class BoardPoint {

    private final Ship ship;
    private PointType type;
    final int x;
    final int y;

    private BoardPoint(Builder builder) {
        this.type = builder.type;
        this.ship = builder.ship;
        this.x = builder.x;
        this.y = builder.y;
    }

    public void setType(PointType type) {
        this.type = type;
    }

    public PointType getType() {
        return type;
    }

    public Ship getShip() {
        return ship;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PointType type;
        private Ship ship;
        private int x;
        private int y;

        public Builder type(PointType type) {
            this.type = type;
            return this;
        }

        public Builder ship(Ship ship) {
            this.ship = ship;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public BoardPoint build() {
            return new BoardPoint(this);
        }
    }
}
package com.battleship.entity;

import java.util.Optional;

public class ShotResult {

    private final ShotStatus shotStatus;
    private final ShipType shipType;
    private final Boolean sunken;

    private ShotResult(Builder builder) {
        shotStatus = builder.shotStatus;
        shipType = builder.shipType;
        sunken = builder.sunken;
    }

    public ShotStatus getShotStatus() {
        return shotStatus;
    }

    public Optional<ShipType> getShipType() {
        return Optional.ofNullable(shipType);
    }

    public Optional<Boolean> getSunken() {
        return Optional.ofNullable(sunken);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ShotStatus shotStatus;
        private ShipType shipType;
        private Boolean sunken;

        public Builder shotStatus(ShotStatus shotStatus) {
            this.shotStatus = shotStatus;
            return this;
        }

        public Builder shipType(ShipType shipType) {
            this.shipType = shipType;
            return this;
        }

        public Builder sunken(Boolean sunken) {
            this.sunken = sunken;
            return this;
        }

        public ShotResult build() {
            return new ShotResult(this);
        }
    }
}

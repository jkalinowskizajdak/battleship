package com.battleship.boundary.dto;

import com.battleship.entity.ShipType;
import com.battleship.entity.ShotStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShotStatusDTO {

    private ShotStatus result;
    private ShipType shipType;
    private Boolean sunken;

    public ShotStatusDTO(ShotStatus result, ShipType shipType, Boolean sunken) {
        this.result = result;
        this.shipType = shipType;
        this.sunken = sunken;
    }

    public ShotStatus getResult() {
        return result;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Boolean isSunken() {
        return sunken;
    }

    public void setResult(ShotStatus result) {
        this.result = result;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public void setSunken(Boolean sunken) {
        this.sunken = sunken;
    }

}

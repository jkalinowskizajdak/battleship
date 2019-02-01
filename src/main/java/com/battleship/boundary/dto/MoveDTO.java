package com.battleship.boundary.dto;

public class MoveDTO {

    private String position;

    public MoveDTO() {

    }

    public MoveDTO(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

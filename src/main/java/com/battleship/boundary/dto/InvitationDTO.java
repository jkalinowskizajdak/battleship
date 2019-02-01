package com.battleship.boundary.dto;

public class InvitationDTO {

    private String invitationUrl;

    public InvitationDTO(String invitationUrl) {
        this.invitationUrl = invitationUrl;
    }

    public String getInvitationUrl() {
        return invitationUrl;
    }

    public void setInvitationUrl(String invitationUrl) {
        this.invitationUrl = invitationUrl;
    }

}

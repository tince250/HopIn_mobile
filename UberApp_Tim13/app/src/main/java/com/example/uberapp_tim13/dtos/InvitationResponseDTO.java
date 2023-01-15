package com.example.uberapp_tim13.dtos;

public class InvitationResponseDTO {
    private int passengerId;
    private boolean response;

    public InvitationResponseDTO(int passengerId, boolean response) {
        this.passengerId = passengerId;
        this.response = response;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}

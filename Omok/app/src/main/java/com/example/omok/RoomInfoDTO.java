package com.example.omok;

public class RoomInfoDTO {
    private String RoomName;
    private String PlayerB;
    private String PlayerW;
    private boolean isWaiting;
    private boolean isGaming;
    private boolean isFinish;

    public RoomInfoDTO() {
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getPlayerB() {
        return PlayerB;
    }

    public void setPlayerB(String playerB) {
        PlayerB = playerB;
    }

    public String getPlayerW() {
        return PlayerW;
    }

    public void setPlayerW(String playerW) {
        PlayerW = playerW;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public boolean isGaming() {
        return isGaming;
    }

    public void setGaming(boolean gaming) {
        isGaming = gaming;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}

package com.example.omok;

public class OmokSignalDTO {
    private int Turn;
    private int Point_x;
    private int Point_y;
    private String Color;
    private String PlayerId;

    public OmokSignalDTO() {
    }

    public int getTurn() {
        return Turn;
    }

    public void setTurn(int turn) {
        Turn = turn;
    }

    public int getPoint_x() {
        return Point_x;
    }

    public void setPoint_x(int point_x) {
        Point_x = point_x;
    }

    public int getPoint_y() {
        return Point_y;
    }

    public void setPoint_y(int point_y) {
        Point_y = point_y;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getPlayerId() {
        return PlayerId;
    }

    public void setPlayerId(String playerId) {
        PlayerId = playerId;
    }
}

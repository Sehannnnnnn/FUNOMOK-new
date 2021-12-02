package com.example.omok;

public class OmokSignalDTO {
    int Turn;
    int Point_x;
    int Point_y;
    int Color_int;
    String Color_str;
    String PlayerId;

    public OmokSignalDTO(int turn, int point_x, int point_y, int color_int, String playerId) {
        Turn = turn;
        Point_x = point_x;
        Point_y = point_y;
        Color_int = color_int;
        Color_str = (Color_int == 1) ? "Black" : "White";
        PlayerId = playerId;
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

    public int getColor_int() {
        return Color_int;
    }

    public void setColor_int(int color_int) {
        Color_int = color_int;
    }

    public String getColor_str() {
        return Color_str;
    }

    public void setColor_str(String color_str) {
        Color_str = color_str;
    }

    public String getPlayerId() {
        return PlayerId;
    }

    public void setPlayerId(String playerId) {
        PlayerId = playerId;
    }
}

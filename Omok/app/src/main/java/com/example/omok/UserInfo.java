package com.example.omok;

public class UserInfo {
    String name;
    int win;
    int lose;

    public UserInfo(String name,int win,int lose){
        this.name=name;
        this.win=win;
        this.lose=lose;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }
}

package com.example.omok;

public class CheckVictoryActivity {
    //승리인지 판단
    public static boolean checkVictory(int[][] array,int i,int j) {

        if(countStone(array,i,j,0,1)+countStone(array,i,j,0,-1)==4) {
            return true;
        }else if(countStone(array,i,j,1,0)+countStone(array,i,j,-1,0)==4) {
            return true;
        }else if(countStone(array,i,j,-1,1)+countStone(array,i,j,1,-1)==4) {
            return true;
        }else if(countStone(array,i,j,-1,-1)+countStone(array,i,j,1,1)==4) {
            return true;
        }else {
            return false;
        }

    }
    // 기준 (i,j)점으로부터의 돌 개수 세기(방향:가로,세로,우 대각,좌 대각)
    public static int countStone(int[][] array,int i,int j,int dx,int dy) {

        int count=0;
        while((i>=0 && i<15) && (j>=0 && j<15)) {
            //index 넘어가면 break
            if(i+dx<0||i+dx>=15||j+dy<0||j+dy>=15) break;

            if(array[i][j]==array[i+dx][j+dy])
                count++;
            else
                break;
            i=i+dx;
            j=j+dy;

        }
        return count;
    }
}

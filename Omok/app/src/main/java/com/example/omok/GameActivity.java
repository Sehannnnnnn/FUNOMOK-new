package com.example.omok;


import static com.example.omok.CheckVictoryActivity.checkVictory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;


public class GameActivity extends AppCompatActivity {
    GridLayout omokstage;
    // player = "white" or "black"
    String color = "white";
    String playerID = "sdasluveks";
    int turn = 0;
    DatabaseReference logDatabase, RoomDatabase;
    Intent intent;
    String ROOM_NAME;

    int omokArray[][] = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        omokstage = (GridLayout) findViewById(R.id.omokstage);

        intent = getIntent();
        ROOM_NAME = intent.getStringExtra("ROOMNAME");
        logDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/gamelog");
        RoomDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/gameinfo");


        //오목알 크기 지정
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);


        //오목판 생성
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                View _stone = (View) View.inflate(GameActivity.this, R.layout.omok_block, null);
                ImageButton stonecontainer = (ImageButton) _stone.findViewById(R.id.Img_Stone);
                stonecontainer.setId(i * 15 + j);
                stonecontainer.setTag('X');

                if (omokArray[i][j] == 1) {
                    stonecontainer.setTag("B");
                    stonecontainer.setBackground(ContextCompat.getDrawable(this, R.drawable.blackstone));
                } else if (omokArray[i][j] == 2) {
                    stonecontainer.setTag("W");
                    stonecontainer.setBackground(ContextCompat.getDrawable(this, R.drawable.whitestone));
                } else {
                    stonecontainer.setTag("X");
                    stonecontainer.setBackground(ContextCompat.getDrawable(this, R.drawable.nostone));
                }

                omokstage.addView(_stone, params);  //GridView 순서대로 정렬(0-225)
                final int final_i = i;
                final int final_j = j;
                // stonecotainer(이미지버튼) 마다 리스너를 설정
                stonecontainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stoneColor = (String) v.getTag();
                        if (stoneColor == "B" || stoneColor == "W") {
                            Toast.makeText(getApplicationContext(), "돌을 놓을 수 없다.", Toast.LENGTH_SHORT).show();
                        } else {
                            //오목판에 바둑돌 올리기
                            turn = turn + 1;
                            int stoneChanged_cor = v.getId();
                            int changedPointX = stoneChanged_cor / 15;
                            int changedPointY = stoneChanged_cor % 15;

                            OmokSignalDTO omokSignal = new OmokSignalDTO();
                            omokSignal.setTurn(turn);
                            omokSignal.setPoint_x(changedPointX);
                            omokSignal.setPoint_y(changedPointY);
                            omokSignal.setColor(color);
                            omokSignal.setPlayerId(playerID);
                            logDatabase.push().setValue(omokSignal);
                        }
                    }
                });
            }
        }
        logDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    OmokSignalDTO omoksignal = snapshot.getValue(OmokSignalDTO.class);
                    int x = omoksignal.getPoint_x();
                    int y = omoksignal.getPoint_y();
                    int stoneId = (x * 15) + y;
                    int turn = omoksignal.getTurn();
                    String color = omoksignal.getColor();
                    Log.d("stoneID", Integer.toString(stoneId));

                    View stoneChanged = (View) findViewById(stoneId);

                    putstone(stoneChanged, color);
                    change_omokArray(omokArray, x, y, color);
                    Log.d("omokArrayChanged", Arrays.deepToString(omokArray));
                    Boolean isWin = checkVictory(omokArray, x, y);
                    if (isWin == true) {
                        Toast.makeText(getApplicationContext(), "승부가 났습니다", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //돌 넣는 애니메이션 (화면에만 표시)
    private void putstone(View v, String color) {
        switch (color) {
            case "black":
                v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackstone));
                v.setTag("B");
                break;
            case "white":
                v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitestone));
                v.setTag("W");
                break;
        }
    }

    //바둑판 데이터 전송
    private void change_omokArray(int[][] omokArray, int X, int Y, String color) {
        int changedPointX = X;
        int changedPointY = Y;
        if (color == "black") {
            omokArray[changedPointX][changedPointY] = 1;
        } else if (color == "white") {
            omokArray[changedPointX][changedPointY] = 2;
        }
    }

    //    데이터베이스에 따라 오목판 변경하기 (작업중)
    private void read_omoksignal() {

    }

}

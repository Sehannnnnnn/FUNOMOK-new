package com.example.omok;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class GameActivity extends AppCompatActivity {
    GridLayout omokstage;
    // player = "white" or "black"
    String player = "black";
    String playerID = "sdasluveks";
    int turn = 0;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    Intent inIntent = getIntent();
    ArrayList<String> receiveArr= inIntent.getStringArrayListExtra("ROOMNAME");
    int receiveIndex=inIntent.getIntExtra("ROOMINDEX",0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        omokstage = (GridLayout) findViewById(R.id.omokstage);

        mDatabase = database.getReference("omokRoom");

        //오목알 크기 지정
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                width, height
        );

        int omokArray[][] = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};


        //오목판 출력
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                View _stone = (View) View.inflate(GameActivity.this, R.layout.omok_block, null);
                ImageButton stonecontainer = (ImageButton) _stone.findViewById(R.id.Img_Stone);
                stonecontainer.setId(i*15+j);
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
                omokstage.addView(_stone, params);
                stonecontainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stoneColor = (String) v.getTag();
                        if (stoneColor == "B" || stoneColor == "W") {
                            Toast.makeText(getApplicationContext(), "돌을 놓을 수 없다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //오목판에 바둑돌 올리기
                            putstone(v, player, stoneColor);
                            //오목 데이터 수정
                            turn = turn + 1;
                            change_omokArray(v, omokArray);
                            //gamelog hashmap에 signal data 저장
                            Log.d("omokArrayChanged", Arrays.deepToString(omokArray));
                            //오목데이터 Intent로 해서 CheckVictory.Activity로 보내면될듯.
                        }
                    }
                });
            }
        }
    }


    //돌 넣는 애니메이션
    private void putstone(View v, String player, String stoneColor) {
        int num = v.getId();
        switch (player) {
            case "black":
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.blackstone));
                v.setTag("B");
                break;
            case "white":
                v.setBackground(ContextCompat.getDrawable(this, R.drawable.whitestone));
                v.setTag("W");
                break;
        }
    }

    //바둑판 데이터 전송
    private void change_omokArray(View v, int[][] omokArray) {
        int stoneChanged_cor = v.getId();
        int changedPointX = stoneChanged_cor/15;
        int changedPointY = stoneChanged_cor%15;

        if (player == "black") {
            omokArray[changedPointX][changedPointY] = 1;
            OmokSignalDTO omokSignal = new OmokSignalDTO(turn, changedPointX, changedPointY, 1, playerID);
            //Firebase로 송신
            mDatabase.setValue(omokSignal);
        }
        else if (player == "white") {
            omokArray[changedPointX][changedPointY] = 2;
        }
    }

    //데이터베이스에 따라 오목판 변경하기 (작업중)
    private void read_omoksignal() {
        mDatabase.child(receiveArr.get(receiveIndex)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                OmokSignalDTO omokSignal = dataSnapshot.getValue(OmokSignalDTO.class);

                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                OmokSignalDTO omokSignal = dataSnapshot.getValue(OmokSignalDTO.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}

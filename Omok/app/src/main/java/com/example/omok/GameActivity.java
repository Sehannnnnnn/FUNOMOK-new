package com.example.omok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity {
    GridLayout omokstage;
    // player = "white" or "black"
    String player = "black";
    String playerID = "sdasluveks";
    int turn = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        omokstage = (GridLayout) findViewById(R.id.omokstage);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("No Hello, World!");
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });


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
                            Log.d("omokArrayChanged", java.util.Arrays.deepToString(omokArray));
                            //오목데이터 Intent로 해서 CheckVictory.Activity로 보내면될듯.
                        }
                    }
                });
            }
        }
    }



    //돌 넣는 애니메이션
    private void putstone(View v, String player, String stoneColor) {
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

    //바둑판 데이터 수정
    private int[][] change_omokArray(View v, int[][] omokArray) {
        int stoneChanged_cor = v.getId();
        int changedPointX = stoneChanged_cor/15;
        int changedPointY = stoneChanged_cor%15;
        if (player == "black") {
            omokArray[changedPointX][changedPointY] = 1;
            OmokSignalDTO omokSignalDTO = new OmokSignalDTO(turn, changedPointX, changedPointY, 1, playerID);
            Log.d("omokSignalCreated", omokSignalDTO.getSignal_str());
            //Firebase로 송신
        }
        else if (player == "white") {
            omokArray[changedPointX][changedPointY] = 2;
        }
        return omokArray;
    }
}

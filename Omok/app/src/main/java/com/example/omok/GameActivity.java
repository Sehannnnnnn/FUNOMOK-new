package com.example.omok;


import static com.example.omok.CheckVictoryActivity.checkVictory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    LinearLayout myPart, yourPart;
    TextView my_ID, your_ID, my_Message, your_Message, Roomname;
    // myColor = "white" or "black"
    String myColor;
    String myID, yourID;
    int turn;
    DatabaseReference logDatabase, RoomDatabase,GameStarter;
    ImageView your_profile;
    String ROOM_NAME;

    private final int Fragment_1 = 1;
    private final int Fragment_2 = 2;

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
        Intent intent = getIntent();
        myID = intent.getStringExtra("myID");
        myColor = intent.getStringExtra("color");
        ROOM_NAME = intent.getStringExtra("ROOMNAME");

        logDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/gameLog");
        RoomDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/roomInfo");
        GameStarter = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/roomInfo/gaming");
        turn = 0;

        //xml ?????????
        omokstage = (GridLayout) findViewById(R.id.omokstage);
        yourPart = (LinearLayout) findViewById(R.id.container_oppose);
        myPart = (LinearLayout) findViewById(R.id.container_my);
        my_ID = (TextView) findViewById(R.id.my_Id);
        your_ID = (TextView) findViewById(R.id.oppose_Id);
        my_Message = (TextView) findViewById(R.id.my_Message);
        your_Message = (TextView) findViewById(R.id.oppose_Message);
        Roomname = (TextView) findViewById(R.id.tv_roomname);
        your_profile = (ImageView) findViewById(R.id.oppose_Img);



        my_ID.setText(myID);
        Roomname.setText("??? ??????: " + ROOM_NAME);
        my_Message.setText("?????? ?????? ??????????????????."+"\n" +
                "?????? ?????? ?????? ?????????.");
        your_Message.setText("????????? ????????? ???????????? ????????????.");
        your_profile.setVisibility(View.INVISIBLE);


        //????????? ?????? ??????
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        //2??? for ??? ????????? ?????? + clickeventlistener add
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                View _stone = (View) View.inflate(GameActivity.this, R.layout.omok_block, null);
                ImageButton stone_container = (ImageButton) _stone.findViewById(R.id.Img_Stone);
                stone_container.setId(i * 15 + j);
                stone_container.setTag('X');
                if (omokArray[i][j] == 1) {
                    stone_container.setTag("B");
                    stone_container.setBackground(ContextCompat.getDrawable(this, R.drawable.blackstone));
                } else if (omokArray[i][j] == 2) {
                    stone_container.setTag("W");
                    stone_container.setBackground(ContextCompat.getDrawable(this, R.drawable.whitestone));
                } else {
                    stone_container.setTag("X");
                    stone_container.setBackground(ContextCompat.getDrawable(this, R.drawable.nostone));
                }
                omokstage.addView(_stone, params);  //GridView ???????????? ??????(0-225)
                // stonecotainer(???????????????) ?????? ???????????? ??????
                stone_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stoneColor = (String) v.getTag();
                        if (stoneColor == "B" || stoneColor == "W") {
                            Toast.makeText(getApplicationContext(), "?????? ?????? ??? ??????.", Toast.LENGTH_SHORT).show();
                        } else {
                            //???????????? ????????? ?????????
                            int stoneChanged_cor = v.getId();
                            int changedPointX = stoneChanged_cor / 15;
                            int changedPointY = stoneChanged_cor % 15;
                            OmokSignalDTO omokSignal = new OmokSignalDTO();
                            omokSignal.setTurn(turn);
                            omokSignal.setPoint_x(changedPointX);
                            omokSignal.setPoint_y(changedPointY);
                            omokSignal.setColor(myColor);
                            omokSignal.setPlayerId(myID);
                            //??????????????????????????? ??????
                            logDatabase.push().setValue(omokSignal);
                        }
                    }
                });
            }
        }

//        if (intent.getStringExtra("ROOMNAME") != null) {
//            ROOM_NAME = intent.getStringExtra("ROOMNAME");
//            logDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/gamelog");
//            RoomDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/gameinfo");
//            myColor = "white";
//            yourPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
//            your_Message.setText("????????? ?????? ?????? ????????????...");
//        }
//        if (intent.getStringExtra("createRoomName") != null) {
//            CREATE_ROOM_NAME = intent.getStringExtra("createRoomName");
//            logDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + CREATE_ROOM_NAME + "/gamelog");
//            RoomDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + CREATE_ROOM_NAME + "/gameinfo");
//            myColor = "black";
//            myPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
//            my_Message.setText("????????? ?????? ?????? ???????????????.");
//        }

        // ?????? ?????? ?????? ?????? ??????
        RoomDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomInfoDTO room_info = snapshot.getValue(RoomInfoDTO.class);
                if (room_info.isGaming() == true) {
                    Log.d("GameStarted", "?????? ??????????????????.");
                    your_profile.setVisibility(View.VISIBLE);
                    your_Message.setText("");
                    my_Message.setText("");
                    if(room_info.getPlayerB().equals(myID)) {
                        myPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
                        your_ID.setText(room_info.getPlayerW());
                        my_Message.setText("??????(???)?????????. ?????? ?????? ?????????.");
                    }
                    else if (room_info.getPlayerW().equals(myID)) {
                        your_ID.setText(room_info.getPlayerB());
                        yourPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
                        my_Message.setText("??????(???) ?????????. ??????????????????");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "???????????????????????? ?????????");
            }
        });
        //?????? ?????? ???????????? logDatabase?????? ??????????????? ????????????
        logDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OmokSignalDTO omoksignal = snapshot.getValue(OmokSignalDTO.class);
                turn = omoksignal.getTurn() + 1;
                int x = omoksignal.getPoint_x();
                int y = omoksignal.getPoint_y();
                int stoneId = (x * 15) + y;
                String color = omoksignal.getColor();
                String who_Stone = omoksignal.getPlayerId();
                View stoneChanged = (View) findViewById(stoneId);
                //?????? ???????????? ???????????? ???
                put_stone(stoneChanged, color);
                change_omokArray(omokArray, x, y, color);
                Log.d("omokArrayChanged", Arrays.deepToString(omokArray));
                boolean isWin = checkVictory(omokArray, x, y);
                Log.d("isWin", Boolean.toString(isWin));
                // ?????? ?????? ??? UI ??????
                setUserUI(myID, who_Stone);
                //?????? ??????
                checkisWin(isWin);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }


    //??? ?????? ??????????????? (???????????? ??????)
    private void put_stone(View v, String color) {
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

    //????????? ????????? ??????
    private void change_omokArray(int[][] omokArray, int X, int Y, String color) {
        int changedPointX = X;
        int changedPointY = Y;
        switch (color) {
            case "black":
                omokArray[changedPointX][changedPointY] = 1;
                break;
            case "white":
                omokArray[changedPointX][changedPointY] = 2;
                break;
        }
    }

    private void setUserUI(String _myID, String who_Stone) {
        if (who_Stone == _myID) {
            yourPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
            myPart.setBackgroundResource(0);
            my_Message.setText("");
            your_Message.setText("????????? ?????? ?????? ????????????...");
        } else {
            myPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
            yourPart.setBackgroundResource(0);
            my_Message.setText("????????? ?????? ?????? ???????????????.");
            your_Message.setText("");
        }
    }

    private void checkisWin(boolean isWin) {
        if (isWin == true) {
            if (yourID == myID) {
                FragmentView(Fragment_1);
            } else {
                FragmentView(Fragment_2);
            }
            my_Message.setText("?????? ???!");
            your_Message.setText("?????? ???!");
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            }, 5000);
        }
    }

    //?????? ?????????
    private void FragmentView(int fragment) {
        //FragmentTransaction??? ????????? ?????????????????? ???????????????.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragment) {
            case 1:
                // ?????? ??? ??????????????? ??????
                winFragment fragment1 = new winFragment();
                transaction.add(R.id.omokBackground, fragment1);
                transaction.commit();
                break;

            case 2:
                // ?????? ??? ??????????????? ??????
                loseFragment fragment2 = new loseFragment();
                transaction.add(R.id.omokBackground, fragment2);
                transaction.commit();
                break;
        }
    }

}

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {
    GridLayout omokstage;
    LinearLayout myPart, yourPart;
    TextView my_ID, your_ID, my_Message, your_Message, Roomname;
    // myColor = "white" or "black"
    String myColor;
    static String myID, yourID;
    int turn;
    DatabaseReference logDatabase, RoomDatabase,GameStarter;
    ImageView your_profile;
    Intent intent;
    String ROOM_NAME, CREATE_ROOM_NAME;

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
        turn = 0;
        omokstage = (GridLayout) findViewById(R.id.omokstage);
        yourPart = (LinearLayout) findViewById(R.id.container_oppose);
        myPart = (LinearLayout) findViewById(R.id.container_my);
        my_ID = (TextView) findViewById(R.id.my_Id);
        your_ID = (TextView) findViewById(R.id.oppose_Id);
        my_Message = (TextView) findViewById(R.id.my_Message);
        your_Message = (TextView) findViewById(R.id.oppose_Message);
        Roomname = (TextView) findViewById(R.id.tv_roomname);
        your_profile = (ImageView) findViewById(R.id.oppose_Img);

        intent = getIntent();
        myID = intent.getStringExtra("myID");
        ROOM_NAME = intent.getStringExtra("ROOMNAME");
        my_ID.setText(myID);
        Roomname.setText(ROOM_NAME);
        your_profile.setVisibility(View.INVISIBLE);

        logDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/gamelog");
        RoomDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/roominfo");
        GameStarter = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/roominfo/gaming");


        RoomDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomInfoDTO roominfo = new RoomInfoDTO();
                if (roominfo.isGaming()){
                    your_profile.setVisibility(View.VISIBLE);
                    your_Message.setText("");
                    my_Message.setText("");
                    if(roominfo.getPlayerB() == myID) {
                        your_ID.setText(roominfo.getPlayerW());
                        myColor = "black";
                        my_Message.setText("선공입니다. 먼저 돌을 두세요.");
                    }
                    else if (roominfo.getPlayerW() == myID) {
                        your_ID.setText(roominfo.getPlayerB());
                        myColor = "white";
                    }
                }
                else {
                    your_Message.setText("상대의 입장을 기다리고 있습니다.");
                    my_Message.setText("잠시 기다려주세요.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "데이터불러오기에 실패함");
            }
        });


//        if (intent.getStringExtra("ROOMNAME") != null) {
//            ROOM_NAME = intent.getStringExtra("ROOMNAME");
//            logDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/gamelog");
//            RoomDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + ROOM_NAME + "/gameinfo");
//            myColor = "white";
//            yourPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
//            your_Message.setText("상대가 돌을 놓는 중입니다...");
//        }
//        if (intent.getStringExtra("createRoomName") != null) {
//            CREATE_ROOM_NAME = intent.getStringExtra("createRoomName");
//            logDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + CREATE_ROOM_NAME + "/gamelog");
//            RoomDatabase = FirebaseDatabase.getInstance().getReference("omokRoom/" + CREATE_ROOM_NAME + "/gameinfo");
//            myColor = "black";
//            myPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
//            my_Message.setText("당신이 돌을 놓을 차례입니다.");
//        }




        //오목알 크기 지정
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        //black 일 때


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
                            int stoneChanged_cor = v.getId();
                            int changedPointX = stoneChanged_cor / 15;
                            int changedPointY = stoneChanged_cor % 15;

                            OmokSignalDTO omokSignal = new OmokSignalDTO();
                            omokSignal.setTurn(turn);
                            omokSignal.setPoint_x(changedPointX);
                            omokSignal.setPoint_y(changedPointY);
                            omokSignal.setColor(myColor);
                            omokSignal.setPlayerId(myID);
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
                turn = omoksignal.getTurn() + 1;
                int x = omoksignal.getPoint_x();
                int y = omoksignal.getPoint_y();
                int stoneId = (x * 15) + y;

//                yourID = omoksignal.getPlayerId();
//
//                if(turn%2==0) {
//                    if(myID.equals(yourID)){
//                        your_ID.setText("상대1");
//                    }else{
//                        your_ID.setText(yourID);
//                    }
//
//                }
//                if(turn%2==1){
//                    //내가 백
//                    if(myID.equals(yourID)){
//                        your_ID.setText("상대1");
//                    }else{
//                        your_ID.setText(yourID);
//                    }
//                }
                String color = omoksignal.getColor();
                View stoneChanged = (View) findViewById(stoneId);
                putstone(stoneChanged, color);
                change_omokArray(omokArray, x, y, color);
                Log.d("omokArrayChanged", Arrays.deepToString(omokArray));
                boolean isWin = checkVictory(omokArray, x, y);
                Log.d("isWin", Boolean.toString(isWin));
                setUserUI();
                if (isWin == true) {
                    if (yourID == myID) {
                        FragmentView(Fragment_1);
                    } else {
                        FragmentView(Fragment_2);
                    }
                    my_Message.setText("경기 끝!");
                    your_Message.setText("경기 끝!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                        }, 5000);
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

    //승패 메세지
    private void FragmentView(int fragment) {
        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragment) {
            case 1:
                // 첫번 째 프래그먼트 호출
                winFragment fragment1 = new winFragment();
                transaction.add(R.id.omokBackground, fragment1);
                transaction.commit();
                break;

            case 2:
                // 두번 째 프래그먼트 호출
                loseFragment fragment2 = new loseFragment();
                transaction.add(R.id.omokBackground, fragment2);
                transaction.commit();
                break;
        }
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
        switch (color) {
            case "black":
                omokArray[changedPointX][changedPointY] = 1;
                break;
            case "white":
                omokArray[changedPointX][changedPointY] = 2;
                break;
        }
    }

    private void setUserUI() {
        if (yourID == myID) {
            yourPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
            myPart.setBackgroundResource(0);
            my_Message.setText("");
            your_Message.setText("상대가 돌을 놓는 중입니다...");
        } else {
            myPart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
            yourPart.setBackgroundResource(0);
            my_Message.setText("당신이 돌을 놓을 차례입니다.");
            your_Message.setText("");
        }
    }


}

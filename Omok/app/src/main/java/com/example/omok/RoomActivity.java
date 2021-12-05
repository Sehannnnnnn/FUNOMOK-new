package com.example.omok;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;


public class RoomActivity extends AppCompatActivity {

    private EditText edtRoomName;
    private Button btnCreate;
    private ListView room_list;
    String userID;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        edtRoomName=(EditText) findViewById(R.id.edtRoomName);
        btnCreate=(Button) findViewById(R.id.btnCreate);
        room_list=(ListView) findViewById(R.id.room_list);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            userID=email.substring(0,email.indexOf("@"));
        } else {
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("omokRoom");


        final ArrayList<String> roomList=new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomList);
        room_list.setAdapter(adapter);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.add(snapshot.getKey());
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

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomName=edtRoomName.getText().toString();
                if(roomName.equals("")){
                    Toast.makeText(RoomActivity.this,"방 이름 입력하세요",Toast.LENGTH_SHORT).show();
                }else {
                    roomList.add(roomName);
                    adapter.notifyDataSetChanged();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("omokRoom");

                    //RoomInfo wrapping
                    RoomInfoDTO roomInfo = new RoomInfoDTO();
                    roomInfo.setPlayerB(userID);
                    roomInfo.setPlayerW("");
                    roomInfo.setRoomName(roomName);
                    roomInfo.setWaiting(true);
                    roomInfo.setGaming(false);
                    roomInfo.setFinish(false);


                    myRef.child(roomName).child("roomInfo").setValue(roomInfo);
                    myRef.child(roomName).child("gameLog").setValue("");
                    Intent intent=new Intent(getApplicationContext(), GameActivity.class);
                    intent.putExtra("color","black");
                    intent.putExtra("myID",userID);
                    intent.putExtra("ROOMNAME",roomName);
                    startActivity(intent);
                    finish();
                }
            }
        });
        room_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String roomName = (String) adapterView.getAdapter().getItem(i);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("omokRoom");

                DatabaseReference PlayerW = database.getReference("omokRoom/"+roomName+"/roomInfo/playerW");
                DatabaseReference isWating = database.getReference("omokRoom/"+roomName+"/roomInfo/waiting");
                DatabaseReference isGaming = database.getReference("omokRoom/"+roomName+"/roomInfo/gaming");
                PlayerW.setValue(userID);
                isWating.setValue(false);
                isGaming.setValue(true);


                Intent intent=new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("ROOMNAME",roomName);
                intent.putExtra("myID",userID);
                intent.putExtra("color", "white");
                intent.putExtra("ROOMINDEX",i);

                startActivity(intent);
                finish();
            }
        });

    }


}

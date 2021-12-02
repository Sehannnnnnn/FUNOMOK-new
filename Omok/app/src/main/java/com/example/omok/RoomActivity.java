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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class RoomActivity extends AppCompatActivity {

    private EditText edtRoomName;
    private Button btnCreate;
    private ListView room_list;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        edtRoomName=(EditText) findViewById(R.id.edtRoomName);
        btnCreate=(Button) findViewById(R.id.btnCreate);
        room_list=(ListView) findViewById(R.id.room_list);


        final ArrayList<String> roomList=new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomList);
        room_list.setAdapter(adapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomName=edtRoomName.getText().toString();
                if(roomName.equals("")){
                    Toast.makeText(RoomActivity.this,"방 이름 입력하세요",Toast.LENGTH_SHORT).show();
                }else {
                    roomList.add(roomName);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        room_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                databaseReference.child("omokRoom").setValue(roomList.get(i));

                Intent intent=new Intent(RoomActivity.this,GameActivity.class);
                intent.putExtra("roomName",roomList);
                intent.putExtra("roomIndex",i);
                startActivity(intent);
            }
        });

    }


}

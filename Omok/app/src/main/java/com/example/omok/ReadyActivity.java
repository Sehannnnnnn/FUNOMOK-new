package com.example.omok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ReadyActivity extends AppCompatActivity {
    private TextView textUser;
    private Button btnStart,btnSetting,btnEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        textUser=(TextView) findViewById(R.id.textUser);
        btnStart=(Button) findViewById(R.id.btnStart);
        btnSetting=(Button) findViewById(R.id.btnSetting);
        btnEnd=(Button) findViewById(R.id.btnEnd);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReadyActivity.this, RoomActivity.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg= new AlertDialog.Builder(ReadyActivity.this);
                dlg.setTitle("환경 설정: 효과음,배경음 선택");
                dlg.setMessage("여기에 2개 항목 있도록");
                dlg.show();
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}

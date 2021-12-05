package com.example.omok;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReadyActivity extends AppCompatActivity {
    private TextView textUser;
    private Button btnStart,btnSetting,btnEnd;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        textUser=(TextView) findViewById(R.id.textUser);
        btnStart=(Button) findViewById(R.id.btnStart);
        btnSetting=(Button) findViewById(R.id.btnSetting);
        btnEnd=(Button) findViewById(R.id.btnEnd);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String userID=email.substring(0,email.indexOf("@"));
            textUser.setText(userID);
        } else {
        }

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
                final String[] versionArray=new String[] {"음악 재생 o","음악 재생 x"};
                AlertDialog.Builder dlg=new AlertDialog.Builder(ReadyActivity.this);
                dlg.setTitle("음악 재생 o/x");
                dlg.setSingleChoiceItems(versionArray, 3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            Context c = view.getContext();
                            mediaPlayer= MediaPlayer.create(c , R.raw.east_music );
                            mediaPlayer.start();
                        }else{
                            mediaPlayer.stop();
                        }
                    }
                });
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
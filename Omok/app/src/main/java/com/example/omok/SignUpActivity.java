package com.example.omok;



import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText edtEmail;
    private EditText edtPW;
    private EditText edtPWCheck;
    private EditText edtNickname;
    private Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPW = (EditText) findViewById(R.id.edtPW);
        edtPWCheck = (EditText) findViewById(R.id.edtPWCheck);
        edtNickname = (EditText) findViewById(R.id.edtNickname);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String pw = edtPW.getText().toString();
                String pwCheck = edtPWCheck.getText().toString();
                String nickname = edtNickname.getText().toString();

                if (!email.equals("") && !pw.equals("") && !pwCheck.equals("") && !nickname.equals("")) {
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && pw.length()<6){
                        Toast.makeText(SignUpActivity.this, "유효한 이메일과 비밀번호 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();

                    }
                    else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Toast.makeText(SignUpActivity.this, "유효한 이메일을 입력해주세요", Toast.LENGTH_SHORT).show();

                    }else if(pw.length()<6) {
                        Toast.makeText(SignUpActivity.this, "비밀번호 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                    }else{
                        if (pw.equals(pwCheck))
                            createUser(email,pw,nickname);
                        else
                            Toast.makeText(SignUpActivity.this, "비밀번호가 불일치 합니다\n다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this, "공백을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }
}


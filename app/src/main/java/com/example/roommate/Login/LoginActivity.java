package com.example.roommate.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommate.BackKeyHandler;
import com.example.roommate.Friend.GroupActivity;
import com.example.roommate.MainActivity;
import com.example.roommate.NonNull;
import com.example.roommate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button  mLoginBtn;
    private TextView mResigettxt;
    private EditText mEmailText, mPasswordText;
    private FirebaseAuth firebaseAuth;

    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth =  FirebaseAuth.getInstance();
        //버튼 등록하기
        mResigettxt = findViewById(R.id.join_button);
        mLoginBtn = findViewById(R.id.login_button);
        mEmailText = findViewById(R.id.login_email);
        mPasswordText = findViewById(R.id.login_password);

        //가입 버튼이 눌리면
        mResigettxt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //로그인 버튼이 눌리면
        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString().trim();
                String pwd = mPasswordText.getText().toString().trim();
                if(email.equals("")){
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(pwd.equals("")){
                    Toast.makeText(LoginActivity.this,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.signInWithEmailAndPassword(email,pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(LoginActivity.this,"로그인 오류",Toast.LENGTH_SHORT).show();
                                        mPasswordText.setText("");
                                    }
                                }
                            });
                }
            }
        });
    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            String email = currentUser.getEmail();
            Intent intent = new Intent(LoginActivity.this, GroupActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed(){
        backKeyHandler.onBackPressed(5);
    }
}
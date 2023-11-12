package com.example.roommate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.UserManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommate.CustomCalendar.CustomCalendarActivity;
import com.example.roommate.Friend.GroupActivity;
import com.example.roommate.Login.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tx_diary;
    private Button button,bt_friend,button2;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        tx_diary = findViewById(R.id.tx_diary);
        button = findViewById(R.id.button);
        bt_friend = findViewById(R.id.bt_friend);
        button2 = findViewById(R.id.button2);

        FirebaseApp.initializeApp(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                tx_diary.setText(String.format("%d %d %d",year,month+1,day));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        bt_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
            }
        });
        tx_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomCalendarActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onStart() {
        super.onStart();

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(MainActivity.this, GroupActivity.class);
            startActivity(intent);
        }
    }
}
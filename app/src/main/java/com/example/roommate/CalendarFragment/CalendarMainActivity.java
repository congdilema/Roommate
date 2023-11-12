package com.example.roommate.CalendarFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.roommate.CustomCalendar.CustomCalendarFragment;
import com.example.roommate.GroupCalendar.GroupCalendarFragment;
import com.example.roommate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalendarMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment fragment_custom;
    private Fragment fragment_group;

    private String myemail,otheremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);

        fragment_custom = new CustomCalendarFragment();
        fragment_group = new GroupCalendarFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Intent intent = getIntent();
        myemail = intent.getStringExtra("myemail");
        otheremail = intent.getStringExtra("otheremail");

        //번들객체 생성, text값 저장
        Bundle bundle = new Bundle();
        bundle.putString("myemail",myemail);
        bundle.putString("otheremail",otheremail);

        //fragment로 번들 전달
        fragment_group.setArguments(bundle);
        fragment_custom.setArguments(bundle);

        //처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragment_custom).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.customCalendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_custom).commit();
                        break;
                    case R.id.groupCalendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_group).commit();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new GroupCalendarFragment()).commit();
                        break;
                }
                return true;
            }
        });

    }
}
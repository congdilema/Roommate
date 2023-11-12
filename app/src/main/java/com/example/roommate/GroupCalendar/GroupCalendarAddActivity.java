package com.example.roommate.GroupCalendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommate.R;
import com.google.firebase.auth.FirebaseAuth;

public class GroupCalendarAddActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Button ok_button,cancelbutton;
    private String otheremail,calendar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_calendar_add);

        Intent intent1 = getIntent();
        otheremail = intent1.getStringExtra("otheremail");

        datePicker = findViewById(R.id.datePicker);
        ok_button = findViewById(R.id.okButton);
        cancelbutton = findViewById(R.id.cancelButton);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onClick(View view) {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                datePicker.clearFocus();

                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();

                String fmonth,fday;

                if(month<10) {
                    fmonth = "0"+month;
                }
                else{
                    fmonth = ""+month;
                }
                if(day<10){
                    fday = "0"+day;
                }
                else{
                    fday = ""+day;
                }
                calendar = year+fmonth+fday;

                Intent intent = new Intent(GroupCalendarAddActivity.this, TimeAddActivity.class);
                intent.putExtra("date",calendar);
                intent.putExtra("otheremail",otheremail);
                startActivity(intent);
            }
        });
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                GroupCalendarAddActivity.this.onBackPressed();
            }
        });
    }

}
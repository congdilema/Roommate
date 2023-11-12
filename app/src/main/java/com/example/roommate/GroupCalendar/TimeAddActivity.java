package com.example.roommate.GroupCalendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.roommate.R;
import com.example.roommate.Timeline.CustomTimelineActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimeAddActivity extends AppCompatActivity {

    private TimePicker time_picker;
    private TextView tv_time;
    private EditText et_event;
    private Button ok_button,cancelbutton;
    private FirebaseAuth firebaseAuth;

    private String date,otheremail;

    //GroupCalendarActivity groupCalendarActivity = (GroupCalendarActivity) GroupCalendarActivity.groupcalendarActivity;
    GroupCalendarFragment groupCalendarFragment = (GroupCalendarFragment) GroupCalendarFragment.groupcalendarActivity;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_add);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        otheremail = intent.getStringExtra("otheremail");

        time_picker = findViewById(R.id.time_picker);
        tv_time = findViewById(R.id.tv_time);
        et_event = findViewById(R.id.event_title);
        ok_button = findViewById(R.id.okButton);
        cancelbutton = findViewById(R.id.cancelButton);

        FirebaseDatabase database;
        DatabaseReference databaseReference;

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseAuth =  FirebaseAuth.getInstance();

        tv_time.setText(date);

        // TimePicker 클릭 이벤트
        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                tv_time.setText(hour + "시 " + minute + "분 선택");
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onClick(View view) {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                time_picker.clearFocus();

                int thour = time_picker.getHour();
                int tminute = time_picker.getMinute();

                String fhour,fminute;

                if(thour<10) {
                    fhour = "0"+thour;
                }
                else{
                    fhour = ""+thour;
                }
                if(tminute<10){
                    fminute = "0"+tminute;
                }
                else{
                    fminute = ""+tminute;
                }

                int idx = email.indexOf("@");
                String xemail = email.substring(0,idx);
                databaseReference.child("Users").child(xemail).child("calendar").child(date).child(fhour+fminute).setValue(et_event.getText().toString());

                groupCalendarFragment.clear();

                Intent intent = new Intent(TimeAddActivity.this, CustomTimelineActivity.class);
                intent.putExtra("myemail",xemail);
                intent.putExtra("otheremail",otheremail);
                intent.putExtra("date",date);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                TimeAddActivity.this.onBackPressed();
            }
        });

    }
}

package com.example.roommate.Timeline;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.roommate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimeChangeActivity extends AppCompatActivity {

    private TimePicker time_picker;
    private TextView tv_time;
    private EditText et_event;
    private Button ok_button,cancelbutton,deletelButton;
    private FirebaseAuth firebaseAuth;

    private String myemail,otheremail,date;

    CustomTimelineActivity customTimelineActivity = (CustomTimelineActivity) CustomTimelineActivity.customTimelineActivity;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_change);

        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String event = intent.getStringExtra("event");
        myemail = intent.getStringExtra("myemail");
        otheremail = intent.getStringExtra("otheremail");
        date = intent.getStringExtra("date");

        // xml 연결
        time_picker = findViewById(R.id.time_picker);
        tv_time = findViewById(R.id.tv_time);
        et_event = findViewById(R.id.event_title);
        ok_button = findViewById(R.id.okButton);
        cancelbutton = findViewById(R.id.cancelButton);
        deletelButton = findViewById(R.id.deletelButton);

        final String[] timehour = {time.substring(0, 2)};
        final String[] timeminute = {time.substring(2, 4)};

        time_picker.setHour(Integer.parseInt(timehour[0]));
        time_picker.setMinute(Integer.parseInt(timeminute[0]));
        et_event.setText(event);

        FirebaseDatabase database;
        DatabaseReference databaseReference;

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseAuth =  FirebaseAuth.getInstance();

        tv_time.setText(timehour[0] + "시 " + timeminute[0] + "분 선택");

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
                databaseReference.child("Users").child(xemail).child("calendar").child(date).child(time).setValue(null);
                databaseReference.child("Users").child(xemail).child("calendar").child(date).child(fhour+fminute).setValue(et_event.getText().toString());

                customTimelineActivity.clear();
                //customTimelineActivity.TimelineDatabase(xemail,otheremail,date);

                Intent intent = new Intent(TimeChangeActivity.this, CustomTimelineActivity.class);
                intent.putExtra("myemail",myemail);
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
                TimeChangeActivity.this.onBackPressed();
            }
        });
        deletelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Users").child(myemail).child("calendar").child(date).child(time).setValue(null);
                Intent intent = new Intent(TimeChangeActivity.this, CustomTimelineActivity.class);
                intent.putExtra("myemail",myemail);
                intent.putExtra("otheremail",otheremail);
                intent.putExtra("date",date);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}

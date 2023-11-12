package com.example.roommate.Timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.roommate.GroupCalendar.TimeAddActivity;
import com.example.roommate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CustomTimelineActivity extends AppCompatActivity {

    private RecyclerView RecyclerView;
    private CustomTimelineAdapter customTimelineAdapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private androidx.recyclerview.widget.RecyclerView.LayoutManager mLayoutManager;

    public static Object customTimelineActivity;

    private String myemail,otheremail;
    private String date;
    private Button bt_timeadd;

    ArrayList<TimelineList> timelineListsArraylist = new ArrayList<>();
    ArrayList<TimelineList> temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_timeline);

        Intent intent = getIntent();
        myemail = intent.getStringExtra("myemail");
        otheremail = intent.getStringExtra("otheremail");
        date = intent.getStringExtra("date");

        customTimelineActivity = this;
        bt_timeadd = (Button) findViewById(R.id.bt_time_add);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        RecyclerView = (RecyclerView) findViewById(R.id.rv_timeline);

        mLayoutManager = new LinearLayoutManager(CustomTimelineActivity.this);
        RecyclerView.setLayoutManager(mLayoutManager);

        customTimelineAdapter = new CustomTimelineAdapter(CustomTimelineActivity.this,CustomTimelineActivity.this);
        RecyclerView.setAdapter(customTimelineAdapter);

        TimelineDatabase(myemail,otheremail,date);

        bt_timeadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomTimelineActivity.this, TimeAddActivity.class);
                String time = "0000";
                String event = "";
                intent.putExtra("time",time);
                intent.putExtra("event",event);
                intent.putExtra("myemail",myemail);
                intent.putExtra("otheremail",otheremail);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });
    }

    public void bt_test(String time,String event){
        Intent intent = new Intent(CustomTimelineActivity.this, TimeChangeActivity.class);
        intent.putExtra("time",time);
        intent.putExtra("event",event);
        intent.putExtra("myemail",myemail);
        intent.putExtra("otheremail",otheremail);
        intent.putExtra("date",date);
        startActivity(intent);
    }

    public void clear(){
        customTimelineAdapter.clear();
    }

    public void TimelineDatabase(String myemail,String otheremail,String date){
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(myemail).child("calendar").child(date).exists()) {
                    Iterator iterator = dataSnapshot.child(myemail).child("calendar").child(date).getChildren().iterator();
                    ArrayList<String> friendlist = new ArrayList<>();

                    while (iterator.hasNext()) {
                        friendlist.add(((DataSnapshot) iterator.next()).getKey());
                    }
                    for (int a = 0; a<friendlist.size();a++){
                        String push = friendlist.get(a).toString();

                        String time = dataSnapshot.child(myemail).child("calendar").child(date).child(push).getKey().toString();
                        String event = dataSnapshot.child(myemail).child("calendar").child(date).child(push).getValue().toString();

                        timelineListsArraylist.add(new TimelineList(time,event,"myemail"));
                    }
                }
                if (dataSnapshot.child(otheremail).child("calendar").child(date).exists()) {
                    Iterator iterator = dataSnapshot.child(otheremail).child("calendar").child(date).getChildren().iterator();
                    ArrayList<String> friendlist = new ArrayList<>();

                    while (iterator.hasNext()) {
                        friendlist.add(((DataSnapshot) iterator.next()).getKey());
                    }
                    for (int a = 0; a<friendlist.size();a++){
                        String push = friendlist.get(a).toString();

                        String time = dataSnapshot.child(otheremail).child("calendar").child(date).child(push).getKey().toString();
                        String event = dataSnapshot.child(otheremail).child("calendar").child(date).child(push).getValue().toString();

                        timelineListsArraylist.add(new TimelineList(time,event,"otheremail"));
                    }
                }

                temp.add(new TimelineList("","",""));
                if (timelineListsArraylist.size()!=0){
                    for(int i =0;i<timelineListsArraylist.size();i++){
                        for(int j=i+1;j<timelineListsArraylist.size();j++){
                            if(Integer.parseInt(timelineListsArraylist.get(i).getTime())==Integer.parseInt(timelineListsArraylist.get(j).getTime())){
                                timelineListsArraylist.get(i).setOtherevent(timelineListsArraylist.get(j).getEvent());
                                timelineListsArraylist.remove(j);
                                timelineListsArraylist.get(i).setEmail("sameemail");
                            }
                            else if(Integer.parseInt(timelineListsArraylist.get(i).getTime())>Integer.parseInt(timelineListsArraylist.get(j).getTime())){
                                temp.get(0).setTime(timelineListsArraylist.get(i).getTime());
                                temp.get(0).setEvent(timelineListsArraylist.get(i).getEvent());
                                temp.get(0).setEmail(timelineListsArraylist.get(i).getEmail());
                                timelineListsArraylist.get(i).setTime(timelineListsArraylist.get(j).getTime());
                                timelineListsArraylist.get(i).setEvent(timelineListsArraylist.get(j).getEvent());
                                timelineListsArraylist.get(i).setEmail(timelineListsArraylist.get(j).getEmail());
                                timelineListsArraylist.get(j).setTime(temp.get(0).getTime());
                                timelineListsArraylist.get(j).setEvent(temp.get(0).getEvent());
                                timelineListsArraylist.get(j).setEmail(temp.get(0).getEmail());
                            }
                        }
                    }
                }
                for(int i =0;i<timelineListsArraylist.size();i++){
                    if(timelineListsArraylist.get(i).getEmail().equals("myemail")){
                        customTimelineAdapter.addItem(new TimelineList(timelineListsArraylist.get(i).getTime(),timelineListsArraylist.get(i).getEvent(),"myemail"));
                    }
                    else if(timelineListsArraylist.get(i).getEmail().equals("otheremail")){
                        customTimelineAdapter.addItem(new TimelineList(timelineListsArraylist.get(i).getTime(),timelineListsArraylist.get(i).getEvent(),"otheremail"));
                    }
                    else if(timelineListsArraylist.get(i).getEmail().equals("sameemail")){
                        customTimelineAdapter.addItem(new TimelineList(timelineListsArraylist.get(i).getTime(),timelineListsArraylist.get(i).getEvent(),timelineListsArraylist.get(i).getOtherevent(),"sameemail"));
                    }
                }
                customTimelineAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }
//    @Override
//    public void onBackPressed(){
//        Intent intent = new Intent(CustomTimelineActivity.this,GroupCalendarActivity.class);
//        intent.putExtra("myemail",myemail);
//        intent.putExtra("otheremail",otheremail);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
}
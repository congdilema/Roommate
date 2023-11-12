package com.example.roommate.GroupCalendar;

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

import com.example.roommate.R;
import com.example.roommate.Timeline.CustomTimelineActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GroupCalendarActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private GroupCalendarAdapter groupCalendarAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mylist;
    private List<String> otherlist;
    private String myemail;
    private String otheremail;

    private Button bt_sort;

    public static Object groupcalendarActivity;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_calendar);

        Intent intent = getIntent();
        myemail = intent.getStringExtra("myemail");
        otheremail = intent.getStringExtra("otheremail");

        groupcalendarActivity = this;

        recyclerView = findViewById(R.id.recycler_view);
        bt_sort = (Button) findViewById(R.id.bt_sort);

        mylist = new ArrayList<>();
        otherlist = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(GroupCalendarActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        groupCalendarAdapter = new GroupCalendarAdapter(GroupCalendarActivity.this,GroupCalendarActivity.this);
        recyclerView.setAdapter(groupCalendarAdapter);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        bt_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(GroupCalendarActivity.this, GroupCalendarAddActivity.class);
                intent1.putExtra("otheremail",otheremail);
                startActivity(intent1);
            }
        });


        databaseReference.child("Users").child(myemail).child("calendar").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator iterator = dataSnapshot.getChildren().iterator();
                    ArrayList<String> datalist = new ArrayList<>();

                    while (iterator.hasNext()) {
                        datalist.add(((DataSnapshot) iterator.next()).getKey());
                    }
                    for (int a = 0; a<datalist.size();a++){
                        String date = datalist.get(a).toString();
                        int time = Integer.parseInt(date);
                        mylist.add(date);
                    }
                    //groupCalendarAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        databaseReference.child("Users").child(otheremail).child("calendar").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator iterator = dataSnapshot.getChildren().iterator();
                    ArrayList<String> datalist = new ArrayList<>();

                    while (iterator.hasNext()) {
                        datalist.add(((DataSnapshot) iterator.next()).getKey());
                    }
                    for (int a = 0; a<datalist.size();a++){
                        String date = datalist.get(a).toString();
                        int time = Integer.parseInt(date);
                        otherlist.add(date);
                        //groupCalendarAdapter.addItem(new GroupCalendarList(date,time));
                    }
                    Set<String> set = new LinkedHashSet<>(mylist);
                    set.addAll(otherlist);
                    List <String> totallist = new ArrayList<>(set);

                    for(int a =0;a<totallist.size();a++){
                        groupCalendarAdapter.addItem(new GroupCalendarList(totallist.get(a)));
                    }
                    groupCalendarAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

    public void bt_customtime(String Date){
        Intent intent = new Intent(GroupCalendarActivity.this, CustomTimelineActivity.class);
        intent.putExtra("myemail",myemail);
        intent.putExtra("otheremail",otheremail);
        intent.putExtra("date",Date);
        startActivity(intent);
    }

    public void clear(){
        groupCalendarAdapter.clear();
    }


}

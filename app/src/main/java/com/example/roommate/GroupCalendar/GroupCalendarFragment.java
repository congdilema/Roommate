package com.example.roommate.GroupCalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class GroupCalendarFragment extends Fragment {

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

    private ViewGroup viewGroup;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_group_calendar,container,false);

//        Intent intent = getIntent();
//        myemail = intent.getStringExtra("myemail");
//        otheremail = intent.getStringExtra("otheremail");

        //RequestActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();
        //번들 안의 텍스트 불러오기
        myemail = bundle.getString("myemail");
        otheremail = bundle.getString("otheremail");

        groupcalendarActivity = this;

        recyclerView = viewGroup.findViewById(R.id.recycler_view);
        bt_sort = (Button) viewGroup.findViewById(R.id.bt_sort);

        mylist = new ArrayList<>();
        otherlist = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        groupCalendarAdapter = new GroupCalendarAdapter(getContext(),getActivity());
        recyclerView.setAdapter(groupCalendarAdapter);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        bt_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(),GroupCalendarAddActivity.class);
                intent1.putExtra("otheremail",otheremail);
                startActivity(intent1);
            }
        });

        if(!myemail.equals("")){
            calendar();
        }else{
            Toast.makeText(getContext(), "이메일 계정 오류", Toast.LENGTH_SHORT).show();
        }

        return viewGroup;

    }
    public void bt_customtime(String Date){
        Intent intent = new Intent(getActivity(), CustomTimelineActivity.class);
        intent.putExtra("myemail",myemail);
        intent.putExtra("otheremail",otheremail);
        intent.putExtra("date",Date);
        startActivity(intent);
    }

    public void calendar(){
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
                }
                else{
                    for(int a=0;a<mylist.size();a++){
                        groupCalendarAdapter.addItem(new GroupCalendarList(mylist.get(a)));
                    }
                }
                groupCalendarAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

    public void clear(){
        groupCalendarAdapter.clear();
    }
}

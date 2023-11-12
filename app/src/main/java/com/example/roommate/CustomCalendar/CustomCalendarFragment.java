package com.example.roommate.CustomCalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;
import com.example.roommate.Timeline.CustomTimelineActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CustomCalendarFragment extends Fragment {

    private TextView monthYearText;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private List<String> mylist;
    private List<String> otherlist;
    private String email;

    private CustomCalendarAdapter customCalendarAdapter;

    private String myemail;
    private String otheremail;

    public static Object customCalendarActivity;

    private ViewGroup viewGroup;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_custom_calendar,container,false);

        monthYearText = viewGroup.findViewById(R.id.monthYearText);
        ImageButton prevBtn = viewGroup.findViewById(R.id.pre_btn);
        ImageButton nextBtn = viewGroup.findViewById(R.id.next_btn);
        recyclerView = viewGroup.findViewById(R.id.rv_calendar);

        mylist = new ArrayList<>();
        otherlist = new ArrayList<>();

        customCalendarActivity = this;

        //RequestActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();
        //번들 안의 텍스트 불러오기
        myemail = bundle.getString("myemail");
        otheremail = bundle.getString("otheremail");

        CalendarUnit.selectDate = Calendar.getInstance();
        CalendarUnit.today = LocalDate.now();

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        email = currentUser.getEmail();

        setMonthView();

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지난달추가
                CalendarUnit.selectDate.add(Calendar.MONTH,-1);
                setMonthView();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다음달추가
                CalendarUnit.selectDate.add(Calendar.MONTH,1);
                setMonthView();
            }
        });

        return viewGroup;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(Calendar calendar){

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;

        String MonthYear = month+"월"+year;

        return MonthYear;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String yearMonthFromDate(Calendar calendar){

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;

        String MonthYear = year+"년"+month;

        return MonthYear;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView(){

        monthYearText.setText(monthYearFromDate(CalendarUnit.selectDate));
        ArrayList<Date> dayList = daysInMonthArray();

        customCalendarAdapter = new CustomCalendarAdapter(dayList,getContext());

        CheckDay(myemail,otheremail);
        //열 7개
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(),7);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(customCalendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Date> daysInMonthArray(){

        ArrayList<Date> dayList = new ArrayList<>();
        //날짜 복사해서 변수 생성
        Calendar monthCalendar = (Calendar) CalendarUnit.selectDate.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        //1일로 셋팅(4월 1일)
        int firstDayofMonth  = monthCalendar.get(Calendar.DAY_OF_WEEK) -1;
        //날짜 셋팅(-5일전)
        monthCalendar.add(Calendar.DAY_OF_MONTH,-firstDayofMonth);
        //42전까지 반복
        while (dayList.size()<42){
            dayList.add(monthCalendar.getTime());
            //1일씩 늘린 날짜로 변경 1일->2일->3일
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }
        return dayList;
    }

    public void CheckDay(String myemail,String otheremail){
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(myemail).child("calendar").exists()) {
                    Iterator iterator = dataSnapshot.child(myemail).child("calendar").getChildren().iterator();
                    ArrayList<String> datalist = new ArrayList<>();

                    while (iterator.hasNext()) {
                        datalist.add(((DataSnapshot) iterator.next()).getKey());
                    }
                    for (int a = 0; a<datalist.size();a++){
                        String date = datalist.get(a).toString();
                        mylist.add(date);
                    }
                    for(int a=0;a<mylist.size();a++){
                        if(dataSnapshot.child(myemail).child("calendar").child(mylist.get(a)).exists()){
                            Iterator iterator1 = dataSnapshot.child(myemail).child("calendar").child(mylist.get(a)).getChildren().iterator();
                            ArrayList<String> datalist1 = new ArrayList<>();
                            while (iterator1.hasNext()) {
                                datalist1.add(((DataSnapshot) iterator1.next()).getKey());
                            }
                            customCalendarAdapter.addItem(new CalendarGroupList(mylist.get(a),datalist1.size(),"myschedule"));
                        }
                    }
                }

                if(dataSnapshot.child(otheremail).child("calendar").exists()){
                    Iterator iterator = dataSnapshot.child(otheremail).child("calendar").getChildren().iterator();
                    ArrayList<String> datalist = new ArrayList<>();

                    while (iterator.hasNext()) {
                        datalist.add(((DataSnapshot) iterator.next()).getKey());
                    }
                    for (int a = 0; a<datalist.size();a++){
                        String date = datalist.get(a).toString();
                        otherlist.add(date);
                    }
                    for(int a=0;a<otherlist.size();a++){
                        if(dataSnapshot.child(otheremail).child("calendar").child(otherlist.get(a)).exists()){
                            Iterator iterator1 = dataSnapshot.child(otheremail).child("calendar").child(otherlist.get(a)).getChildren().iterator();
                            ArrayList<String> datalist1 = new ArrayList<>();
                            while (iterator1.hasNext()){
                                datalist1.add(((DataSnapshot) iterator1.next()).getKey());
                            }
                            Integer otherschedule = datalist1.size();
                            customCalendarAdapter.addItem(new CalendarGroupList(otherlist.get(a),otherschedule,"otherschedule"));
                        }
                    }

                }
                customCalendarAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }
    public void bt_customtime(String Date){
        Intent intent = new Intent(getActivity(), CustomTimelineActivity.class);
        intent.putExtra("myemail",myemail);
        intent.putExtra("otheremail",otheremail);
        intent.putExtra("date",Date);
        startActivity(intent);
    }
}


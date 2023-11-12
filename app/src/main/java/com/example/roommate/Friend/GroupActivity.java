package com.example.roommate.Friend;

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
import android.widget.Toast;

import com.example.roommate.BackKeyHandler;
import com.example.roommate.CalendarFragment.CalendarMainActivity;
import com.example.roommate.MainActivity;
import com.example.roommate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class GroupActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private GroupListAdapter groupListAdapter;
    private RecyclerView recyclerView;
    private ArrayList<GroupList> arrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button bt_group,bt_logout;

    public static Object groupActivity;
    private FirebaseAuth firebaseAuth;

    private String myemail,otheremail,email;
    private FirebaseUser currentUser;

    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupActivity = this;

        recyclerView = findViewById(R.id.rv_group);

        mLayoutManager = new LinearLayoutManager(GroupActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        groupListAdapter = new GroupListAdapter(GroupActivity.this,GroupActivity.this);
        recyclerView.setAdapter(groupListAdapter);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(); // DB 테이블 연결

        firebaseAuth =  FirebaseAuth.getInstance(); //auth연결

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        email = currentUser.getEmail();

        bt_group = findViewById(R.id.bt_findfriend);
        bt_logout = findViewById(R.id.bt_logout);

        bt_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this,FriendActivity.class);
                startActivity(intent);
            }
        });
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(GroupActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        CheckGroup(email);
    }

    public void bt_timeline(String otheremail){
        Intent intent = new Intent(GroupActivity.this, CalendarMainActivity.class);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String email = currentUser.getEmail();
        int idx = email.indexOf("@");
        String xemail = email.substring(0,idx);
        int idx2 = otheremail.indexOf("@");
        String xotheremail = otheremail.substring(0,idx2);
        intent.putExtra("myemail",xemail);
        intent.putExtra("otheremail",xotheremail);
        startActivity(intent);
    }

    public void CheckGroup(String currentUser){
        databaseReference.child("Group").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator iterator = dataSnapshot.getChildren().iterator();
                    ArrayList<String> grouplist = new ArrayList<>();

                    while (iterator.hasNext()){
                        grouplist.add(((DataSnapshot) iterator.next()).getKey());
                    }
                    for (int a = 0; a<grouplist.size();a++){
                        String push = grouplist.get(a).toString();

                        myemail = dataSnapshot.child(push).child("grouplist").child("myemail").getValue().toString();
                        otheremail = dataSnapshot.child(push).child("grouplist").child("otheremail").getValue().toString();

                        if(myemail.equals(currentUser)){
                            String groupname = dataSnapshot.child(push).child("groupname").getValue().toString();
                            groupListAdapter.addItem(new GroupList(groupname,myemail,otheremail));
                        }
                    }
                }
                groupListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        backKeyHandler.onBackPressed(5);
    }

}

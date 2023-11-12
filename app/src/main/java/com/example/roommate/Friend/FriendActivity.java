package com.example.roommate.Friend;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.Dialog.FaddDialog;
import com.example.roommate.Login.LoginActivity;
import com.example.roommate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;import java.util.Iterator;

public class FriendActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FriendListAdapter friendListAdapter;
    private RecyclerView recyclerView;
    private ArrayList<FriendList> arrayList;
    private RecyclerView.LayoutManager mLayoutManager;

    public static Object FriendActivity;

    private String currentemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        recyclerView = findViewById(R.id.recycler_view);

        FriendActivity = this;

        mLayoutManager = new LinearLayoutManager(FriendActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        friendListAdapter = new FriendListAdapter(FriendActivity.this,FriendActivity.this);
        recyclerView.setAdapter(friendListAdapter);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //auth연결

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentemail = currentUser.getEmail();

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Users"); // DB 테이블 연결

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterator iterator = dataSnapshot.getChildren().iterator();
                    ArrayList<String> friendlist = new ArrayList<>();

                    while (iterator.hasNext()) {
                        friendlist.add(((DataSnapshot) iterator.next()).getKey());
                    }
                    for (int a = 0; a<friendlist.size();a++){
                        String push = friendlist.get(a).toString();
                        String email = dataSnapshot.child(push).child("email").getValue().toString();
                        String friendname = dataSnapshot.child(push).child("name").getValue().toString();

                        if(!email.equals(currentemail)){
                            friendListAdapter.addItem(new FriendList(email,friendname));
                        }
                    }
                    friendListAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }
    public void bt_fradd(String friend_name,String friend_email){
        FaddDialog faddDialog = new FaddDialog(FriendActivity.this);
        faddDialog.callFunction(friend_name,friend_email);
    }

    public void bt_okbutton(){
        Intent intent = new Intent(FriendActivity.this,GroupActivity.class);
        startActivity(intent);
    }

    public void bt_login(){
        Intent intent = new Intent(FriendActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}

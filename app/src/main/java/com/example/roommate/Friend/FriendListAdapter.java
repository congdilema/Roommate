package com.example.roommate.Friend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.CustomViewHolder>{

    private ArrayList<FriendList> items = new ArrayList<FriendList>();
    private Context context;
    private Activity activity;
    private FriendListAdapter FriendListAdapter;

    FriendActivity friendActivity = (FriendActivity) FriendActivity.FriendActivity;

    public FriendListAdapter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void addItem(FriendList data){
        items.add(data);
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public FriendListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlistid_item, parent, false);
        FriendListAdapter.CustomViewHolder holder = new FriendListAdapter.CustomViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.onBind(items.get(position));

        FriendListAdapter = new FriendListAdapter(context, activity);

        holder.bt_friendadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friend_name = holder.friend_name.getText().toString();
                String friend_email = holder.friend_email.getText().toString();
                friendActivity.bt_fradd(friend_name,friend_email);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView friend_name;
        TextView friend_email;
        Button bt_friendadd;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.friend_name = itemView.findViewById(R.id.friendlist_name);
            this.friend_email = itemView.findViewById(R.id.friendlist_email);
            this.bt_friendadd = itemView.findViewById(R.id.bt_friendadd);
        }
        @SuppressLint("SetTextI18n")
        public void onBind(FriendList data){
            friend_name.setText(data.getFriendname());
            friend_email.setText(data.getEmail());
        }
    }

}

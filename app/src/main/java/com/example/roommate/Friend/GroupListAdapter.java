package com.example.roommate.Friend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.CustomViewHolder>{

    private ArrayList<GroupList> items = new ArrayList<GroupList>();
    private Context context;
    private Activity activity;
    private GroupListAdapter GroupListAdapter;

    GroupActivity groupActivity = (GroupActivity) GroupActivity.groupActivity;

    public GroupListAdapter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public GroupListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grouplistid_item, parent, false);
        GroupListAdapter.CustomViewHolder holder = new GroupListAdapter.CustomViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.onBind(items.get(position));

        GroupListAdapter = new GroupListAdapter(context, activity);

        holder.ll_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otheremail = items.get(position).getOtheremail();
                groupActivity.bt_timeline(otheremail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    public void addItem(GroupList data){
        items.add(data);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView groupname;
        TextView myemail;
        TextView otheremail;
        LinearLayout ll_group;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.groupname = itemView.findViewById(R.id.groupname);
            this.myemail = itemView.findViewById(R.id.myemail);
            this.otheremail = itemView.findViewById(R.id.otheremail);
            this.ll_group = itemView.findViewById(R.id.ll_group);
        }
        @SuppressLint("SetTextI18n")
        public void onBind(GroupList data){
            groupname.setText(data.getGroupname());
            myemail.setText(data.getMyemail());
            otheremail.setText(data.getOtheremail());
        }
    }
}

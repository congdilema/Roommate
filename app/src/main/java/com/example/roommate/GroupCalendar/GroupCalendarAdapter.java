package com.example.roommate.GroupCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GroupCalendarAdapter extends RecyclerView.Adapter<GroupCalendarAdapter.CustomViewHolder>{

    private ArrayList<GroupCalendarList> items = new ArrayList<GroupCalendarList>();
    private Context context;
    private Activity activity;
    private GroupCalendarAdapter GroupCalendarAdapter;

    GroupCalendarActivity groupcalendarActivity = (GroupCalendarActivity) GroupCalendarActivity.groupcalendarActivity;
    GroupCalendarFragment groupCalendarFragment = (GroupCalendarFragment) GroupCalendarFragment.groupcalendarActivity;

    public GroupCalendarAdapter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public GroupCalendarAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupcalendar, parent, false);
        GroupCalendarAdapter.CustomViewHolder holder = new GroupCalendarAdapter.CustomViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GroupCalendarAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.onBind(items.get(position));

        GroupCalendarAdapter = new GroupCalendarAdapter(context, activity);

        Comparator<GroupCalendarList> sort = new Comparator<GroupCalendarList>() {
            @Override
            public int compare(GroupCalendarList a1, GroupCalendarList a2) {
                int ret = 0;
                if(Integer.parseInt(a1.getDate())<Integer.parseInt(a2.getDate())){
                    ret = -1;
                }
                else if(Integer.parseInt(a1.getDate())==Integer.parseInt(a2.getDate())){
                    ret = 0;
                }
                else if(Integer.parseInt(a1.getDate())>Integer.parseInt(a2.getDate())){
                    ret = 1;
                }
                return ret;
            }
        };
        Collections.sort(items,sort);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //groupcalendarActivity.bt_customtime(items.get(position).getDate());
                groupCalendarFragment.bt_customtime(items.get(position).getDate());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    public void addItem(GroupCalendarList data){
        items.add(data);
    }

    public void clear(){items.clear();}

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.tx_groupcalendar);
        }
        @SuppressLint("SetTextI18n")
        public void onBind(GroupCalendarList data){
            textView.setText(data.getDate());
        }
    }
}

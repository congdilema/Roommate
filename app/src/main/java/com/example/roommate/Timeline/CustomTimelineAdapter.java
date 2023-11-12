package com.example.roommate.Timeline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;

public class CustomTimelineAdapter extends RecyclerView.Adapter<CustomTimelineAdapter.CustomViewHolder>{

    public ArrayList<TimelineList> items = new ArrayList<TimelineList>();
    private Context context;
    private Activity activity;
    private CustomTimelineAdapter CustomTimelineAdapter;

    CustomTimelineActivity customTimelineActivity = (CustomTimelineActivity) CustomTimelineActivity.customTimelineActivity;

    int[] colors = {0xffFFAD6C, 0xff62f434, 0xffdeda78, 0xff7EDCFF, 0xff58fdea, 0xfffdc75f};//颜色组

    public CustomTimelineAdapter(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomTimelineAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        CustomTimelineAdapter.CustomViewHolder holder = new CustomTimelineAdapter.CustomViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomTimelineAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.onBind(items.get(position));

        CustomTimelineAdapter = new CustomTimelineAdapter(context, activity);

        holder.mytime.setTextColor(colors[position % colors.length]);
        holder.othertime.setTextColor(colors[position % colors.length]);

        holder.dot_other.setVisibility(View.INVISIBLE);

        if(items.get(position).getEmail().equals("otheremail")){
            holder.dot_my.setVisibility(View.INVISIBLE);
            holder.dot_other.setVisibility(View.VISIBLE);
            holder.ll_other.setVisibility(View.VISIBLE);
            holder.ll_my.setVisibility(View.INVISIBLE);
        }

        holder.ll_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = items.get(position).getTime();
                String event = items.get(position).getEvent();
                customTimelineActivity.bt_test(time,event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    public void addItem(TimelineList data){
        items.add(data);
    }

    public void clear(){
        items.clear();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView mytime,othertime,mytextView,othertextview;
        private LinearLayout ll_my,ll_other;
        private View dot_other,dot_my;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mytime = (TextView) itemView.findViewById(R.id.tx_my_time);
            othertime = (TextView) itemView.findViewById(R.id.tx_other_time);
            mytextView = (TextView) itemView.findViewById(R.id.tx_my_str);
            othertextview = (TextView) itemView.findViewById(R.id.tx_other_str);
            ll_my = (LinearLayout) itemView.findViewById(R.id.ll_my);
            ll_other = (LinearLayout) itemView.findViewById(R.id.ll_other);
            dot_my = (View) itemView.findViewById(R.id.dot_my);
            dot_other = (View) itemView.findViewById(R.id.dot_other);
        }
        @SuppressLint("SetTextI18n")
        public void onBind(TimelineList data){
            String email = data.getEmail();

            if(email.equals("myemail")){
                mytime.setText(String.valueOf(data.getTime()));
                mytextView.setText(data.getEvent());
            }
            else if(email.equals("otheremail")){
                othertime.setText(String.valueOf(data.getTime()));
                othertextview.setText(data.getEvent());
            }
            else{
                ll_other.setVisibility(View.VISIBLE);
                dot_other.setVisibility(View.VISIBLE);
                mytime.setText(String.valueOf(data.getTime()));
                mytextView.setText(data.getEvent());
                othertime.setText(String.valueOf(data.getTime()));
                othertextview.setText(data.getOtherevent());
            }
        }
    }
}
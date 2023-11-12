package com.example.roommate.CustomCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommate.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomCalendarAdapter extends RecyclerView.Adapter<CustomCalendarAdapter.CalendarViewHolder> {

    private ArrayList<Date> dayList;
    private Context context;
    private ArrayList<CalendarGroupList> items = new ArrayList<CalendarGroupList>();

    CustomCalendarActivity customCalendarActivity = (CustomCalendarActivity) CustomCalendarActivity.customCalendarActivity;
    CustomCalendarFragment customCalendarFragment = (CustomCalendarFragment) CustomCalendarFragment.customCalendarActivity;

    public CustomCalendarAdapter(ArrayList<Date> dayList,Context context){
        this.dayList = dayList;
        this.context = context;
    }

    public CustomCalendarAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public CustomCalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell,parent,false);
        return new CalendarViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Date monthDate = dayList.get(position);
        Calendar datecalendar = Calendar.getInstance();
        datecalendar.setTime(monthDate);

        int currentday = CalendarUnit.selectDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = CalendarUnit.selectDate.get(Calendar.MONTH)+1;
        int currentYear = CalendarUnit.selectDate.get(Calendar.YEAR);

        int displayDay = datecalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = datecalendar.get(Calendar.MONTH)+1;
        int displayYear = datecalendar.get(Calendar.YEAR);

        if(displayMonth == currentMonth && displayYear ==currentYear){
            holder.parentView.setBackgroundColor(Color.parseColor("#F0FFF0"));
            holder.itemView.setBackgroundColor(Color.parseColor("#F0FFF0"));
        }else{
            holder.parentView.setBackgroundColor(Color.WHITE);
        }

        int dayNo = datecalendar.get(Calendar.DAY_OF_MONTH);
        holder.dayText.setText(String.valueOf(dayNo));

        String curM = ""+displayMonth;
        String curD = ""+displayDay;
        if(displayMonth<10){
            curM = "0"+displayMonth;
        }else if(displayDay<10){
            curD = "0"+displayDay;
        }
        String curr = displayYear+"-"+curM+"-"+curD;
        String schedule = displayYear+""+curM+""+curD;

        if(curr.equals(String.valueOf(CalendarUnit.today))){
            holder.parentView.setBackgroundColor(Color.LTGRAY);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }

        if((position+1)%7==0){
            holder.dayText.setTextColor(Color.BLUE);
        }else if(position==0||(position%7)==0){
            holder.dayText.setTextColor(Color.RED);
        }

        holder.myshedule.setText("");
        holder.otherschedule.setText("");

        if(items.size()!=0){
            for(int a=0;a<items.size();a++){
                if(schedule.equals(items.get(a).getDate())){
                    if(items.get(a).getPerson().equals("myschedule")){
                        holder.myshedule.setText(""+items.get(a).getSchedule());
                        holder.myshedule.setBackgroundColor(Color.parseColor("#ffff00"));
                    }
                    if(items.get(a).getPerson().equals("otherschedule")){
                        holder.otherschedule.setText(""+items.get(a).getSchedule());
                        holder.otherschedule.setBackgroundColor(Color.parseColor("#dce4f0"));
                    }
                }
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sccehck = false;
                for(int a=0;a<items.size();a++){
                    if(schedule.equals(items.get(a).getDate())){
                        //customCalendarActivity.bt_customtime(schedule);
                        customCalendarFragment.bt_customtime(schedule);
                        sccehck=true;
                        break;
                    }
                }
                if(!sccehck){
                    Toast.makeText(context,"해당되는 날짜에는 일정이 없습니다.",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public void addItem(CalendarGroupList data){
        items.add(data);
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView dayText,myshedule,otherschedule;
        LinearLayout parentView;

        public CalendarViewHolder(@NonNull View itemView){
            super(itemView);

            dayText = itemView.findViewById(R.id.dayText);
            parentView = itemView.findViewById(R.id.parentView);
            myshedule = itemView.findViewById(R.id.mySchedule);
            otherschedule = itemView.findViewById(R.id.otherSchedule);

        }
        public void onBind(CalendarGroupList calendarGroupList){
            myshedule.setText(calendarGroupList.date);
        }
    }
}

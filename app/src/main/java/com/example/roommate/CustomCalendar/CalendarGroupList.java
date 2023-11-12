package com.example.roommate.CustomCalendar;

public class CalendarGroupList {

    String groupname;
    String otheremail;
    String date;
    String person;

    Integer schedule;

    CalendarGroupList(String date){
        this.date = date;
    }

    CalendarGroupList(String date,Integer schedule,String person){
        this.date = date;
        this.schedule = schedule;
        this.person = person;
    }


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getOtheremail() {
        return otheremail;
    }

    public void setOtheremail(String otheremail) {
        this.otheremail = otheremail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Integer getSchedule() {
        return schedule;
    }

    public void setSchedule(Integer schedule) {
        this.schedule = schedule;
    }



}

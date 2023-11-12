package com.example.roommate.Timeline;

public class TimelineList {

    String time;
    String event;
    String email;
    String otherevent;

    public TimelineList(String time, String event, String email){
        this.time = time;
        this.event = event;
        this.email = email;
    }

    public TimelineList(String times, String events, String otherevent, String email) {
        this.time = times;
        this.event = events;
        this.otherevent = otherevent;
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtherevent() {
        return otherevent;
    }

    public void setOtherevent(String otherevent) {
        this.otherevent = otherevent;
    }

}

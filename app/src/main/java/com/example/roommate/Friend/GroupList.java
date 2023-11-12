package com.example.roommate.Friend;

public class GroupList {
    private String groupname;
    private String myemail;
    private String otheremail;

    public GroupList(String myemail,String otheremail){
        this.myemail = myemail;
        this.otheremail = otheremail;
    }

    public GroupList(String groupname,String myemail,String otheremail){
        this.groupname = groupname;
        this.myemail = myemail;
        this.otheremail = otheremail;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getMyemail() {
        return myemail;
    }

    public String getOtheremail() {
        return otheremail;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void setMyemail(String myemail) {
        this.myemail = myemail;
    }

    public void setOtheremail(String otheremail) {
        this.otheremail = otheremail;
    }
}

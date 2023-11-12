package com.example.roommate.Friend;

public class FriendList {

    private String friendname;
    private String email;

    public FriendList(String email,String friendname){
        this.email = email;
        this.friendname = friendname;
    }

    public String getEmail() {
        return email;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }
}

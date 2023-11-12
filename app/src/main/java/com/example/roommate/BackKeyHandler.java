package com.example.roommate;

import android.app.Activity;
import android.widget.Toast;

public class BackKeyHandler {

    private long backKeyPressedTime = 0;
    private Activity activity;
    private Toast toast;

    public BackKeyHandler(Activity activity) {
        this.activity = activity;
    }

    private void showGuide(){
        toast = Toast.makeText(activity,"한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onBackPressed(double time) {
        if (System.currentTimeMillis() > backKeyPressedTime + (time * 1000)) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.moveTaskToBack(true);
            activity.finishAndRemoveTask();
            toast.cancel();
        }
    }
}


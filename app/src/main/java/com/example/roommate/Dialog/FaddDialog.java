package com.example.roommate.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommate.Friend.FriendActivity;
import com.example.roommate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FaddDialog {

    private Context context;

    public FaddDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    @SuppressLint("SetTextI18n")
    public void callFunction(String friend_name,String friend_email) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_fadd);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView textView = (TextView) dlg.findViewById(R.id.fadd_dialog_title);
        final EditText message = (EditText) dlg.findViewById(R.id.fadd_dialog_mesgase);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);

        FriendActivity friendActivity = (FriendActivity) FriendActivity.FriendActivity;

        FirebaseDatabase database;
        DatabaseReference databaseReference;

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        textView.setText(friend_name+"와(과) 만들 그룹이름을 작성해주세요.");

        //계정확인
        FirebaseAuth firebaseAuth;
        firebaseAuth =  FirebaseAuth.getInstance();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                if(message.getText().toString().equals("")){
                    Toast.makeText(context,"값을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    String email = null;
                    String otheremail = friend_email;
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if(currentUser != null){
                        email = currentUser.getEmail();

                        String push = FirebaseDatabase.getInstance().getReference().push().getKey();
                        databaseReference.child("Group").child(push).child("groupname").setValue(message.getText().toString());
                        databaseReference.child("Group").child(push).child("grouplist").child("myemail").setValue(email);
                        databaseReference.child("Group").child(push).child("grouplist").child("otheremail").setValue(otheremail);

                        Toast.makeText(context, message.getText().toString() + "이 추가되었습니다.", Toast.LENGTH_SHORT).show();

                        // 커스텀 다이얼로그를 종료한다.
                        dlg.dismiss();

                        friendActivity.bt_okbutton();
                    }
                    else{
                        Toast.makeText(context, "현재 로그인이 되어 있지 않습니다. 먼저 로그인부터 해주세요.", Toast.LENGTH_SHORT).show();
                        friendActivity.bt_login();
                    }
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}

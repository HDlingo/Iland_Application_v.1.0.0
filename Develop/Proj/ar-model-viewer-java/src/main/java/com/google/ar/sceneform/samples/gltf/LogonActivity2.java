package com.google.ar.sceneform.samples.gltf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LogonActivity2 extends AppCompatActivity {


    ImageButton button_finish;
    ImageButton button_remove_phone_number;

    EditText editText_phone_number;
    EditText editText_identifying_code;

    String nick_name_text;
    String account_text;
    String password_text;

    UserDao userDao;

    private static final String TAG = "LogonActivity2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //use this to activate full screen
        setContentView(R.layout.activity_logon2);

        //get the message from last step
        Intent intent=getIntent();
        Bundle msg_last_step=intent.getExtras();
        nick_name_text=msg_last_step.getString("nickname");
        account_text=msg_last_step.getString("account");
        password_text=msg_last_step.getString("password");

//        //print out the messages
//        Log.d(TAG, String.format("nickname is %s",nick_name_text));
//        Log.d(TAG, String.format("account is %s",account_text));
//        Log.d(TAG, String.format("password is %s",password_text));

        //init user dao
        userDao = new UserDao(this);
        //find the buttons
        button_finish=findViewById(R.id.zhuce2_finish);
        button_remove_phone_number=findViewById(R.id.zhuce2_delete_phone_number);

        //find the edits
        editText_phone_number=findViewById(R.id.logon2_edit_phone_number);
        editText_identifying_code=findViewById(R.id.logon2_edit_identifying_code);

        //configure finish button
        /* button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogonActivity2.this, MainPageActive.class); //switch to main activity
                startActivity(intent);
                LogonActivity2.this.finish();
            }
        });*/

        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = account_text;
                String password = password_text;
                UserBean userBean = userDao.querryUser1(username,password);
                if(TextUtils.isEmpty(userBean.getUsername())){
                    boolean flag = userDao.insertUser(username,password);
                    if(flag){
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(LogonActivity2.this, MainPageActive.class); //switch to main activity
                    startActivity(intent);
                    LogonActivity2.this.finish();
                }else {
                    Toast.makeText(getApplicationContext(),"该账号已被注册",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //configure delete phone number button
        button_remove_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText_phone_number.setText("");
            }
        });

    }
}
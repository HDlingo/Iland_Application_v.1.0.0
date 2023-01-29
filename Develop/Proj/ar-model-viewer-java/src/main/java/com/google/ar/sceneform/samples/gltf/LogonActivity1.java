package com.google.ar.sceneform.samples.gltf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

public class LogonActivity1 extends AppCompatActivity {

    EditText nickname_text;
    EditText password_text;
    EditText account_text;
    ImageButton password_readable;
    ImageButton next_step_button;
    boolean is_password_showed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //use this to activate full screen
        setContentView(R.layout.activity_logon1);
        is_password_showed=false;
        //get edit text
        nickname_text=findViewById(R.id.logon1_nickname);
        account_text=findViewById(R.id.logon1_account_name);
        // get the password edit text
        password_text=findViewById(R.id.logon1_password);
        password_text.setTransformationMethod(PasswordTransformationMethod.getInstance()); //set the password style
        // get the button
        // password readable button
        password_readable=findViewById(R.id.zhuce1_password_readable);
        password_readable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_password_showed) {
                    is_password_showed=false;
                    password_text.setTransformationMethod(PasswordTransformationMethod.getInstance()); //hide the password
                }else{
                    is_password_showed=true;
                    password_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //show the password
                }
            }
        });
        // next step button
        next_step_button=findViewById(R.id.zhuce1_next_step);
        next_step_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogonActivity1.this, LogonActivity2.class); //switch to logon activity 2
                Bundle msg_to_next_step = new Bundle(); //send message to the next step

                msg_to_next_step.putString("nickname",nickname_text.getText().toString());
                msg_to_next_step.putString("account",account_text.getText().toString());
                msg_to_next_step.putString("password",password_text.getText().toString());
                intent.putExtras(msg_to_next_step);

                startActivity(intent);
                LogonActivity1.this.finish();
            }
        });
    }
}
package com.google.ar.sceneform.samples.gltf;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    ImageButton button = null;
    private ImageButton mSwitchButton;
    private ImageButton mLogonButton;
    private EditText mPasswordEditText;
    private boolean isHidden=true;

    EditText etUname,etUpwd;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        button = (ImageButton)findViewById(R.id.denglu_jinru);
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainPageActive.class);
                startActivity(intent);
            }
        });*/

        userDao = new UserDao(this);
        etUname = findViewById(R.id.username);
        etUpwd = findViewById(R.id.password);

        init();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUname.getText().toString();
                String password = etUpwd.getText().toString();
                UserBean userBean = userDao.querryUser(username,password);
                if(TextUtils.isEmpty(userBean.getUsername())){
                    Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainPageActive.class);
                    startActivity(intent);
                }
            }
        });

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUname.getText().toString();
                String password = etUpwd.getText().toString();
                boolean flag = userDao.insertUser(username,password);
                if(flag){
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    private void init(){

        mSwitchButton=(ImageButton) findViewById(R.id.denglu_kejian);

        mPasswordEditText=(EditText) findViewById(R.id.password);

        mLogonButton=(ImageButton) findViewById(R.id.denglu_zhuce);

        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                if (isHidden) {

                    //设置EditText文本为可见的

                    mPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {

                    //设置EditText文本为隐藏的

                    mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

                isHidden = !isHidden;

                mPasswordEditText.postInvalidate();

                //切换后将EditText光标置于末尾

                CharSequence charSequence = mPasswordEditText.getText();

                if (charSequence instanceof Spannable) {

                    Spannable spanText = (Spannable) charSequence;

                    Selection.setSelection(spanText, charSequence.length());

                }

            }

        });

        mLogonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, LogonActivity1.class);
                startActivity(intent);
            }
        });
    }
}

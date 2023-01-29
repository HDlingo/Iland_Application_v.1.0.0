package com.google.ar.sceneform.samples.gltf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class DetailYunduan extends AppCompatActivity {


    private ImageButton mBack;
    private ImageButton mDeer;
    private ImageButton mStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_yunduan);
        //----------------------------basic window setting-----------------------------------
        //set immersive status bar if possible
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //remove the title
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //-------------------------buttons processing-----------------------------------------
        mBack=findViewById(R.id.detail_tu_back);
        mDeer=findViewById(R.id.detail_deer_tu);
        mStart=findViewById(R.id.detail_start_tu);
        //back:close the activity
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailYunduan.this,MainPageActive.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_up,R.anim.slide_in_up);
            }
        });
    }
}
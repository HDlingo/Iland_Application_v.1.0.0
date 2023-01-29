package com.google.ar.sceneform.samples.gltf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class FirstPage extends AppCompatActivity {
    ImageButton button = null;
    ImageButton buttonZhuce = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        button = (ImageButton)findViewById(R.id.kaishi_denglu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FirstPage.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        buttonZhuce = (ImageButton)findViewById(R.id.kaishi_zhuce);
        buttonZhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FirstPage.this, LogonActivity1.class);
                startActivity(intent);
            }
        });
    }

}

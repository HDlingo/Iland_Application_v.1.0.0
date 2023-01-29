package com.google.ar.sceneform.samples.gltf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.ar.sceneform.samples.gltf.databinding.ActivityMainPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainPageActive extends AppCompatActivity {
    // activity binding for fragment switching
    private ActivityMainPageBinding binding;
    private BottomNavigationView mBottomNavigationView;
    //view pager
    ViewPager view_pager;
    private static final String TAG = "MainPageActive";
    private ArrayList<MainPagePagerItemModel> modelArrayList;
    private MainPagePagerAdapter pagerAdapter;

    private boolean isTouched=false;

    private float  oldX=0;
    private float newX=0;
    private float oldY=0;
    private float newY=0;

    private float yThreshold=200;

    private float abs(float x){
        return x<0?-x:x;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //-------------------------capture the touch event to process swiping-----------------------------
        int actionMasked = event.getActionMasked();
        // process the swiping down action that
        switch(actionMasked) {
            case (MotionEvent.ACTION_DOWN) :
                isTouched=true;
                oldX=event.getX();
                oldY=event.getY();
                Log.d(TAG,"Action was DOWN");
                break;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(TAG,"Action was MOVE");
                newX=event.getX();
                newY=event.getY();
                if(newY-oldY>yThreshold&&isTouched){
                    Log.d(TAG, String.format("dispatchTouchEvent: swipe activated at dy=%e", newY-oldY));
                    isTouched=false;
                    int currentItem = view_pager.getCurrentItem();
                    Intent intent; //init intent for activity switching
                    switch (currentItem){
                        case 0:
                        case 2:
                            //start the tu activity
                            intent = new Intent(MainPageActive.this, DetailYunduan.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_down,R.anim.slide_in_down);
                            break;
                        case 1:
                            //start the tu activity
                            intent = new Intent(MainPageActive.this, DetailYunnian.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_down,R.anim.slide_in_down);
                            break;
                        default:
                            Log.d(TAG, String.format("activate swipe at item:%d", currentItem));
                    }
                }
                break;
            case (MotionEvent.ACTION_UP) :
                isTouched=false;
                Log.d(TAG,"Action was UP");
                break;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(TAG,"Action was CANCEL");
                break;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                break;
            default :
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //set immersive status bar if possible
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        binding=ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //remove the title
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //switch to initial fragment
        replaceFragment(new MainPageMindfulnessFragment());

        //-------------------------navigation bar--------------------------------
        //set the navigation bar to the default choice
        mBottomNavigationView=(BottomNavigationView)findViewById(R.id.main_page_bottom_navigation_bar);
        //set the navigation bar as transparent
        mBottomNavigationView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));


        //get the choice of the bottom navigation bar
        binding.mainPageBottomNavigationBar.setOnItemSelectedListener( item-> {
            switch (item.getItemId()){
                case R.id.main_page_navigation_mindfulness:
                    replaceFragment(new MainPageMindfulnessFragment());
//                    loadCards();
                    break;
                case R.id.main_page_navigation_sandplay:
                    replaceFragment(new MainPageSandplayFragment());
//                    loadCards();
                    break;
                case R.id.main_page_navigation_other:
                    replaceFragment(new MainPageOtherFragment());
//                    loadCards();
                    break;
            }
            return true;
        });

        //-------------------------buttons--------------------------------


        //load button view
        ImageButton mOpen=findViewById(R.id.main_page_open_buttons_group);
        ImageButton mSetting=findViewById(R.id.main_page_setting_button);
        ImageButton mDeer=findViewById(R.id.main_page_deer_button);
        ImageButton mPersonal=findViewById(R.id.main_page_personal_button);

        //init state
        final boolean[] isButtonsVisible = {false};
        mDeer.setVisibility(View.INVISIBLE);
        mPersonal.setVisibility(View.INVISIBLE);

        mOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isButtonsVisible[0]){
                    isButtonsVisible[0] =false;
                    //hide all buttons
                    mDeer.setVisibility(View.INVISIBLE);
                    mPersonal.setVisibility(View.INVISIBLE);
                    //change src
                    mOpen.setImageResource(R.mipmap.open_day);
                }else{
                    isButtonsVisible[0] =true;
                    mDeer.setVisibility(View.VISIBLE);
                    mPersonal.setVisibility(View.VISIBLE);
                    //change src
                    mOpen.setImageResource(R.mipmap.close_day);
                }
            }
        });
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainPageActive.this,"setting clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //-------------------------navigation bar--------------------------------
        mBottomNavigationView.getMenu().findItem(R.id.main_page_navigation_mindfulness).setChecked(true);
        //-------------------------view pager--------------------------------
        view_pager=findViewById(R.id.mindfulness_view_pager);
        view_pager.setPageMargin(-10);
        loadCards();
    }

    //method for fragment switching
    //fragment: the fragment object to switch to
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_page_framelayout,fragment);
        fragmentTransaction.commit();
    }
    public void loadCards(){
        view_pager=findViewById(R.id.mindfulness_view_pager);
        //init array list for pager
        modelArrayList = new ArrayList<>();
        //add item to list
        modelArrayList.add(new MainPagePagerItemModel(R.mipmap.titalyunduan_day,R.mipmap.yunduan_day,R.mipmap.wordsyunduan_day,R.mipmap.xialayunduan_day));
        modelArrayList.add(new MainPagePagerItemModel(R.mipmap.titalyunnian_day,R.mipmap.yunnian_day,R.mipmap.wordsyunnian_day,R.mipmap.xialayunnian_day));
//        modelArrayList.add(new MainPagePagerItemModel(R.mipmap.titalyunduan_day,R.mipmap.yunduan_day,R.mipmap.wordsyunduan_day,R.mipmap.xialayunduan_day));
        //set adapter
        pagerAdapter = new MainPagePagerAdapter(this,modelArrayList);
        //set adapter to view pager
        view_pager.setAdapter(pagerAdapter);
        //set default padding
        view_pager.setPadding(100,0,100,0);
    }
}
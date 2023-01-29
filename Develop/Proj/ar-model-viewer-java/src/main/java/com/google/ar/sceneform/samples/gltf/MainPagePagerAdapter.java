package com.google.ar.sceneform.samples.gltf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class MainPagePagerAdapter extends PagerAdapter{
    //this class is an adapter for the pager view in main page
    private Context context;
    private ArrayList<MainPagePagerItemModel> modelArrayList;

    //constructor
    public MainPagePagerAdapter(Context context, ArrayList<MainPagePagerItemModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }


    @Override
    public int getCount() {
        return modelArrayList.size(); //return how many items in list
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_page_view_pager_item,container,false);
        //init uid views from the layout xml
        ImageView title_image_view = view.findViewById(R.id.mindfulness_view_page_item_title_image_view);
        ImageView context_image_view = view.findViewById(R.id.mindfulness_view_page_item_context_image_view);
        ImageView draw_down_image_view=view.findViewById(R.id.mindfulness_view_page_item_draw_down_image_view);
        ImageView description_image_view=view.findViewById(R.id.mindfulness_view_page_item_description_image_view);
        //get data
        MainPagePagerItemModel model=modelArrayList.get(position);
        int title_image_id=model.getTitle_image_id();
        int context_image_id= model.getContext_image_id();
        int description_image_id=model.getDescription_image_id();
        int draw_down_image_id=model.getDraw_down_image_id();
        //set data
        title_image_view.setImageResource(title_image_id);
        context_image_view.setImageResource(context_image_id);
        description_image_view.setImageResource(description_image_id);
        draw_down_image_view.setImageResource(draw_down_image_id);

        //handle card click
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context,"clicked\n", Toast.LENGTH_SHORT).show();
            }
        });
        //add the view to container
        container.addView(view,position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

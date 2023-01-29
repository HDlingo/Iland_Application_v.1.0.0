package com.google.ar.sceneform.samples.gltf;

public class MainPagePagerItemModel {
    //this class is for the model in pager view of main page
    //contains the following elements:
    //1 title image view
    //1 context image view
    int title_image_id,context_image_id,description_image_id,draw_down_image_id;

    public MainPagePagerItemModel(int title_image_id, int context_image_id, int description_image_id, int draw_down_image_id) {
        this.title_image_id = title_image_id;
        this.context_image_id = context_image_id;
        this.description_image_id = description_image_id;
        this.draw_down_image_id = draw_down_image_id;
    }

    public int getTitle_image_id() {
        return title_image_id;
    }

    public int getContext_image_id() {
        return context_image_id;
    }

    public int getDescription_image_id() {
        return description_image_id;
    }

    public int getDraw_down_image_id() {
        return draw_down_image_id;
    }
}

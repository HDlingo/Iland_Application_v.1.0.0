package com.google.ar.sceneform.samples.gltf.ar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.google.ar.core.Anchor;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Light;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.samples.gltf.DetailYunnian;
import com.google.ar.sceneform.samples.gltf.R;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ArActivity extends AppCompatActivity implements
        FragmentOnAttachListener,
        BaseArFragment.OnTapArPlaneListener,
        BaseArFragment.OnSessionConfigurationListener,
        ArFragment.OnViewCreatedListener,
        SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {

    static class ImageButtonInfo{
        ImageButton button;
        float endPosX;
        float endPosY;
        float startPosX;
        float startPosY;

        public ImageButton getButton() {
            return button;
        }

        public void setButton(ImageButton button) {
            this.button = button;
        }

        public float getEndPosX() {
            return endPosX;
        }

        public void setEndPosX(float endPosX) {
            this.endPosX = endPosX;
        }

        public float getEndPosY() {
            return endPosY;
        }

        public void setEndPosY(float endPosY) {
            this.endPosY = endPosY;
        }

        public ImageButtonInfo(ImageButton button) {
            this.button = button;
            this.endPosX = 0.0f;
            this.endPosY = 0.0f;
            this.startPosX = 0.0f;
            this.startPosY = 0.0f;
        }

        public float getStartPosX() {
            return startPosX;
        }

        public void setStartPosX(float startPosX) {
            this.startPosX = startPosX;
        }

        public float getStartPosY() {
            return startPosY;
        }

        public void setStartPosY(float startPosY) {
            this.startPosY = startPosY;
        }
    }
    private static final String TAG = "ArActivity";

    private final int BUTTONOFFSET = 200;

    private final int BUTTONDIS = 100;

    private final float INTENSITY_MAX = 10000000f;

    private final float INTENSITY_MIN = 100000f;

    private final float ANGLE_MAX = 1.0f;

    private final float ANGLE_MIN = 0.0f;

    private final float INIT_SCALE = 0.05f;

    private final float SIGHT_SCALE = 1.0f;

    private final float UP_DOWN_PARAM = 13.0f;

    private final Vector3 INIT_KIOSK_SIGHT_POS =new Vector3(1.89652f,12.2837f,2.57026f);

    private final Vector3 INIT_TOWER_SIGHT_POS = new Vector3(1.6969f,13.609f,-4.9472f);//new Vector3(1.68204f,13.7396f,-5.25524f);

    private final Vector3 INIT_MOUNTAIN_SIGHT_POS = new Vector3(-0.63962f,18.2572f,-4.84735f);
    private final Vector3 INIT_BRIDGE_SIGHT_POS = new Vector3(-1.02623f,11.4863f,3.51982f);

    private final float PLAIN_POS_Y = 11.0f;
    private final Vector3 PLAIN_POS = new Vector3(0,11.0f,0);

    private ArFragment arFragment;
    private Renderable model;
    private ViewRenderable viewRenderable;


    private Light spotLight;
    private TransformableNode newModel;
    private Anchor islandAnchor;

    private boolean lightOn;

    private List<Node> lightNodes;

    private boolean scaled;

    private float scaleFactor;

    private boolean islandAnimeFlag = false;

    private ImageButton imageButton;
    private ImageButton openLightButton;
    private ImageButton cancelLightButton;
    private ImageButton fixLightButton;
    private ImageButton colorButton;
    private ImageButton intensityButton;
    private ImageButton rangeButton;
    private ImageButton sightButton;
    private ImageButton unSightButton;
    private ImageButtonInfo unSightInfo;
    private ImageButton kioskButton;
    private ImageButton towerButton;
    private ImageButton mountainButton;
    private ImageButton bridgeButton;
    private ImageButton backButton;
    private SeekBar intensitySeekBar;
    private TextView intensityTextView;
    private SeekBar rangeSeekBar;
    private TextView rangeTextView;
    private SeekBar colorSeekBar;
    private TextView colorTextView;
    private boolean lightFlag = true;
    private boolean intensityFlag = true;
    private boolean rangeFlag = true;
    private boolean colorFlag = true;
    private boolean sightFlag = true;
    private boolean unSightFlag = true;
    private int seekBarWidth;
    private float textViewStartPos;
    private float textViewEndPos;

    private Vector3 islandOriWorldPos;
    private Vector3 islandSightWorldPos;
    private Vector3 moveWorldDirection;
    private Quaternion islandOriWorldRotation;
    private Quaternion islandSightWorldRotation;
    private Vector3 cameraWorldPosition;
    private Vector3 sightLocalPos;
    private Vector3 islandMiddlePos;

    private Vector3 staticY;
    private Vector3 plainY;

    private boolean showFlag = true;

    private List<List<ImageButtonInfo>> ibInfoGroup = new ArrayList<>();

    private List<ImageButtonInfo> sightButtonList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ar);
        getSupportFragmentManager().addFragmentOnAttachListener(this);

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFragment.class, null)
                        .commit();
            }
        }

        widgetInit();

        imageButton.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        calculateButtonEndPos(BUTTONOFFSET);
                        textViewStartPos = colorTextView.getX();
                        textViewEndPos = colorTextView.getX() + seekBarWidth + 10;
                        imageButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        lightNodes = new ArrayList<>();

        loadModels();

        scaled = false;
        scaleFactor = 1.0f;
    }

    private void calculateButtonEndPos(int radius){
        int k = ibInfoGroup.size();

        float buttonStartPosX = imageButton.getX();
        float buttonStartPosY = imageButton.getY();
        Log.e("buttonpos",String.valueOf(buttonStartPosX));
        for(int i = 0; i <ibInfoGroup.size() ;i++) {
            k--;
            List<ImageButtonInfo> ibInfoList = ibInfoGroup.get(i);
            int radiusRe = radius;
            for (int j = ibInfoList.size() - 1; j >= 0; j--) {
                ImageButtonInfo cInfo = ibInfoList.get(j);

                float distanceX = - (float) (radiusRe * (Math.cos(Util.getAngle(ibInfoGroup.size(), k))));
                float distanceY = -(float) (radiusRe * (Math.sin(Util.getAngle(ibInfoGroup.size(), k))));
                radiusRe += BUTTONDIS;
                cInfo.setStartPosX(buttonStartPosX);
                cInfo.setStartPosY(buttonStartPosY);
                cInfo.setEndPosX(buttonStartPosX + distanceX);
                cInfo.setEndPosY(buttonStartPosY + distanceY);
            }
        }
        buttonStartPosX = sightButton.getX();
        buttonStartPosY = sightButton.getY();
        int radiusRe = BUTTONDIS;
        for(int i = 0;i<sightButtonList.size();i++){
            ImageButtonInfo cInfo = sightButtonList.get(i);
            float distanceY = (float) (radiusRe);
            radiusRe += BUTTONDIS;
            cInfo.setStartPosX(buttonStartPosX);
            cInfo.setStartPosY(buttonStartPosY);
            cInfo.setEndPosY(buttonStartPosY + distanceY);
        }
        float distanceY = (float) (radiusRe);
        radiusRe += BUTTONDIS;
        unSightInfo.setStartPosX(buttonStartPosX);
        unSightInfo.setStartPosY(buttonStartPosY);
        unSightInfo.setEndPosY(buttonStartPosY + distanceY);
    }

    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.arFragment) {
            arFragment = (ArFragment) fragment;
            arFragment.setOnSessionConfigurationListener(this);
            arFragment.setOnViewCreatedListener(this);
            arFragment.setOnTapArPlaneListener(this);
        }
        Log.e(TAG,"onAttachFragment");
    }

    void onUpdate(){
        if(newModel == null && showFlag){
            Frame frame = arFragment.getArSceneView().getArFrame();
            if(arFragment.getArSceneView().getPlaneRenderer().getRenderFlag()){
                Toast.makeText(this, "请点击点云以放置岛屿", Toast.LENGTH_LONG).show();
                showFlag = false;
            }
//            for (Plane plane : frame.getUpdatedTrackables(Plane.class)){
//                if (plane.getTrackingState() == TrackingState.TRACKING){
//                    Toast.makeText(this, "请点击点云以放置岛屿", Toast.LENGTH_LONG).show();
//                    showFlag = false;
//                    break;
//                }
//            }
        }
        if(newModel!=null) {
            Frame frame = arFragment.getArSceneView().getArFrame();
            Camera camera = frame.getCamera();

            if (camera.getTrackingState() == TrackingState.PAUSED){
                Log.e("TrackingState","paused");
            }
            scaled = true;
            Vector3 scale = newModel.getLocalScale();
            Log.e("localscale", String.valueOf(scale.x)+String.valueOf(scale.y)+String.valueOf(scale.z));

        }
        if(newModel!=null&&spotLight!=null&&lightOn&&scaled) {
            Frame frame = arFragment.getArSceneView().getArFrame();
            Camera camera = frame.getCamera();
            if (camera.getTrackingState() == TrackingState.TRACKING){
                Pose cameraPose = camera.getDisplayOrientedPose();

                float[] zAxis = cameraPose.getZAxis();
                Vector3 worldDirection = new Vector3(-zAxis[0],-zAxis[1],-zAxis[2]);
                Vector3 localDirection = newModel.worldToLocalDirection(worldDirection);

                Vector3 worldPosition = new Vector3(cameraPose.tx(),cameraPose.ty(),cameraPose.tz());
                Vector3 localPosition = newModel.worldToLocalPoint(worldPosition);

                spotLight.setDirection(localDirection);
                spotLight.setPosition(localPosition);
            }
        }
    }

    @Override
    public void onSessionConfiguration(Session session, Config config) {
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC);
            config.setLightEstimationMode(Config.LightEstimationMode.ENVIRONMENTAL_HDR);
        }
        Log.e(TAG,"onSessionConfiguration");
    }

    @Override
    protected void onResume() {
        super.onResume();

        arFragment.onResume();
    }

    @Override
    protected void onPause() {
        arFragment.onPause();
        super.onPause();

    }

    @Override
    public void onViewCreated(ArSceneView arSceneView) {
        arFragment.setOnViewCreatedListener(null);

        // Fine adjust the maximum frame rate
        arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL);
//        arSceneView.getCameraStream()
//                .setDepthOcclusionMode(CameraStream.DepthOcclusionMode.DEPTH_OCCLUSION_ENABLED);
        Scene.OnUpdateListener listener = frameTime -> {
            arFragment.onUpdate(frameTime);
            onUpdate();
        };

        arFragment.getArSceneView().getScene().addOnUpdateListener(listener);
//        arFragment.getArSceneView().getScene().removeOnUpdateListener(listener);
        Log.e(TAG,"onViewCreated");

    }
    //Uri.parse("https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb")
//    Uri.parse("ball1/ball.gltf")
    public void loadModels() {
        WeakReference<ArActivity> weakActivity = new WeakReference<>(this);
        ModelRenderable.builder()
                .setSource(this, Uri.parse("models/island2.glb"))
                .setIsFilamentGltf(true)
                .setAsyncLoadEnabled(true)
                .build()
                .thenAccept(model -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.model = model;
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(
                            this, "无法加载模型", Toast.LENGTH_LONG).show();
                    return null;
                });
        ViewRenderable.builder()
                .setView(this, R.layout.view_model_title)
                .build()
                .thenAccept(viewRenderable -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.viewRenderable = viewRenderable;
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "无法加载模型", Toast.LENGTH_LONG).show();
                    return null;
                });

    }

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        if (model == null || viewRenderable == null) {
            Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newModel != null){
            Toast.makeText(this, "您只能放置一个岛屿", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Anchor.
        islandAnchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(islandAnchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        anchorNode.setEnabled(true);

        // Create the transformable model and add it to the anchor.
        newModel = new TransformableNode(arFragment.getTransformationSystem());
        float scale = INIT_SCALE;
        newModel.setLocalScale(new Vector3(scale,scale,scale));
        newModel.getScaleController().setEnabled(false);
        newModel.setParent(anchorNode);
        newModel.setRenderable(this.model)
                .animate(true).start();


        newModel.select();


        float y = 200.0f;
        Light pointLight =
                Light.builder(ArActivity.this, Light.Type.POINT)
                        .setColor(new Color(android.graphics.Color.WHITE))
                        .setIntensity(1000f)
//                        .setPosition(new Vector3(45.546783f, 316.3221f, -4.266613f))
                        .setPosition(new Vector3(43.584f, 232.69f, -2.9925f))
//                        .setDirection(new Vector3(0.0f,1.0f,0.0f))
//                        .setFalloffRadius(0.5f)
//                        .setInnerConeAngle(0.1f)
//                        .setOuterConeAngle(0.13f)
                        .setShadowCastingEnabled(false)
                        .build();

        Light pointLight1 =
                Light.builder(ArActivity.this, Light.Type.POINT)
                        .setColor(new Color(android.graphics.Color.WHITE))
                        .setIntensity(1000f)
                        .setPosition(INIT_KIOSK_SIGHT_POS)
                        .setShadowCastingEnabled(false)
                        .build();
        newModel.setLight(pointLight1);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        switch(seekBar.getId()){
//            case R.id.seekBar:
//                Log.e(TAG,String.valueOf(seekBar.getProgress()));
//
//                if(newModel!=null){
//                    float max = (float)seekBar.getMax();
//                    float proc = (float)seekBar.getProgress();
//                    newModel.setLocalPosition(new Vector3(0.0f, (0.0f + proc - max) ,0.0f));
//                }
//                break;
            case R.id.colorSeekBar:
                float radio = (float) progress / seekBar.getMax();
                int mColor = ColorPickerGradientUtil.getColor(radio);
                // 设置拖动按钮随颜色的改变而改变
                Drawable thumb = seekBar.getThumb();
                thumb.setColorFilter(mColor, PorterDuff.Mode.SRC_IN);
                if(lightOn)
                    spotLight.setColor(new Color(mColor));
                break;
            case R.id.intensitySeekBar:
                float max = (float)seekBar.getMax();
                float proc = (float)seekBar.getProgress();
                float intensity = proc/(max)*(INTENSITY_MAX-INTENSITY_MIN) + INTENSITY_MIN;
                if(lightOn)
                    spotLight.setIntensity(intensity);
//                Log.e("intensitySeekBarMax",String.valueOf(seekBar.getProgress()));
                break;
            case R.id.rangeSeekBar:
                max = (float)seekBar.getMax();
                proc = (float)seekBar.getProgress();
                float range = proc/(max)*(ANGLE_MAX-ANGLE_MIN) + ANGLE_MIN;

                if(lightOn) {
                    spotLight.setInnerConeAngle(range);
                    spotLight.setOuterConeAngle(range + 0.1f);
                }
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
//        Log.e("LIGHTLOG",String.valueOf(newModel.getLightInstancesSize()));
        if(islandAnimeFlag){
            Toast.makeText(ArActivity.this, "请等待视角切换", Toast.LENGTH_LONG).show();
            return;
        }
        switch(view.getId()){
            case R.id.backButton:
                Intent intent = new Intent(ArActivity.this,DetailYunnian.class);
                startActivity(intent);
                break;
            case R.id.kioskButton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null){
                    return;
                }
                sightButtonAnimation(sightButtonList,true);

                if(sightButtonList.get(sightButtonList.size()-1).getButton().getId() != unSightInfo.getButton().getId()) {
                    toFirstSight(INIT_KIOSK_SIGHT_POS);
                    sightButtonList.add(unSightInfo);
                }

                else{
                    changeFirstSight(INIT_KIOSK_SIGHT_POS);
                }

//                unSightFlag = false;
                sightFlag = !sightFlag;


                break;
            case R.id.towerButton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null){
                    return;
                }
                sightButtonAnimation(sightButtonList,true);
                if(sightButtonList.get(sightButtonList.size()-1).getButton().getId() != unSightInfo.getButton().getId()) {
                    toFirstSight(INIT_TOWER_SIGHT_POS);
                    sightButtonList.add(unSightInfo);
                }
                else{
                    changeFirstSight(INIT_TOWER_SIGHT_POS);
                }
//                unSightFlag = false;
                sightFlag = !sightFlag;

                break;
            case R.id.mountainButton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null){
                    return;
                }
                sightButtonAnimation(sightButtonList,true);
                if(sightButtonList.get(sightButtonList.size()-1).getButton().getId() != unSightInfo.getButton().getId()) {
                    toFirstSight(INIT_MOUNTAIN_SIGHT_POS);
                    sightButtonList.add(unSightInfo);
                }
                else{
                    changeFirstSight(INIT_MOUNTAIN_SIGHT_POS);
                }
//                unSightFlag = false;
                sightFlag = !sightFlag;
                break;
            case R.id.bridgeButton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null){
                    return;
                }
                sightButtonAnimation(sightButtonList,true);
                if(sightButtonList.get(sightButtonList.size()-1).getButton().getId() != unSightInfo.getButton().getId()) {
                    toFirstSight(INIT_BRIDGE_SIGHT_POS);
                    sightButtonList.add(unSightInfo);
                }
                else{
                    changeFirstSight(INIT_BRIDGE_SIGHT_POS);
                }
//                unSightFlag = false;
                sightFlag = !sightFlag;

                break;
            case R.id.sightButton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null){
                    return;
                }
//                sightButton.setVisibility(View.INVISIBLE);
//                unSightButton.setVisibility(View.VISIBLE);
                sightButtonAnimation(sightButtonList,true);
//                if(sightFlag)
//                    sightButtonList.add(unSightInfo);
//                else
//                    sightButtonList.remove(unSightInfo);
                sightFlag = !sightFlag;
//                toFirstSight();
                break;
            case R.id.unsightButton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null){
                    return;
                }
                sightButtonAnimation(sightButtonList, false);

                sightFlag = !sightFlag;
//                unSightButton.setVisibility(View.INVISIBLE);
//                sightButton.setVisibility(View.VISIBLE);
                toOriSight();
                arFragment.getArSceneView().getPlaneRenderer().setEnabled(true);
                break;
            case R.id.openlightbutton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null||!scaled){
                    return;
                }
                if(newModel.getLightInstancesSize()>8){
                    Toast.makeText(ArActivity.this, "光源不能超过8个!", Toast.LENGTH_LONG).show();
                }
                if(lightOn){
                    Toast.makeText(ArActivity.this, "光源已打开", Toast.LENGTH_LONG).show();
                    return;
                }
                intensitySeekBar.setProgress(50);
                rangeSeekBar.setProgress(50);
                colorSeekBar.setProgress(0);

                float intensity = (float)intensitySeekBar.getProgress()/(float)intensitySeekBar.getMax() * (INTENSITY_MAX - INTENSITY_MIN) + INTENSITY_MIN;
                float angle = (float) rangeSeekBar.getProgress() / (float) rangeSeekBar.getMax() * (ANGLE_MAX - ANGLE_MIN)+ ANGLE_MIN;
                float radio = (float) colorSeekBar.getProgress() / (float)colorSeekBar.getMax();
                int mColor = ColorPickerGradientUtil.getColor(radio);

                spotLight =
                        Light.builder(ArActivity.this, Light.Type.SPOTLIGHT)
                                .setColor(new Color(mColor))
                                .setIntensity(intensity)
                                .setInnerConeAngle(angle)
                                .setOuterConeAngle(angle+0.1f)
                                .setShadowCastingEnabled(false)
                                .build();

                newModel.setLight(spotLight);
                lightOn = true;
                break;
            case R.id.cancellightbutton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(newModel.getLightInstancesSize()==1){
                    Toast.makeText(ArActivity.this, "当前没有光源", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null||!scaled){

                    return;
                }

                if(newModel.getLightInstancesSize()>1) {
                    newModel.destroyLightInstanceLast();
                    if(lightOn){
                        if(!intensityFlag||!rangeFlag||!colorFlag) {
                            seekBarAnimation(intensitySeekBar, intensityTextView, false, 0);
                            seekBarAnimation(rangeSeekBar, rangeTextView, false, 0);
                            seekBarAnimation(colorSeekBar, colorTextView, false, 0);
                            intensityFlag = true;
                            rangeFlag = true;
                            colorFlag = true;
                        }
                        lightOn = false;
                    }
                    else{
                        int lastNodeIndex = lightNodes.size()-1;
                        Node lightNode = lightNodes.get(lastNodeIndex);
                        lightNodes.remove(lastNodeIndex);
                        newModel.removeChild(lightNode);
                    }
                }
                else{
                    Toast.makeText(ArActivity.this, "当前已没有光源", Toast.LENGTH_LONG).show();
                }

//              newModel.setLight(null);
                break;

            case R.id.fixlightbutton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null||!scaled){
                    return;
                }
                if(!lightOn){
                    Toast.makeText(ArActivity.this, "请先打开光源", Toast.LENGTH_LONG).show();
                    return;
                }
                newModel.destroyLightInstanceLast();
                lightOn = false;
                if(!intensityFlag||!rangeFlag||!colorFlag) {
                    seekBarAnimation(intensitySeekBar, intensityTextView, false, 0);
                    seekBarAnimation(rangeSeekBar, rangeTextView, false, 0);
                    seekBarAnimation(colorSeekBar, colorTextView, false, 0);
                    intensityFlag = true;
                    rangeFlag = true;
                    colorFlag = true;
                }

                Frame frame = arFragment.getArSceneView().getArFrame();
                assert frame != null;
                Camera camera = frame.getCamera();
                if (camera.getTrackingState() == TrackingState.TRACKING) {
                    Pose cameraPose = camera.getDisplayOrientedPose();

                    Node lightNode = new Node();
                    lightNode.setParent(newModel);
                    lightNode.setEnabled(true);
                    lightNode.setRenderable(viewRenderable);
                    Light spotLight1 =
                            Light.builder(ArActivity.this, Light.Type.SPOTLIGHT)
                                    .setColor(spotLight.getColor())
                                    .setIntensity(spotLight.getIntensity())
                                    .setInnerConeAngle(spotLight.getInnerConeAngle())
                                    .setOuterConeAngle(spotLight.getOuterConeAngle())
                                    .setShadowCastingEnabled(false)
                                    .build();
                    lightNodes.add(lightNode);

                    float[] zAxis = cameraPose.getZAxis();
                    Vector3 worldDirection = new Vector3(-zAxis[0], -zAxis[1], -zAxis[2]);
                    Vector3 localDirection = lightNode.worldToLocalDirection(worldDirection);

                    Vector3 worldPosition = new Vector3(cameraPose.tx(), cameraPose.ty(), cameraPose.tz());
                    Vector3 localPosition = newModel.worldToLocalPoint(worldPosition);

                    spotLight1.setDirection(localDirection);
//                    spotLight.setPosition(localPosition);
//                    spotLight.setDirection(new Vector3(0.0f, 0.0f, 1.0f));

                    lightNode.setLight(spotLight1);
                    lightNode.setLocalPosition(localPosition);


                    Log.e(TAG,"lightloc:"+String.valueOf(localPosition.x) + ", "+ String.valueOf(localPosition.y)+ ", " + String.valueOf(localPosition.z));
                }
                break;
            case R.id.imageButton:
                if(newModel==null){
                    Toast.makeText(ArActivity.this, "请先放置岛屿", Toast.LENGTH_LONG).show();
                    return;
                }
                if(arFragment==null||!scaled){
                    return;
                }
                if(lightFlag){
                    buttonAnimation(ibInfoGroup);
                    lightFlag = !lightFlag;
                }
                else{
                    if(!intensityFlag||!rangeFlag||!colorFlag) {
                        seekBarAnimation(intensitySeekBar, intensityTextView, false, 0);
                        seekBarAnimation(rangeSeekBar, rangeTextView, false, 0);
                        seekBarAnimation(colorSeekBar, colorTextView, false, 1);
                        intensityFlag = true;
                        rangeFlag = true;
                        colorFlag = true;
                    }
                    else{
                        buttonAnimation(ibInfoGroup);
                        lightFlag = !lightFlag;
                    }
                }
                break;
            case R.id.intensityButton:
                if(!lightOn){
                    Toast.makeText(ArActivity.this, "请打开新光源", Toast.LENGTH_LONG).show();
                    return;
                }
                seekBarAnimation(intensitySeekBar, intensityTextView,intensityFlag,0);
                intensityFlag = !intensityFlag;
                break;
            case R.id.rangeButton:
                if(!lightOn){
                    Toast.makeText(ArActivity.this, "请打开新光源", Toast.LENGTH_LONG).show();
                    return;
                }
                seekBarAnimation(rangeSeekBar, rangeTextView,rangeFlag,0);
                rangeFlag = !rangeFlag;
                break;
            case R.id.colorButton:
                if(!lightOn){
                    Toast.makeText(ArActivity.this, "请打开新光源", Toast.LENGTH_LONG).show();
                    return;
                }
                seekBarAnimation(colorSeekBar, colorTextView,colorFlag,0);
                colorFlag = !colorFlag;
                break;

        }
    }

    public void sightButtonAnimation(List<ImageButtonInfo> sightButtonList, boolean buttonFlag){
        float fromY;
        float toY;
        for(int i = 0; i < sightButtonList.size();i++){
            ObjectAnimator objAnimatorY;
            ObjectAnimator objAnimatorRotate;
            ImageButtonInfo buttonInfo = sightButtonList.get(i);
            ImageButton button = buttonInfo.getButton();
            button.setVisibility(View.VISIBLE);

            fromY = button.getY();
            //弹出按钮
            if(sightFlag){
                toY = buttonInfo.getEndPosY();
            }else{
                toY = buttonInfo.getStartPosY();
            }

            objAnimatorY = ObjectAnimator.ofFloat(button, "y", fromY, toY);
            objAnimatorY.setDuration(200);
            objAnimatorY.setStartDelay(100);
            objAnimatorY.start();

            objAnimatorRotate = ObjectAnimator.ofFloat(button, "rotation", 0, 360);
            objAnimatorRotate.setDuration(200);
            objAnimatorY.setStartDelay(100);
            objAnimatorRotate.start();

            if(!sightFlag){
                objAnimatorY.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        for(int i = 0; i< sightButtonList.size();i++){
                            sightButtonList.get(i).getButton().setVisibility(View.INVISIBLE);
                        }
                        if(!buttonFlag){
                            sightButtonList.remove(unSightInfo);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                });
            }
        }
    }




    public void buttonAnimation(List<List<ImageButtonInfo>>  buttonInfoGroup) {
        float fromX;
        float toX;
        float fromY;
        float toY;
        Log.e("buttonpos",String.valueOf(imageButton.getX()));
        for(int i = 0; i <buttonInfoGroup.size() ;i++){
            List<ImageButtonInfo> buttonInfoList = buttonInfoGroup.get(i);
            for (int j = 0; j < buttonInfoList.size(); j++) {
                ObjectAnimator objAnimatorX;
                ObjectAnimator objAnimatorY;
                ObjectAnimator objAnimatorRotate;

                ImageButtonInfo buttonInfo = buttonInfoList.get(j);
                ImageButton button = buttonInfo.getButton();
                // 将按钮设为可见
                button.setVisibility(View.VISIBLE);
                fromX = button.getX();
                fromY = button.getY();
                //弹出按钮
                if(lightFlag){
                    toX = buttonInfo.getEndPosX();
                    toY = buttonInfo.getEndPosY();
                }else{
                    toX = buttonInfo.getStartPosX();
                    toY = buttonInfo.getStartPosY();
                }
                // X方向移动
                objAnimatorX = ObjectAnimator.ofFloat(button, "x", fromX, toX);
                objAnimatorX.setDuration(200);
                objAnimatorX.setStartDelay(100);
                objAnimatorX.start();
                // Y方向移动
                objAnimatorY = ObjectAnimator.ofFloat(button, "y", fromY, toY);
                objAnimatorY.setDuration(200);
                objAnimatorY.setStartDelay(100);
                objAnimatorY.start();
                // 按钮旋转
                objAnimatorRotate = ObjectAnimator.ofFloat(button, "rotation", 0, 360);
                objAnimatorRotate.setDuration(200);
                objAnimatorY.setStartDelay(100);
                objAnimatorRotate.start();
                if (!lightFlag) {
                    objAnimatorX.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            for(int i = 0; i< buttonInfoGroup.size();i++){
                                List<ImageButtonInfo> buttonInfos = buttonInfoGroup.get(i);
                                for (int j = 0; j < buttonInfos.size(); j++) {
                                    buttonInfos.get(j).getButton().setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }
                    });
                }
            }
        }

    }

    void seekBarAnimation(SeekBar seekBar,TextView textView, boolean barFlag, int buttonBack){
        Property<SeekBar, Float> PROPERTY_SEEKBAR_SCALE = new Property<SeekBar, Float>(Float.class, "scale") {
            @Override
            public Float get(SeekBar seekBar) {
                FrameLayout.LayoutParams params= (FrameLayout.LayoutParams)seekBar.getLayoutParams();
                return (float)params.width/(float)seekBarWidth;
            }


            @Override
            public void set(SeekBar seekBar, Float value) {
                FrameLayout.LayoutParams params= (FrameLayout.LayoutParams)seekBar.getLayoutParams();
                params.width = (int)(seekBarWidth * value);
                seekBar.setLayoutParams(params); // 使设置好的布局参数应用到控件
            }
        };

        ObjectAnimator objAnimatorScale;
        ObjectAnimator objAnimatorX;
        if(barFlag){
            seekBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params= (FrameLayout.LayoutParams)seekBar.getLayoutParams();

            objAnimatorScale = ObjectAnimator.ofFloat(seekBar, PROPERTY_SEEKBAR_SCALE, (float)params.width/(float)seekBarWidth, 1);
            objAnimatorScale.setDuration(200);
            objAnimatorScale.start();

            objAnimatorX = ObjectAnimator.ofFloat(textView, "x", textView.getX(), textViewEndPos);
            objAnimatorX.setDuration(200);
            objAnimatorX.start();
        }else{
            FrameLayout.LayoutParams params= (FrameLayout.LayoutParams)seekBar.getLayoutParams();
            objAnimatorScale = ObjectAnimator.ofFloat(seekBar, PROPERTY_SEEKBAR_SCALE, (float)params.width/(float)seekBarWidth, 0);
            objAnimatorScale.setDuration(200);
            objAnimatorScale.setStartDelay(100);
            objAnimatorScale.start();

            objAnimatorX = ObjectAnimator.ofFloat(textView, "x",  textView.getX(), textViewStartPos);
            objAnimatorX.setDuration(200);
            objAnimatorX.setStartDelay(100);
            objAnimatorX.start();

            objAnimatorX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    textView.setVisibility(View.INVISIBLE);
                    seekBar.setVisibility(View.INVISIBLE);
                    if(buttonBack==1){
                        buttonAnimation(ibInfoGroup);
                        lightFlag = !lightFlag;
                    }
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
        }
    }

    private void widgetInit(){

        List<ImageButtonInfo> ibInfoList1 = new ArrayList<>();
        List<ImageButtonInfo> ibInfoList2 = new ArrayList<>();

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);

        openLightButton = findViewById(R.id.openlightbutton);
        openLightButton.setOnClickListener(this);
        ibInfoList1.add(new ImageButtonInfo(openLightButton));

        cancelLightButton = findViewById(R.id.cancellightbutton);
        cancelLightButton.setOnClickListener(this);
        ibInfoList1.add(new ImageButtonInfo(cancelLightButton));

        fixLightButton = findViewById(R.id.fixlightbutton);
        fixLightButton.setOnClickListener(this);
        ibInfoList1.add(new ImageButtonInfo(fixLightButton));

        ibInfoGroup.add(ibInfoList1);

        colorButton = findViewById(R.id.colorButton);
        colorButton.setOnClickListener(this);
        ibInfoList2.add(new ImageButtonInfo(colorButton));

        rangeButton = findViewById(R.id.rangeButton);
        rangeButton.setOnClickListener(this);
        ibInfoList2.add(new ImageButtonInfo(rangeButton));

        intensityButton = findViewById(R.id.intensityButton);
        intensityButton.setOnClickListener(this);
        ibInfoList2.add(new ImageButtonInfo(intensityButton));

        ibInfoGroup.add(ibInfoList2);

        sightButton = findViewById(R.id.sightButton);
        sightButton.setOnClickListener(this);

        unSightButton = findViewById(R.id.unsightButton);
        unSightButton.setOnClickListener(this);
        unSightInfo = new ImageButtonInfo(unSightButton);


        kioskButton = findViewById(R.id.kioskButton);
        kioskButton.setOnClickListener(this);
        sightButtonList.add(new ImageButtonInfo(kioskButton));

        towerButton = findViewById(R.id.towerButton);
        towerButton.setOnClickListener(this);
        sightButtonList.add(new ImageButtonInfo(towerButton));

        mountainButton = findViewById(R.id.mountainButton);
        mountainButton.setOnClickListener(this);
        sightButtonList.add(new ImageButtonInfo(mountainButton));

        bridgeButton = findViewById(R.id.bridgeButton);
        bridgeButton.setOnClickListener(this);
        sightButtonList.add(new ImageButtonInfo(bridgeButton));

        intensitySeekBar = findViewById(R.id.intensitySeekBar);
        intensitySeekBar.setOnSeekBarChangeListener(this);
        intensitySeekBar.setProgress(50);
        intensityTextView = findViewById(R.id.intensityText);
        intensitySeekBar.setY(intensitySeekBar.getY()-BUTTONOFFSET);
        intensityTextView.setY(intensityTextView.getY() - BUTTONOFFSET);
        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams)intensitySeekBar.getLayoutParams();
        seekBarWidth = params.width;

        rangeSeekBar = findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setOnSeekBarChangeListener(this);
        rangeSeekBar.setProgress(50);
        rangeTextView = findViewById(R.id.rangeText);
        rangeSeekBar.setY(rangeSeekBar.getY()-BUTTONOFFSET-BUTTONDIS);
        rangeTextView.setY(rangeTextView.getY() - BUTTONOFFSET-BUTTONDIS);

        colorSeekBar = findViewById(R.id.colorSeekBar);
        colorSeekBar.setProgress(0);

        Drawable thumb = colorSeekBar.getThumb();
        thumb.setColorFilter(android.graphics.Color.WHITE, PorterDuff.Mode.SRC_IN);
        colorSeekBar.setOnSeekBarChangeListener(this);
        colorTextView = findViewById(R.id.colorText);

        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                        ColorPickerGradientUtil.PICKCOLORBAR_COLORS, ColorPickerGradientUtil.PICKCOLORBAR_POSITIONS, Shader.TileMode.REPEAT);
                return linearGradient;
            }
        };
        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setCornerRadius(10);
        paint.setShaderFactory(shaderFactory);
        colorSeekBar.setProgressDrawable(paint);

        colorSeekBar.setY(colorSeekBar.getY() - BUTTONOFFSET-BUTTONDIS*2);
        colorTextView.setY(colorTextView.getY() - BUTTONOFFSET-BUTTONDIS*2);

    }

    private void changeFirstSight(Vector3 sightPos){
        sightLocalPos = sightPos;
        Property<TransformableNode, Vector3> PROPERTY_ISLAND_TRANS = new Property<TransformableNode, Vector3>(Vector3.class, "scale") {
            @Override
            public Vector3 get(TransformableNode model) {

                return model.getWorldPosition();
            }
            @Override
            public void set(TransformableNode model, Vector3 pos) {
                model.setWorldPosition(pos);
            }
        };
        islandAnimeFlag = true;
//        arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);
        Frame frame = arFragment.getArSceneView().getArFrame();
        Camera camera = frame.getCamera();
        if (camera.getTrackingState() == TrackingState.TRACKING){
            Pose cameraPose = camera.getDisplayOrientedPose();
            cameraWorldPosition = new Vector3(cameraPose.tx(),cameraPose.ty(),cameraPose.tz());
        }
        Vector3 sightWorldPosition = newModel.localToWorldPoint(sightLocalPos);
        Vector3 moveDirection = Vector3.subtract(cameraWorldPosition, sightWorldPosition);
        Vector3 islandNewWorldPosition = Vector3.add(moveDirection, newModel.getWorldPosition());
        ObjectAnimator objAnimatorTrans;
        objAnimatorTrans = ObjectAnimator.ofObject(newModel, PROPERTY_ISLAND_TRANS, new Vector3Evaluator(),newModel.getWorldPosition(),islandNewWorldPosition);
        objAnimatorTrans.setDuration(3000);
        objAnimatorTrans.start();
        objAnimatorTrans.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                islandAnimeFlag = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    private void toFirstSight(Vector3 sightPos){
        sightLocalPos = sightPos;
        arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);
        Frame frame = arFragment.getArSceneView().getArFrame();
        Camera camera = frame.getCamera();
        if (camera.getTrackingState() == TrackingState.TRACKING){
            Pose cameraPose = camera.getDisplayOrientedPose();
            newModel.getRotationController().setEnabled(false);
            newModel.getTranslationController().setEnabled(false);

            cameraWorldPosition = new Vector3(cameraPose.tx(),cameraPose.ty(),cameraPose.tz());
            Vector3 islandWorldPosition = newModel.getWorldPosition();
            Vector3 direction = Vector3.subtract(cameraWorldPosition, islandWorldPosition);
            direction.y = 0;
            Quaternion lookRotation = Quaternion.lookRotation(direction, Vector3.up());
            islandSightWorldRotation = lookRotation;
            islandOriWorldRotation = newModel.getWorldRotation();
            Vector3 islandOriLocalScale = newModel.getLocalScale();

            newModel.setWorldRotation(lookRotation);
            newModel.setLocalScale(new Vector3(SIGHT_SCALE,SIGHT_SCALE,SIGHT_SCALE));
            Vector3 sightPosition = sightPos;
            Vector3 sightWorldPosition = newModel.localToWorldPoint(sightPosition);
            Vector3 moveDirection = Vector3.subtract(cameraWorldPosition, sightWorldPosition);

            Vector3 islandNewWorldPosition = Vector3.add(moveDirection, islandWorldPosition);
            newModel.setLocalScale(islandOriLocalScale);
            newModel.setWorldRotation(islandOriWorldRotation);

//            Vector3 scale = new Vector3(1,1,1);
//            newModel.setWorldScale(scale);

//            Vector3 sightPosition = sightPos;
//            Vector3 sightWorldPosition = newModel.localToWorldPoint(sightPosition);
//            Vector3 moveDirection = Vector3.subtract(cameraWorldPosition, sightWorldPosition);
//
//            Vector3 islandNewWorldPosition = Vector3.add(moveDirection, islandWorldPosition);
            islandSightWorldPos = islandNewWorldPosition;
            islandOriWorldPos = islandWorldPosition;
            moveWorldDirection = moveDirection;

//            newModel.setWorldPosition(islandNewWorldPosition);
            islandAnimator1(true);
        }
    }

    private void toOriSight(){
//        newModel.setWorldPosition(islandOriWorldPos);
//        newModel.setLocalScale(new Vector3(INIT_SCALE,INIT_SCALE,INIT_SCALE));
//        newModel.setWorldRotation(islandOriWorldRotation);
        islandAnimator1(false);
//        islandBackAnimator(true);
        newModel.getRotationController().setEnabled(true);
        newModel.getTranslationController().setEnabled(true);

    }
    private void islandAnimator1(boolean type){
        Property<TransformableNode, Quaternion> PROPERTY_ISLAND_ROTATE = new Property<TransformableNode, Quaternion>(Quaternion.class, "rotate") {
            @Override
            public Quaternion get(TransformableNode model) {

                return model.getWorldRotation();
            }
            @Override
            public void set(TransformableNode model, Quaternion qua) {
                model.setWorldRotation(qua);
            }
        };
        Property<TransformableNode, Float> PROPERTY_ISLAND_SCALE = new Property<TransformableNode, Float>(Float.class, "scale") {
            @Override
            public Float get(TransformableNode model) {

                return model.getLocalScale().x;
            }
            @Override
            public void set(TransformableNode model, Float scale) {
                model.setLocalScale(new Vector3(scale,scale,scale));
            }
        };

        Property<TransformableNode, Vector3> PROPERTY_ISLAND_TRANS = new Property<TransformableNode, Vector3>(Vector3.class, "scale") {
            @Override
            public Vector3 get(TransformableNode model) {

                return model.getWorldPosition();
            }
            @Override
            public void set(TransformableNode model, Vector3 pos) {
                model.setWorldPosition(pos);
            }
        };

        Property<TransformableNode, Float> PROPERTY_ISLAND_DOWN = new Property<TransformableNode, Float>(Float.class, "down") {
            @Override
            public Float get(TransformableNode model) {
                return model.getWorldPosition().y;
            }
            @Override
            public void set(TransformableNode model, Float y) {
                Vector3 vec = model.getWorldPosition();
                vec.y = y;
                model.setWorldPosition(vec);
            }
        };
        ObjectAnimator objAnimatorRotate;
        ObjectAnimator objAnimatorScale;
        ObjectAnimator objAnimatorTrans;
        Quaternion toRotation;
        Vector3 toPos;
        Float toScale;
        if(type){
            toRotation = islandSightWorldRotation;
            toScale = SIGHT_SCALE;
            toPos = islandSightWorldPos;
        } else{
            toRotation = islandOriWorldRotation;
            toScale = INIT_SCALE;
            toPos = islandOriWorldPos;
        }


        islandAnimeFlag = true;

        objAnimatorRotate = ObjectAnimator.ofObject(newModel, PROPERTY_ISLAND_ROTATE,new QuaternionEvaluator(), newModel.getWorldRotation(),toRotation);
        objAnimatorRotate.setDuration(2000);
//        objAnimatorRotate.start();

        objAnimatorScale = ObjectAnimator.ofFloat(newModel, PROPERTY_ISLAND_SCALE, newModel.getLocalScale().x,toScale);
        objAnimatorScale.setDuration(3000);
//        objAnimatorScale.start();

        objAnimatorTrans = ObjectAnimator.ofObject(newModel, PROPERTY_ISLAND_TRANS, new Vector3Evaluator(),newModel.getWorldPosition(),toPos);
        objAnimatorTrans.setDuration(3000);
//        objAnimatorTrans.start();

        AnimatorSet animSet = new AnimatorSet();
        if(type){
            animSet.play(objAnimatorTrans).with(objAnimatorScale).after(objAnimatorRotate);
        }else{
            animSet.play(objAnimatorTrans).with(objAnimatorScale).before(objAnimatorRotate);

        }
        animSet.start();


        objAnimatorTrans.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                islandAnimeFlag = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });

    }
    private void islandAnimator(boolean type){
        Property<TransformableNode, Quaternion> PROPERTY_ISLAND_ROTATE = new Property<TransformableNode, Quaternion>(Quaternion.class, "rotate") {
            @Override
            public Quaternion get(TransformableNode model) {

                return model.getWorldRotation();
            }
            @Override
            public void set(TransformableNode model, Quaternion qua) {
                model.setWorldRotation(qua);
            }
        };
        Property<TransformableNode, Float> PROPERTY_ISLAND_SCALE = new Property<TransformableNode, Float>(Float.class, "scale") {
            @Override
            public Float get(TransformableNode model) {

                return model.getLocalScale().x;
            }
            @Override
            public void set(TransformableNode model, Float scale) {
                model.setLocalScale(new Vector3(scale,scale,scale));
            }
        };

        Property<TransformableNode, Vector3> PROPERTY_ISLAND_TRANS = new Property<TransformableNode, Vector3>(Vector3.class, "scale") {
            @Override
            public Vector3 get(TransformableNode model) {

                return model.getWorldPosition();
            }
            @Override
            public void set(TransformableNode model, Vector3 pos) {
                model.setWorldPosition(pos);
            }
        };

        Property<TransformableNode, Float> PROPERTY_ISLAND_DOWN = new Property<TransformableNode, Float>(Float.class, "down") {
            @Override
            public Float get(TransformableNode model) {
                return model.getWorldPosition().y;
            }
            @Override
            public void set(TransformableNode model, Float y) {
                Vector3 vec = model.getWorldPosition();
                vec.y = y;
                model.setWorldPosition(vec);
            }
        };
        ObjectAnimator objAnimatorRotate;
        ObjectAnimator objAnimatorScale;
        ObjectAnimator objAnimatorDown;
        Quaternion toRotation;
        Float toDown;
        Float toScale;
        if(type){
            toRotation = islandSightWorldRotation;
            toScale = SIGHT_SCALE;
            toDown = -UP_DOWN_PARAM;
        }else{
            toRotation = islandOriWorldRotation;
            toScale = INIT_SCALE;
            toDown = newModel.getWorldPosition().y;//UP_DOWN_PARAM*INIT_SCALE*INIT_SCALE;
        }
        islandAnimeFlag = true;

        objAnimatorRotate = ObjectAnimator.ofObject(newModel, PROPERTY_ISLAND_ROTATE,new QuaternionEvaluator(), newModel.getWorldRotation(),toRotation);
        objAnimatorRotate.setDuration(1000);


        objAnimatorScale = ObjectAnimator.ofFloat(newModel, PROPERTY_ISLAND_SCALE, newModel.getLocalScale().x,toScale);
        objAnimatorScale.setDuration(3000);


        objAnimatorDown = ObjectAnimator.ofFloat(newModel, PROPERTY_ISLAND_DOWN, newModel.getWorldPosition().y,toDown);
        objAnimatorDown.setDuration(3000);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(objAnimatorDown).with(objAnimatorScale).after(objAnimatorRotate);
        animSet.start();


        objAnimatorScale.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);
                islandMiddlePos = newModel.getWorldPosition();
                Vector3 toPos;
                if(type)
                {
                    Frame frame = arFragment.getArSceneView().getArFrame();
                    Camera camera = frame.getCamera();
                    if (camera.getTrackingState() == TrackingState.TRACKING){
                        Pose cameraPose = camera.getDisplayOrientedPose();
                        cameraWorldPosition = new Vector3(cameraPose.tx(),cameraPose.ty(),cameraPose.tz());
                    }
                    Vector3 sightWorldPosition = newModel.localToWorldPoint(sightLocalPos);
                    Vector3 moveDirection = Vector3.subtract(cameraWorldPosition, sightWorldPosition);
                    Vector3 islandNewWorldPosition = Vector3.add(moveDirection, newModel.getWorldPosition());
                    toPos = islandNewWorldPosition;
                }else{
                    toPos = islandOriWorldPos;
                }
                ObjectAnimator objAnimatorTrans;
                objAnimatorTrans = ObjectAnimator.ofObject(newModel, PROPERTY_ISLAND_TRANS, new Vector3Evaluator(),newModel.getWorldPosition(),toPos);
                objAnimatorTrans.setDuration(1000);
                objAnimatorTrans.start();
                objAnimatorTrans.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        islandAnimeFlag = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });


    }

    private void islandBackAnimator(boolean type){
        Property<TransformableNode, Quaternion> PROPERTY_ISLAND_ROTATE = new Property<TransformableNode, Quaternion>(Quaternion.class, "rotate") {
            @Override
            public Quaternion get(TransformableNode model) {

                return model.getWorldRotation();
            }
            @Override
            public void set(TransformableNode model, Quaternion qua) {
                model.setWorldRotation(qua);
            }
        };
        Property<TransformableNode, Float> PROPERTY_ISLAND_SCALE = new Property<TransformableNode, Float>(Float.class, "scale") {
            @Override
            public Float get(TransformableNode model) {

                return model.getLocalScale().x;
            }
            @Override
            public void set(TransformableNode model, Float scale) {
                Vector3 cY = model.localToWorldPoint(plainY);
                Vector3 move = Vector3.subtract(staticY,cY);
                Vector3 pos = Vector3.add(model.getWorldPosition(),move);
                model.setWorldPosition(pos);
                model.setLocalScale(new Vector3(scale,scale,scale));
            }
        };

        Property<TransformableNode, Vector3> PROPERTY_ISLAND_TRANS = new Property<TransformableNode, Vector3>(Vector3.class, "scale") {
            @Override
            public Vector3 get(TransformableNode model) {

                return model.getWorldPosition();
            }
            @Override
            public void set(TransformableNode model, Vector3 pos) {
                model.setWorldPosition(pos);
            }
        };

        Property<TransformableNode, Float> PROPERTY_ISLAND_DOWN = new Property<TransformableNode, Float>(Float.class, "down") {
            @Override
            public Float get(TransformableNode model) {
                return model.getWorldPosition().y;
            }
            @Override
            public void set(TransformableNode model, Float y) {
                Vector3 vec = model.getWorldPosition();
                vec.y = y;
                model.setWorldPosition(vec);
            }
        };
        islandAnimeFlag = true;
        arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);
        Vector3 toPos;

        ObjectAnimator objAnimatorTrans;
        objAnimatorTrans = ObjectAnimator.ofObject(newModel, PROPERTY_ISLAND_TRANS, new Vector3Evaluator(),newModel.getWorldPosition(),islandMiddlePos);
        objAnimatorTrans.setDuration(1000);
        objAnimatorTrans.start();
        objAnimatorTrans.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator objAnimatorRotate;
                ObjectAnimator objAnimatorScale;
                ObjectAnimator objAnimatorDown;
                Quaternion toRotation;
                Float toDown;
                Float toScale;

                Frame frame = arFragment.getArSceneView().getArFrame();
                Camera camera = frame.getCamera();
                if (camera.getTrackingState() == TrackingState.TRACKING){
                    Pose cameraPose = camera.getDisplayOrientedPose();
                    cameraWorldPosition = new Vector3(cameraPose.tx(),cameraPose.ty(),cameraPose.tz());
                }

                plainY = newModel.worldToLocalPoint(cameraWorldPosition);
//                staticY = newModel.localToWorldPoint(islandMiddlePos);
                staticY = cameraWorldPosition;

                toRotation = islandOriWorldRotation;
                toScale = INIT_SCALE;
                toDown = UP_DOWN_PARAM*INIT_SCALE*INIT_SCALE*INIT_SCALE*INIT_SCALE;




                objAnimatorRotate = ObjectAnimator.ofObject(newModel, PROPERTY_ISLAND_ROTATE,new QuaternionEvaluator(), newModel.getWorldRotation(),toRotation);
                objAnimatorRotate.setDuration(1000);


                objAnimatorScale = ObjectAnimator.ofFloat(newModel, PROPERTY_ISLAND_SCALE, newModel.getLocalScale().x,toScale);
                objAnimatorScale.setDuration(3000);


                objAnimatorDown = ObjectAnimator.ofFloat(newModel, PROPERTY_ISLAND_DOWN, newModel.getWorldPosition().y,toDown);
                objAnimatorDown.setDuration(3000);

                AnimatorSet animSet = new AnimatorSet();
//                animSet.play(objAnimatorDown).with(objAnimatorScale).before(objAnimatorRotate);
                animSet.play(objAnimatorScale).before(objAnimatorRotate);
                animSet.start();


                objAnimatorScale.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator objAnimatorTrans;
                        objAnimatorTrans = ObjectAnimator.ofObject(newModel, PROPERTY_ISLAND_TRANS, new Vector3Evaluator(),newModel.getWorldPosition(),islandOriWorldPos);
                        objAnimatorTrans.setDuration(1000);
                        objAnimatorTrans.start();
                        objAnimatorTrans.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                islandAnimeFlag = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                });

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });


    }
}



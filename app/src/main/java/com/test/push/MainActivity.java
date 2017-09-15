package com.test.push;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.test.push.media.Config;
import com.test.push.media.MediaPublisher;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback2 {

    private static final String TAG = "MainActivity";

    private Button btnToggle,btnSwitch;
    private SurfaceView mSurfaceView;

    private SurfaceHolder mSurfaceHolder;
    private boolean isPublished;

    private MediaPublisher mMediaPublisher;

    private int pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");
        initView();
        mMediaPublisher = MediaPublisher
                .newInstance(new Config.Builder()
                        .setFps(20) // fps
                        .setMaxWidth(720) //视频的最大宽度
                        .setMinWidth(320) //视频的最小宽度
                        .setUrl("rtmp://192.168.1.109/live/1935")//推送的url
                        .build());
        mMediaPublisher.init();
    }

    private void initView() {
        btnToggle = (Button) findViewById(R.id.btn_toggle);
        btnSwitch = (Button) findViewById(R.id.btn_switch);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPublish();
            }
        });
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
                if(mMediaPublisher.getCurrentPos() == 0){
                    pos = 1;

                    mMediaPublisher.initVideoGatherer(MainActivity.this,1,mSurfaceHolder);
                }else {
                    pos =0;
                    mMediaPublisher.initVideoGatherer(MainActivity.this,0,mSurfaceHolder);
                }
            }
        });
    }

    private void switchPublish() {
        if (isPublished) {
            stop();
        } else {
            start();
        }
        btnToggle.setText(isPublished ? "停止" : "开始");
    }

    private void start() {
        //初始化声音采集
        mMediaPublisher.initAudioGatherer();
        //初始化编码器
        mMediaPublisher.initEncoders(pos);
        //开始采集
        mMediaPublisher.startGather();
        //开始编码
        mMediaPublisher.startEncoder();
        //开始推送
        mMediaPublisher.starPublish();
        isPublished = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        mMediaPublisher.initVideoGatherer(this, pos,mSurfaceHolder);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        stop();
    }

    private void stop() {
        mMediaPublisher.stopPublish();
        mMediaPublisher.stopGather();
        mMediaPublisher.stopEncoder();
        isPublished = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPublisher.release();
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
        mMediaPublisher.initVideoGatherer(MainActivity.this,pos, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}

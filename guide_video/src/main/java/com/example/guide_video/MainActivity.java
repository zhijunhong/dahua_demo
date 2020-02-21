package com.example.guide_video;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTvPath ;
    private TextView mTvPlay;
    private VideoView mVideoVioe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        bindEvent();

    }

    private void bindEvent() {
        mTvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.tvsmile;
                //设置路径文本
                mTvPath.setText(videoPath);
                //播放
                playVideo(videoPath);
            }
        });
    }

    /**
     * 播放视频
     * @param videoPath
     */
    private void playVideo(String videoPath) {
        mVideoVioe.setVideoPath(videoPath);
        //用来设置控制台样式
        mVideoVioe.setMediaController(null);
        //用来设置起始播放位置，为0表示从开始播放
        mVideoVioe.seekTo(0);
        //用来设置mp4播放器是否可以聚焦
        mVideoVioe.requestFocus();
        //开始播放
        mVideoVioe.start();
        //videoView.pause();暂停播放
    }

    private void initView() {
        mTvPath = findViewById(R.id.tv_path);
        mTvPlay = findViewById(R.id.tv_play);
        mVideoVioe = findViewById(R.id.videoView);
    }
}

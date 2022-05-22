package com.example4job1.musicplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example4job1.R;

public class PlayActivity extends AppCompatActivity  {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar volumeBar;
    SeekBar positionBar;
    Button playBtn;
    int totalTime;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        playBtn = (Button) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    playBtn.setBackgroundResource(R.drawable.stop);

                } else {
                    mediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.play);
                }
            }
        });

        if(ContextCompat.checkSelfPermission(PlayActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PlayActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        } else {
            initMediaPlayer();
        }

        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);
        totalTime = mediaPlayer.getDuration();

        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mediaPlayer.setVolume(volumeNum, volumeNum);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        if(!mediaPlayer.isPlaying()) break;
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}

                }
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);
            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

        public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }

    public void initMediaPlayer(){

        Intent intent = getIntent();
        String currentIndex = intent.getStringExtra("position");
        try{
            mediaPlayer.setDataSource(currentIndex);
            mediaPlayer.prepare();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "未授权，功能无法实现", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }

    }

    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


}

package com.example.mylittleobserver_android.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylittleobserver_android.R;

public class AudioPlayerActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button back;
    private Button pause;
    private Button forward;
    private SeekBar seekBar;
    boolean isPlaying = false;
    private Handler AudioHandler;
    private Runnable mRunnable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player);

        // View
        ImageView before = (ImageView)findViewById(R.id.beforebtn);
        TextView title = (TextView)findViewById(R.id.audio_text);
        AudioHandler = new Handler();
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Intent intent = getIntent();
        String audioTitle = intent.getExtras().getString("SelectedTitle");
        back = findViewById(R.id.late);
        pause = findViewById(R.id.pause);
        forward = findViewById(R.id.fast);

        mediaPlayer = MediaPlayer.create(this,R.raw.record_testfile);
        mediaPlayer.start();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 오디오 트랙으로 변경
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다음 오디오 트랙으로 변경
            }
        });
        title.setText(audioTitle);

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.sliding_up,R.anim.sliding_down);
            }
        });


    }

    protected void initializeSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration()/1000);


    }

}

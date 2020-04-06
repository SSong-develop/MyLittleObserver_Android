package com.example.mylittleobserver_android.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.example.mylittleobserver_android.R;

import java.util.Timer;
import java.util.TimerTask;

// 타이머 작업 끝내면 됨
public class AudioPlayerActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    AppCompatImageButton Late;
    AppCompatImageButton pause;
    AppCompatImageButton fast;
    AppCompatImageButton start;
    AppCompatImageButton restart;
    TextView startHour;
    TextView startMinute;
    TextView startSec;
    TextView endHour;
    TextView endMinute;
    TextView endSec;
    Handler handler = new Handler();
    int timer_sec;
    int timer_minute;
    boolean isPlaying = false;
    boolean start_and_pause = true;
    int audio_position;
    SeekBar seekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        // View
        TextView title = (TextView)findViewById(R.id.audio_text);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        Late = (AppCompatImageButton)findViewById(R.id.late);
        pause = (AppCompatImageButton)findViewById(R.id.pause);
        start = (AppCompatImageButton)findViewById(R.id.audio_start);
        restart = (AppCompatImageButton)findViewById(R.id.restart);
        fast = (AppCompatImageButton)findViewById(R.id.fast);
        startMinute = findViewById(R.id.startminute);
        startSec = findViewById(R.id.startsec);



        // toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.audioPlayerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("녹음 내용");
        Intent intent = getIntent();
        String audioTitle = intent.getExtras().getString("SelectedTitle");
        title.setText(audioTitle);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // seekbar touch하는 순간의 이벤트
                // progress는 seekbar가 진행된데 까지의 과정을 담은 인스턴스
                // fromUser는 사용자가 움직이면 true, 아니면 false의 값을 가지게 된다.
                if(fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // seekbar touch를 떼내는 순간의 이벤트

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // seekbar의 값이 변할 때의 이벤트
            }
        });

        // Button Event
        Late.setOnClickListener(v -> {

        });
        start.setOnClickListener(v ->{
            start.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
            // mediaplayer
            mediaPlayer = MediaPlayer.create(this,R.raw.ptest);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
            int a = mediaPlayer.getDuration();
            seekBar.setMax(a);
            isPlaying = true;
            new MusicThread().start();
            new TimerThread().start();
            startSec.setText("0"+timer_sec);
            startMinute.setText("0"+timer_minute);
        });
        pause.setOnClickListener(v -> {
            pause.setVisibility(View.INVISIBLE);
            restart.setVisibility(View.VISIBLE);
            audio_position = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            isPlaying = false;
            start_and_pause = true;
        });
        restart.setOnClickListener(v ->{
            pause.setVisibility(View.VISIBLE);
            start.setVisibility(View.INVISIBLE);
            restart.setVisibility(View.INVISIBLE);
            mediaPlayer.seekTo(audio_position);
            mediaPlayer.start();
            isPlaying = true;
            new MusicThread().start();
        });

        fast.setOnClickListener(v -> {

        });
    }

    class TimerThread extends Thread {
        boolean running = false;

        @Override
        public void run() {
            running = true;
            while(running){
                timer_sec+=1;
                if(timer_sec == 60){
                    timer_sec = 0;
                    timer_minute+=1;
                }
                try{
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MusicThread extends Thread {
        @Override
        public void run() {
            while(isPlaying) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home :
                if(isPlaying == false){
                    finish();
                    overridePendingTransition(R.anim.sliding_up,R.anim.sliding_down);
                }
                else {
                    isPlaying = false;
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    finish();
                    overridePendingTransition(R.anim.sliding_up,R.anim.sliding_down);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}

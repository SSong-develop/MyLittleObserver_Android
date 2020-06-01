package com.example.mylittleobserver_android.Activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 타이머 작업 끝내면 됨
public class AudioPlayerActivity extends AppCompatActivity {
    // view instance
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

    // URL
    String URL = "http://ec2-15-165-113-25.ap-northeast-2.compute.amazonaws.com:8080/";

    // response instance
    String fileDownloadUrl;

    // Retrofit
    Retrofit alarmControllRetrofit;
    Service alarmControllService;
    Call<ResponseBody> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        // getIntent
        Intent getIntent = getIntent();
        Long alarmId = getIntent.getExtras().getLong("alarmId");
        // Log.d("FuckAlarmId",alarmId.toString());
        String url = URL + "api/v1/alarms/" + alarmId + "/" + "record/";
        // Log.d("FuckUrl",url);
        // Log.i("hello_FuckingAlarmId",alarmId.toString());

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
        endMinute = findViewById(R.id.endminute);
        endSec = findViewById(R.id.endsec);

        // toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.audioPlayerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("녹음 내용");
        Intent intent = getIntent();
        String audioTitle = intent.getExtras().getString("SelectedTitle");
        title.setText(audioTitle);

        // Retrofit
        alarmControllRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        alarmControllService = alarmControllRetrofit.create(Service.class);
        call = alarmControllService.getAlarmStatus(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.body() == null){
                        Toast.makeText(AudioPlayerActivity.this, "파일이 없습니다 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    fileDownloadUrl = jsonObject.getString("fileDownloadUrl");
                    Log.v("FuckUp!",fileDownloadUrl);
                    Log.d("END Data",result);
                }catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });



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
            try {
                if(fileDownloadUrl == null){
                    Toast.makeText(this, "파일이 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, Uri.parse(fileDownloadUrl));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
            int a = mediaPlayer.getDuration();
            seekBar.setMax(a);
            isPlaying = true;
            new MusicThread().start();
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


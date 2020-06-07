package com.example.mylittleobserver_android.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mylittleobserver_android.Activities.AudioPlayerActivity;
import com.example.mylittleobserver_android.Activities.MainActivity;
import com.example.mylittleobserver_android.Adapter.ChildRecyclerAdapter;
import com.example.mylittleobserver_android.Adapter.MainRecyclerAdapter;
import com.example.mylittleobserver_android.Model.InsideItem;
import com.example.mylittleobserver_android.Model.Section;
import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Audio_Fragment extends Fragment {
    // 알림 리스트 가져오는 인터페이스를 mainActivity로 변경해
    // Instance
    private MainActivity activity;
    private RecyclerView audioRecyclerView;

    private ArrayList<Section> sectionList = new ArrayList<>();

    // RecyclerView
    private MainRecyclerAdapter mainAdapter;
    private ChildRecyclerAdapter childAdapter;
    // Retrofit
    private Retrofit alarmRetrofit;
    private Service alarmService;
    private Call<ResponseBody> call;
    private Long alarmId;

    //
    private boolean _switch = true;

    // URL
    String url = "http://ec2-15-165-113-25.ap-northeast-2.compute.amazonaws.com:8080/";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_audio, container, false);
        // View
        SwipeRefreshLayout mSwipeRefreshLayout = root.findViewById(R.id.swipe_layout);

        // toolbar
        Toolbar tb = (Toolbar) root.findViewById(R.id.audioToolbar_fragment);
        activity.setSupportActionBar(tb);
        activity.getSupportActionBar().setTitle("알림 목록");

        // MainActivity Bundle
        Bundle bundle = getArguments();
        String name = bundle.getString("userName");
        String mloName = bundle.getString("mloName");
        Long mloId = bundle.getLong("mloId");

        // convert url
        String _url = url + "/api/v1/mlos/" + mloName;
        Log.d("convert_url", _url);

        // alarmView구성을 위한 Retrofit
        alarmRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        alarmService = alarmRetrofit.create(Service.class);
        call = alarmService.getAlarm(_url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = null;
                if (response.isSuccessful()) {
                    try {
                        result = response.body().string();
                        // Json 처리 객체
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("alarms");

                        ArrayList<JSONObject> listAlarm = new ArrayList<>();

                        // View 구성
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            listAlarm.add(jsonArray1.getJSONObject(i));
                            initdata(listAlarm.get(i));
                        }

                        // RecyclerView
                        audioRecyclerView = root.findViewById(R.id.group_recyclerview);
                        mainAdapter = new MainRecyclerAdapter(sectionList);
                        mainAdapter.setOnItemClickListener(new MainRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Intent intent = new Intent(activity, AudioPlayerActivity.class);
                                alarmId = Long.valueOf(pos) + 1;
                                intent.putExtra("SelectedTitle", mainAdapter.getTitle(pos));
                                intent.putExtra("alarmId", alarmId);
                                // 여기를 조져야한다.
                                startActivity(intent);
                            }
                        });
                        mainAdapter.setOnItemLongClickListener(new MainRecyclerAdapter.OnItemLongClickListener() {
                            @Override
                            public void onItemLongClick(View v, int pos) {

                            }
                        });
                        audioRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                        audioRecyclerView.setHasFixedSize(true);
                        audioRecyclerView.setAdapter(mainAdapter);

                        DividerItemDecoration dividerItemDecoration =
                                new DividerItemDecoration(audioRecyclerView.getContext(), new LinearLayoutManager(activity).getOrientation());
                        audioRecyclerView.addItemDecoration(dividerItemDecoration);
                        _switch = false;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } // jsonException
                    catch (IOException e) {
                        e.printStackTrace();
                    } // IOException
                } else {
                    Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sectionList.clear();
                alarmRetrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                alarmService = alarmRetrofit.create(Service.class);
                call = alarmService.getAlarm(_url);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = null;
                        if (response.isSuccessful()) {
                            try {
                                result = response.body().string();
                                // Json 처리 객체
                                JSONArray jsonArray = new JSONArray(result);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                JSONArray jsonArray1 = jsonObject.getJSONArray("alarms");

                                ArrayList<JSONObject> listAlarm = new ArrayList<>();

                                // View 구성
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    listAlarm.add(jsonArray1.getJSONObject(i));
                                    initdata(listAlarm.get(i));
                                }

                                // RecyclerView
                                audioRecyclerView = root.findViewById(R.id.group_recyclerview);
                                mainAdapter = new MainRecyclerAdapter(sectionList);
                                mainAdapter.setOnItemClickListener(new MainRecyclerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int pos) {
                                        Intent intent = new Intent(activity, AudioPlayerActivity.class);
                                        alarmId = Long.valueOf(pos) + 1;
                                        intent.putExtra("SelectedTitle", mainAdapter.getTitle(pos));
                                        intent.putExtra("alarmId", alarmId);
                                        // 여기를 조져야한다.
                                        startActivity(intent);
                                    }
                                });
                                mainAdapter.setOnItemLongClickListener(new MainRecyclerAdapter.OnItemLongClickListener() {
                                    @Override
                                    public void onItemLongClick(View v, int pos) {

                                    }
                                });
                                audioRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                                audioRecyclerView.setHasFixedSize(true);
                                audioRecyclerView.setAdapter(mainAdapter);

                                DividerItemDecoration dividerItemDecoration =
                                        new DividerItemDecoration(audioRecyclerView.getContext(), new LinearLayoutManager(activity).getOrientation());
                                audioRecyclerView.addItemDecoration(dividerItemDecoration);
                                _switch = false;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } // jsonException
                            catch (IOException e) {
                                e.printStackTrace();
                            } // IOException
                        } else {
                            Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private void initdata(JSONObject jsonObject) {
        try {
            // Json data 형태들
            Long alarmId = jsonObject.getLong("alarmId"); // 알림 번호
            String stAlarmId = alarmId.toString(); // 알림 번호를 문자형으로 바꿔놓은거
            String title = null; // 알림 제목
            String heart = jsonObject.getString("heart"); // 심박수 수치
            String decibel = jsonObject.getString("decibel"); // 데시벨 수치
            String tumble = jsonObject.getString("tumble"); // 넘어짐 수치
            String date = jsonObject.getString("date"); // 날짜
            String new_date = null;
            String since = null;
            int hour = 0;
            int minute = 0;
            int sec = 0;

            // 1. Date 형태 바꿔주기
            SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            old_format.setTimeZone(TimeZone.getTimeZone("KST"));
            SimpleDateFormat new_format = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
            try {
                Date old_date = old_format.parse(date);
                new_date = new_format.format(old_date).substring(14);
                since = new_format.format(old_date).substring(6, 13);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Subalert title
            // subList로 오는 거 이름
            if (TextUtils.equals(heart, "high")) {
                title = "심박수 위험";
            } else if (TextUtils.equals(decibel, "high")) {
                title = "데시벨 위험";
            } else if (TextUtils.equals(tumble, "fall")) {
                title = "넘어짐";
            }

            /*
            if(TextUtils.equals(heart,"testhigh") && TextUtils.equals(decibel,"testhigh") && TextUtils.equals(tumble,"testfail")){
                // 3개가 전부 오는 경우
                title = "(Test)심박,데시벨,넘어짐 위험";
            } else if(TextUtils.equals(heart,"testhigh") && TextUtils.equals(decibel,"testhigh") && TextUtils.equals(tumble,null)){
                // 심박, 데시벨 만 오는 경우
                title = "(Test)심박,데시벨 위험";
            } else if(TextUtils.equals(heart,"testhigh") && TextUtils.equals(decibel,null) && TextUtils.equals(tumble,"testfail")){
                // 심박, 넘어짐 만 오는 경우
                title = "(Test)심박,넘어짐 위험";
            } else if(TextUtils.equals(heart,null) && TextUtils.equals(decibel,"testhigh") && TextUtils.equals(tumble,"testfail")){
                // 데시벨, 넘어짐 만 오는 경우
                title = "(Test)데시벨,넘어짐 위험";
            } else if(TextUtils.equals(heart,"testhigh") && TextUtils.equals(decibel,null) && TextUtils.equals(tumble,null)){
                // 심박만
                title = "(Test)심박수 위험";
            } else if(TextUtils.equals(heart,null) && TextUtils.equals(decibel,"testhigh") && TextUtils.equals(tumble,null)){
                // 데시벨 만
                title = "(Test)데시벨 위험";
            } else {
                title = "(Test)넘어짐 위험";
            }
            */

            // 구현해야 할 것
            // 초 단위에 따라서 위험한 것을 밑에 달리도록 해야한다.
            // 그렇다면 먼저 해주어야 할 것은
            // sectionItems가 시간대에 따라서 분류가 되어야 한다는 것이다.
            String sectionName = since;
            ArrayList<InsideItem> sectionItems = new ArrayList<>();
            sectionItems.add(new InsideItem(stAlarmId, title, new_date));
            sectionList.add(new Section(sectionName, sectionItems));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}



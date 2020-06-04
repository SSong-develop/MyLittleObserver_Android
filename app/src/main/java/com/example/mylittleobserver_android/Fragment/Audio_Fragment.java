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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylittleobserver_android.Activities.AudioPlayerActivity;
import com.example.mylittleobserver_android.Activities.MainActivity;
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
    private ArrayList<Section> sectionList = new ArrayList<>();
    private RecyclerView audioRecyclerView;

    // Retrofit
    private Retrofit alarmRetrofit;
    private Service alarmService;
    private Call<ResponseBody> call;
    private Long alarmId;

    // URL
    String url = "http://ec2-15-165-113-25.ap-northeast-2.compute.amazonaws.com:8080/";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_audio,container,false);

        // toolbar
        Toolbar tb = (Toolbar)root.findViewById(R.id.audioToolbar_fragment);
        activity.setSupportActionBar(tb);
        activity.getSupportActionBar().setTitle("녹음파일 목록");

        // MainActivity Bundle
        Bundle bundle = getArguments();
        String name = bundle.getString("userName");
        String mloName = bundle.getString("mloName");
        Long mloId = bundle.getLong("mloId");

        // convert url
        String _url = url + "/api/v1/mlos/" + mloName;
        Log.d("convert_url",_url);

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
                if(response.isSuccessful()){
                    try{
                        result = response.body().string();
                        // Log.v("FuckData",result);
                        // Json 처리 객체
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("alarms");

                        ArrayList<JSONObject> listAlarm = new ArrayList<>();

                        // View 구성
                        // 조건문만 없애놓으면 이전 코드
                        for(int i = 0;i<jsonArray1.length();i++){
                            listAlarm.add(jsonArray1.getJSONObject(i));
                            initdata(listAlarm.get(i));
                        }

                        // RecyclerView
                        audioRecyclerView = root.findViewById(R.id.group_recyclerview);
                        MainRecyclerAdapter mainAdapter = new MainRecyclerAdapter(sectionList);
                        mainAdapter.setOnItemClickListener(new MainRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Intent intent = new Intent(activity, AudioPlayerActivity.class);
                                alarmId = Long.valueOf(pos)+1;
                                intent.putExtra("SelectedTitle",mainAdapter.getTitle(pos));
                                intent.putExtra("alarmId",alarmId);
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
                                new DividerItemDecoration(audioRecyclerView.getContext(),new LinearLayoutManager(activity).getOrientation());
                        audioRecyclerView.addItemDecoration(dividerItemDecoration);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } // jsonException
                    catch (IOException e) {
                        e.printStackTrace();
                    } // IOException
                }
                else {
                    Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void initdata(JSONObject jsonObject) {
        try {
            Long alarmId = jsonObject.getLong("alarmId");
            String stAlarmId = alarmId.toString();

            String title = null;
            String heart = jsonObject.getString("heart");
            String decibel = jsonObject.getString("decibel");
            String tumble = jsonObject.getString("tumble");
            String date = jsonObject.getString("date");

            // alert title
            if(TextUtils.equals(heart,"testhigh")){
                title = "(Test)심박수 위험";
            } else if (TextUtils.equals(decibel,"testhigh")) {
                title = "(Test)데시벨 위험";
            } else if (TextUtils.equals(tumble,"testfall")){
                title = "(Test)넘어짐";
            }

            // 구현해야 할 것
            // 초 단위에 따라서 위험한 것을 밑에 달리도록 해야한다.
            String sectionName = "알림";
            ArrayList<InsideItem> sectionItems = new ArrayList<>();
            sectionItems.add(new InsideItem(stAlarmId,title,date));
            sectionList.add(new Section(sectionName,sectionItems));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

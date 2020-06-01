package com.example.mylittleobserver_android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;

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
            int sectionDevider = 0;
            Long alarmId = jsonObject.getLong("alarmId");
            String stAlarmId = alarmId.toString();

            // 이부분은 데이터형 물어보고 차차
            /*int heart = jsonObject.getInt("heart");
            int decibel = jsonObject.getInt("decibel"); */
            String tumble = jsonObject.getString("tumble");
            String date = jsonObject.getString("date");

            String title = tumble;

            // 살릴 부분입니다.
            /*if(heart > decibel){
                // heart를 title
                title = "심박수 증가!!";
            } else if (heart < decibel) {
                // decibel
                title = "데시벨 증가!!";
            } else {
                // 동일할 경우 넘어짐을
                title = tumble;
            }*/

            String sectionName = "ALARMFILE_"+sectionDevider;
            ArrayList<InsideItem> sectionItems = new ArrayList<>();
            sectionItems.add(new InsideItem(stAlarmId,title,date));
            sectionList.add(new Section(sectionName,sectionItems));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*private void initData() {

        // dummy Data
        String sectionOneName = "MLOFILE_01";
        ArrayList<InsideItem> sectionOneItems = new ArrayList<>();
        sectionOneItems.add(new InsideItem("심박수 감지","10:02"));
        sectionOneItems.add(new InsideItem("데시벨 감지","10:09"));

        String sectionTwoName = "MLOFILE_02";
        ArrayList<InsideItem> sectionTwoItems = new ArrayList<>();
        sectionTwoItems.add(new InsideItem("심박수 감지","13:16"));
        sectionTwoItems.add(new InsideItem("데시벨 감지","13:21"));
        sectionTwoItems.add(new InsideItem("넘어짐 감지","13:23"));

        String sectionThreeName = "MLOFILE_03";
        ArrayList<InsideItem> sectionThreeItems = new ArrayList<>();
        sectionThreeItems.add(new InsideItem("심박수 감지","16:20"));
        sectionThreeItems.add(new InsideItem("데시벨 감지","16:22"));
        sectionThreeItems.add(new InsideItem("넘어짐 감지","16:26"));

        sectionList.add(new Section(sectionOneName,sectionOneItems));
        sectionList.add(new Section(sectionTwoName,sectionTwoItems));
        sectionList.add(new Section(sectionThreeName,sectionThreeItems));
    }*/
}

package com.example.mylittleobserver_android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.mylittleobserver_android.Activities.MainActivity;
import com.example.mylittleobserver_android.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class ManageDevice_Fragment extends Fragment {

    // api/v1/users/{userName}/mlos -> userName에 따라 그가 가진 mlo가 무엇인지를 알 수 있다.

    // api/v1/mlos/{mloName}/alarms -> mloName에 따라 알람의 상태를 알 수 있다.

    MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_manage, container, false);

        // view
        MaterialButton button = root.findViewById(R.id.testbutton);
        TextInputEditText test = root.findViewById(R.id.testedit);
        TextInputEditText test1 = root.findViewById(R.id.useredit1);
        Toolbar toolbar = root.findViewById(R.id.search_toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(" ");

        return root;
    }

}

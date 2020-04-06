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
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.mylittleobserver_android.Activities.MainActivity;
import com.example.mylittleobserver_android.Database.AppDatabase;
import com.example.mylittleobserver_android.Database.User;
import com.example.mylittleobserver_android.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class ManageDevice_Fragment extends Fragment {
    MainActivity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_manage,container,false);
        MaterialButton button = root.findViewById(R.id.testbutton);
        TextInputEditText test = root.findViewById(R.id.testedit);
        TextInputEditText test1 = root.findViewById(R.id.useredit1);
        TextView page = root.findViewById(R.id.testpage);

        final AppDatabase db = Room.databaseBuilder(activity,AppDatabase.class,"user-db")
                .allowMainThreadQueries()
                .build();

        page.setText(db.userDao().getAll().toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.userDao().insert(new User(test1.getText().toString(),test.getText().toString()));
                page.setText(db.userDao().getAll().toString());
            }
        });
        return root;
    }

}

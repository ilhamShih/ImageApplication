package com.example.imageapplication.activity;


import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imageapplication.R;
import com.example.imageapplication.helper.SessionManager;

public class ActivitySettings extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

     Switch switch1;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setings);
        session = new SessionManager(getApplicationContext());
        switch1 = findViewById(R.id.switch1);

        if (switch1 != null) {
            switch1.setOnCheckedChangeListener(this);
        }
        shouViews();
}

    public void shouViews() {
        switch1.setChecked(session.isLoggedIn());
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(this, (isChecked ? "Подписались" : "Отписались"),
               Toast.LENGTH_SHORT).show();
        if(isChecked) {
            session.setLogin(true);
        } else {
            session.setLogin(false);
        }
    }






}

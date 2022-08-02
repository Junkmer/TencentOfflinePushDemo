package com.junkmen.offline.push.huaweipush;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Map;

public class PushDisplayActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_display);

        textView = findViewById(R.id.textView);

        getHuaWeiPushIntentData();
    }

    private void getHuaWeiPushIntentData(){
        Bundle bundle = getIntent().getExtras();
        String extContent = bundle.getString("ext");
        Log.e("push_data",extContent);
        textView.setText("透传内容为："+extContent);
    }

}
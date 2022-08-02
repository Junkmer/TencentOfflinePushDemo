package com.junkmen.offline.push.oppopush;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.Set;


public class PushDisplayActivity extends AppCompatActivity {
    private final static String TAG = PushDisplayActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_display);

        textView = findViewById(R.id.textView);

        getOPPOPushIntentData();
    }

    @SuppressLint("SetTextI18n")
    private void getOPPOPushIntentData() {
        Bundle bundle = getIntent().getExtras();
        Set<String> set = bundle.keySet();
        if (set != null) {
            for (String key : set) {
                Object value = bundle.get(key);
                if (TextUtils.equals("extKey", key)) {
                    textView.setText("透传内容为："+value.toString());
                }
            }
        }
    }
}

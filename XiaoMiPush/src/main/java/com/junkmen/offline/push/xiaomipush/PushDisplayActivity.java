package com.junkmen.offline.push.xiaomipush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

import java.util.Map;
import java.util.Set;

public class PushDisplayActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_display);

        textView = findViewById(R.id.textView);

        getXiaoMiPushIntentData();

        Log.e("push_data","+++++++++++++++++++++++++++++++++++++++++++");
    }

    private void getXiaoMiPushIntentData(){
        Bundle bundle = getIntent().getExtras();
        String extContent = "default";
        if ( null != bundle.get("ext")){
            extContent = "配置的打开应用：" + (String) bundle.get("ext");
        }else {
            MiPushMessage miPushMessage = (MiPushMessage)bundle.getSerializable(PushMessageHelper.KEY_MESSAGE);
            Map extra = miPushMessage.getExtra();
            extContent = "配置的跳转到指定页面："+(String) extra.get("ext");
        }
        Log.e("push_data",extContent);
        textView.setText("透传内容为："+extContent);
    }

}

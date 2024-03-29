package com.junker.tencent.im.googlepush;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.junker.tencent.im.googlepush.constants.PushConstants;
import com.junker.tencent.im.googlepush.push.ThirdPushTokenMgr;
import com.junker.tencent.im.googlepush.signature.GenerateTestUserSig;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText loginUser;
    private EditText receiveUser;
    private TextView pushContentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginUser = findViewById(R.id.login_user);
        receiveUser = findViewById(R.id.receive_user);
        pushContentText = findViewById(R.id.push_content);

        jumpIntent();

        getGooglePushIntentData();
    }

    private void getGooglePushIntentData(){
        Bundle bundle = getIntent().getExtras();
        if (null != bundle){
            String extContent = bundle.getString("ext");
            Log.e("push_data",extContent);
            pushContentText.setText("透传内容为："+extContent);
        }
    }

    private void jumpIntent(){
        Intent intent = new Intent(this, PushDisplayActivity.class);
        intent.setData(Uri.parse("pushscheme://im.push/jump"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
        Log.i("TAG", "指定跳转页面地址 intentUri = " + intentUri);
    }

    public void login_im(View view){
        String userId = loginUser.getText().toString().trim();
        String userSig = GenerateTestUserSig.genTestUserSig(userId);

        V2TIMManager.getInstance().login(userId, userSig, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                Log.e("TAG","登录失败");
            }

            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                Log.e("TAG","登录成功");
                ThirdPushTokenMgr.getInstance().setPushTokenToV2TIM();
            }
        });
    }

    public void send_push_msg(View view){
        JSONObject jsonObject = new JSONObject();
        String currentTime = getCurrentTime();
        try {
            jsonObject.put("extKey", "ext content："+ currentTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String extContent = jsonObject.toString();

        V2TIMOfflinePushInfo v2TIMOfflinePushInfo = new V2TIMOfflinePushInfo();
        v2TIMOfflinePushInfo.setAndroidOPPOChannelID(PushConstants.OPPO_PUSH_CHANNELID);
        v2TIMOfflinePushInfo.setExt(extContent.getBytes());

        V2TIMMessage message = V2TIMManager.getMessageManager().createTextMessage("新密码："+ currentTime);
        String s = receiveUser.getText().toString();
        V2TIMManager.getMessageManager().sendMessage(message, s, null, V2TIMMessage.V2TIM_PRIORITY_DEFAULT, false,  v2TIMOfflinePushInfo, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int i) {
                Log.e("TAG","消息发送中");
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this,"消息发送失败",Toast.LENGTH_SHORT).show();
                Log.e("TAG","消息发送失败");
            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                Toast.makeText(MainActivity.this,"消息发送成功",Toast.LENGTH_SHORT).show();
                Log.e("TAG","消息发送成功");
            }
        });
    }

    public void jumpActivity(View view){
        Intent intent = new Intent(MainActivity.this,PushDisplayActivity.class);
        startActivity(intent);
        finish();
    }

    private String getCurrentTime(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmss");
        String format = s.format(new Date());
        return format;
    }

}

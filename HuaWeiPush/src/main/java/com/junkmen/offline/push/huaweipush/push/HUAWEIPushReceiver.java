package com.junkmen.offline.push.huaweipush.push;

import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class HUAWEIPushReceiver extends HmsMessageService {

    private static final String TAG = HUAWEIPushReceiver.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.i(TAG, "onMessageReceived message=" + message);
    }

    @Override
    public void onMessageSent(String msgId) {
        Log.i(TAG, "onMessageSent msgId=" + msgId);
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        Log.i(TAG, "onSendError msgId=" + msgId);
    }

    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "onNewToken token=" + token);

        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
    }

    @Override
    public void onTokenError(Exception exception) {
        Log.i(TAG, "onTokenError exception=" + exception);
    }

    @Override
    public void onMessageDelivered(String msgId, Exception exception) {
        Log.i(TAG, "onMessageDelivered msgId=" + msgId);
    }

}

package com.junker.tencent.im.googlepush.push;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;


public class GoogleFCMMsgService extends FirebaseMessagingService {
    private final String TAG = GoogleFCMMsgService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.i(TAG, "onNewToken token: " + token);
        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
    }
}

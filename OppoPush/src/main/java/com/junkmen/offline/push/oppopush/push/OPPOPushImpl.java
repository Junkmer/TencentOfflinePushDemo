package com.junkmen.offline.push.oppopush.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.heytap.msp.push.callback.ICallBackResultService;

public class OPPOPushImpl implements ICallBackResultService {

    private static final String TAG = OPPOPushImpl.class.getSimpleName();

    @Override
    public void onRegister(int responseCode, String registerID) {
        Log.i(TAG, "onRegister responseCode: " + responseCode + " registerID: " + registerID);

        ThirdPushTokenMgr.getInstance().setThirdPushToken(registerID);
    }

    @Override
    public void onUnRegister(int responseCode) {
        Log.i(TAG, "onUnRegister responseCode: " + responseCode);
    }

    @Override
    public void onSetPushTime(int responseCode, String s) {
        Log.i(TAG, "onSetPushTime responseCode: " + responseCode + " s: " + s);
    }

    @Override
    public void onGetPushStatus(int responseCode, int status) {
        Log.i(TAG, "onGetPushStatus responseCode: " + responseCode + " status: " + status);
    }

    @Override
    public void onGetNotificationStatus(int responseCode, int status) {
        Log.i(TAG, "onGetNotificationStatus responseCode: " + responseCode + " status: " + status);
    }

    @Override
    public void onError(int i, String s) {
        Log.i(TAG, "onError code: " + i + " string: " + s);
    }

    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "oppotest";
            String description = "this is opptest";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("oppo_channel_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

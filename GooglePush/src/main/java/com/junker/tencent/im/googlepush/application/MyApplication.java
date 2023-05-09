package com.junker.tencent.im.googlepush.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.internal.FidListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.junker.tencent.im.googlepush.constants.PushConstants;
import com.junker.tencent.im.googlepush.push.ThirdPushTokenMgr;
import com.junker.tencent.im.googlepush.signature.GenerateTestUserSig;
import com.tencent.imsdk.v2.V2TIMManager;

import java.util.Objects;

public class MyApplication extends Application {
    private final static String TAG = MyApplication.class.getSimpleName();
    private static Context context;

    public static Context getInstance(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initSDK();
    }

    private void initSDK() {
        //初始化IM
        V2TIMManager.getInstance().initSDK(this, GenerateTestUserSig.SDKAPPID, null);

        // 谷歌离线推送
//        if (isGoogleServiceSupport()) {
            Log.e(TAG, "init  google push service");
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.e(TAG, "getInstanceId failed exception = " + task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            Log.i(TAG, "google fcm getToken = " + token);

                            ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                        }
                    });
//        }

        //添加 Activity活动监听
        registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());
    }

    public boolean isGoogleServiceSupport() {
        GoogleApiAvailabilityLight googleApiAvailability = GoogleApiAvailabilityLight.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        return resultCode == ConnectionResult.SUCCESS;
    }

    static class StatisticActivityLifecycleCallback implements ActivityLifecycleCallbacks {
        private int foregroundActivities = 0;
        private boolean isChangingConfiguration;

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            Log.i(TAG, "onActivityCreated bundle: " + bundle);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // 应用切到前台
                V2TIMManager.getOfflinePushManager().doForeground(null);
            }
            isChangingConfiguration = false;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            foregroundActivities--;
            if (foregroundActivities == 0) {
                // 应用切到后台
                V2TIMManager.getOfflinePushManager().doBackground(0, null);
            }
            isChangingConfiguration = activity.isChangingConfigurations();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}

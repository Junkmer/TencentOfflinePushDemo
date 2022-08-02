package com.junkmen.offline.push.huaweipush.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;
import com.junkmen.offline.push.huaweipush.push.ThirdPushTokenMgr;
import com.junkmen.offline.push.huaweipush.signature.GenerateTestUserSig;
import com.tencent.imsdk.v2.V2TIMManager;

/**
 * 创建者 junkmen
 * 时间： 2020/9/1
 * 说明：
 */
public class MyApplication extends Application {
    private final static String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        initV2SDK();
    }

    private void initV2SDK() {
        //初始化IM
        V2TIMManager.getInstance().initSDK(this, GenerateTestUserSig.SDKAPPID, null);

        // 华为离线推送
        // 华为离线推送，设置是否接收Push通知栏消息调用示例
        HmsMessaging.getInstance(getApplicationContext()).turnOnPush().addOnCompleteListener(new com.huawei.hmf.tasks.OnCompleteListener<Void>() {
            @Override
            public void onComplete(com.huawei.hmf.tasks.Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "huawei turnOnPush Complete");
                } else {
                    Log.e(TAG, "huawei turnOnPush failed: ret=" + task.getException().getMessage());
                }
            }
        });
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(getApplicationContext()).getString("client/app_id");
                    String token = HmsInstanceId.getInstance(getApplicationContext()).getToken(appId, "HCM");
                    Log.i(TAG, "huawei get token:" + token);
                    if(!TextUtils.isEmpty(token)) {
                        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                    }
                } catch (ApiException e) {
                    Log.e(TAG, "huawei get token failed, " + e);
                }
            }
        }.start();

        //添加 Activity活动监听
        registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());
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

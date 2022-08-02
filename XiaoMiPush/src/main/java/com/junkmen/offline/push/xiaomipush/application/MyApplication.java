package com.junkmen.offline.push.xiaomipush.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.junkmen.offline.push.xiaomipush.constants.PushConstants;
import com.junkmen.offline.push.xiaomipush.signature.GenerateTestUserSig;

import com.tencent.imsdk.v2.V2TIMManager;
import com.xiaomi.mipush.sdk.MiPushClient;

public class MyApplication extends Application {
    private final static String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    private void initSDK() {
        //初始化IM
        V2TIMManager.getInstance().initSDK(this, GenerateTestUserSig.SDKAPPID, null);

        // 小米离线推送
        MiPushClient.registerPush(this, PushConstants.XM_PUSH_APPID, PushConstants.XM_PUSH_APPKEY);

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

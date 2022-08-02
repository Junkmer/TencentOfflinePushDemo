package com.junkmen.offline.push.oppopush.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.heytap.msp.push.HeytapPushManager;
import com.junkmen.offline.push.oppopush.constants.PushConstants;
import com.junkmen.offline.push.oppopush.push.OPPOPushImpl;
import com.junkmen.offline.push.oppopush.signature.GenerateTestUserSig;
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

        // Oppo推送
        HeytapPushManager.init(getApplicationContext(), false);
        if (HeytapPushManager.isSupportPush(getApplicationContext())) {
            // oppo离线推送
            OPPOPushImpl oppo = new OPPOPushImpl();
            oppo.createNotificationChannel(getApplicationContext());
            HeytapPushManager.register(getApplicationContext(), PushConstants.OPPO_PUSH_APPKEY, PushConstants.OPPO_PUSH_APPSECRET, oppo);

            // OPPO 手机默认关闭通知，需要申请
            HeytapPushManager.requestNotificationPermission();
        }

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

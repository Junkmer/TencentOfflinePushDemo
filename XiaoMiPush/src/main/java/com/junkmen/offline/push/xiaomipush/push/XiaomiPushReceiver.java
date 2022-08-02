package com.junkmen.offline.push.xiaomipush.push;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;
import java.util.Map;

public class XiaomiPushReceiver extends PushMessageReceiver {
    private static final String TAG = XiaomiPushReceiver.class.getSimpleName();
    private String mRegId;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        Log.d(TAG, "onReceivePassThroughMessage is called. ");
    }

    /**
     * 用来接收服务器发来的通知栏消息，配置 "打开应用" 触发
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        Log.d(TAG, "onNotificationMessageClicked miPushMessage " + miPushMessage.toString());

        // 如果聊天消息，跳转到聊天界面
        Map<String, String> extra = miPushMessage.getExtra();
        String ext = extra.get("ext");
        if (TextUtils.isEmpty(ext)) {
            Log.w(TAG, "onNotificationMessageClicked: no extra data found");
            return;
        }
        /*Intent intent = new Intent(DemoApplication.instance(), PushDisplayActivity.class);
        intent.putExtra("ext", ext);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DemoApplication.instance().startActivity(intent);*/
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        Log.d(TAG, "onNotificationMessageArrived is called. ");
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        Log.d(TAG, "onReceiveRegisterResult is called. " + miPushCommandMessage.toString());
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);

        Log.d(TAG, "cmd: " + command + " | arg: " + cmdArg1
                + " | result: " + miPushCommandMessage.getResultCode() + " | reason: " + miPushCommandMessage.getReason());

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }

        Log.d(TAG, "regId: " + mRegId);
        ThirdPushTokenMgr.getInstance().setThirdPushToken(mRegId);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }

}

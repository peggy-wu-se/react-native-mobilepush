package com.reactlibrary;

import android.content.Context;

import android.util.Log;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Map;

public class PushReceiver extends MessageReceiver{

    public static ReactApplicationContext context;
    public static PushReceiver instance;
    private final String PUSH_TYPE_MESSAGE = "message";
    private final String PUSH_TYPE_NOTIFICATION = "notification";

    public PushReceiver() {
        super();
        instance = this;
    }

    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        WritableMap params = Arguments.createMap();
        params.putString("messageId", cPushMessage.getMessageId());
        params.putString("body", cPushMessage.getContent());
        params.putString("title", cPushMessage.getTitle());
        params.putString("type", PUSH_TYPE_MESSAGE);
        sendEvent("aliyunPushReceived", params);
    }

    @Override
    protected void onNotification(Context context, String title, String content, Map<String, String> extraMap) {
        WritableMap params = Arguments.createMap();
        params.putString("body", content);
        params.putString("title", title);
        WritableMap extraWritableMap = Arguments.createMap();
        for (Map.Entry<String, String> entry : extraMap.entrySet()) {
            extraWritableMap.putString(entry.getKey(),entry.getValue());
        }
        params.putMap("extras", extraWritableMap);
        params.putString("type", PUSH_TYPE_NOTIFICATION);
        sendEvent("aliyunPushReceived", params);
    }

    @Override
    protected void onNotificationOpened(Context context, String title, String content, String extraMap) {
        WritableMap params = Arguments.createMap();
        params.putString("body", content);
        params.putString("title", title);
        params.putString("extras", extraMap);
        params.putString("type", PUSH_TYPE_NOTIFICATION);
        params.putString("actionIdentifier", "opened");
        sendEvent("aliyunPushReceived", params);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String content, String extraMap) {
        WritableMap params = Arguments.createMap();
        params.putString("body", content);
        params.putString("title", title);
        params.putString("extras", extraMap);
        params.putString("type", PUSH_TYPE_NOTIFICATION);
        params.putString("actionIdentifier", "opened");
        sendEvent("aliyunPushReceived", params);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId){
        WritableMap params = Arguments.createMap();
        params.putString("messageId", messageId);
        params.putString("type", PUSH_TYPE_NOTIFICATION);
        params.putString("actionIdentifier", "removed");
        sendEvent("aliyunPushReceived", params);
    }

    @Override
    public void onNotificationReceivedInApp(Context context, String title, String content, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        WritableMap params = Arguments.createMap();
        params.putString("content", content);
        params.putString("title", title);
        params.putString("openType", String.valueOf(openType));
        params.putString("openActivity", openActivity);
        params.putString("openUrl", openUrl);

        WritableMap extraWritableMap = Arguments.createMap();
        for (Map.Entry<String, String> entry : extraMap.entrySet()) {
            extraWritableMap.putString(entry.getKey(),entry.getValue());
        }
        params.putMap("extras", extraWritableMap);
        params.putString("type", PUSH_TYPE_NOTIFICATION);
        sendEvent("aliyunPushReceived", params);
    }

    public static void sendEvent(String eventName, WritableMap params) {
      Log.e("sendEvent",eventName+";"+params);
        if (!RNMobilepushModule.isRead) {
            params.putString("appState", "background");
            initialMessage = params;
        }else{
            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }
    public static WritableMap initialMessage = null;
}

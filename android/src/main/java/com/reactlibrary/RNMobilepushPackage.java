
package com.reactlibrary;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;
public class RNMobilepushPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
      return Arrays.<NativeModule>asList(new RNMobilepushModule(reactContext));
    }

    // Deprecated from RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
    }

    static String TAG = "PUSH_CONFIG";
    public static void initPushConfig(final Context applicationContext,String channel,String channelDesc) {
        Log.e(TAG,"initPushConfig");
        createNotificationChannel(applicationContext,channel,channelDesc);
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.setLogLevel(CloudPushService.LOG_DEBUG);
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.w(TAG,response);
            }
            @Override
            public void onFailed(String code, String message) {
                Log.e(TAG,message);
            }
        });
        HuaWeiRegister.register((Application) applicationContext);
        String pkName = applicationContext.getPackageName();
        try{
            ApplicationInfo appInfo = applicationContext.getPackageManager().getApplicationInfo(pkName, PackageManager.GET_META_DATA);
            Bundle metaBundle = appInfo.metaData;
            String xiaomiId = metaBundle.getString("xiaomiId","");
            String xiaomiKey = metaBundle.getString("xiaomiKey","");
            Log.e(TAG,"xiaomiID:"+xiaomiId+";xiaomiKey"+xiaomiKey);
            MiPushRegister.register(applicationContext, xiaomiId, xiaomiKey);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void createNotificationChannel(Context ctx,String channel,String channelDesc) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = channel;
            // 用户可以看到的通知渠道的描述
            String description = channelDesc;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }
}

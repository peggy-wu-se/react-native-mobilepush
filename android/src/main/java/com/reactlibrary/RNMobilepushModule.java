
package com.reactlibrary;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;

public class RNMobilepushModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

  private final ReactApplicationContext reactContext;

  public RNMobilepushModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addLifecycleEventListener(this);
    PushReceiver.context = reactContext;
  }

  @Override
  public String getName() {
    return "RNMobilepush";
  }

  @ReactMethod
  public void getDeviceId(final Promise promise) {
    String deviceID = PushServiceFactory.getCloudPushService().getDeviceId();
    if (deviceID!=null && deviceID.length()>0) {
      promise.resolve(deviceID);
    } else {
      // 或许还没有初始化完成，等3秒钟再次尝试
      try{
        Thread.sleep(3000);
        deviceID = PushServiceFactory.getCloudPushService().getDeviceId();

        if (deviceID!=null && deviceID.length()>0) {
          promise.resolve(deviceID);
          return;
        }
      } catch (Exception e) {

      }

      promise.reject("getDeviceId() failed.");
    }
  }

  @ReactMethod
  public void setApplicationIconBadgeNumber(int badgeNumber, final Promise promise) {

  }

  @ReactMethod
  public void getApplicationIconBadgeNumber(Callback callback) {
    callback.invoke(0);
  }

  @ReactMethod
  public void bindAccount(String account, final Promise promise) {
    PushServiceFactory.getCloudPushService().bindAccount(account, new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        promise.resolve(response);
      }
      @Override
      public void onFailed(String code, String message) {
        promise.reject(code, message);
      }
    });
  }

  @ReactMethod
  public void unbindAccount(final Promise promise) {
    PushServiceFactory.getCloudPushService().unbindAccount(new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        promise.resolve(response);
      }
      @Override
      public void onFailed(String code, String message) {
        promise.reject(code, message);
      }
    });
  }

  @ReactMethod
  public void bindTag(int target, ReadableArray tags, String alias, final Promise promise) {

    String[] tagStrs = new String[tags.size()];
    for(int i=0; i<tags.size();i++) tagStrs[i] = tags.getString(i);

    PushServiceFactory.getCloudPushService().bindTag(target, tagStrs, alias, new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        promise.resolve(response);
      }
      @Override
      public void onFailed(String code, String message) {
        promise.reject(code, message);
      }
    });
  }

  @ReactMethod
  public void unbindTag(int target, ReadableArray  tags, String alias, final Promise promise) {

    String[] tagStrs = new String[tags.size()];
    for(int i=0; i<tags.size();i++) tagStrs[i] = tags.getString(i);

    PushServiceFactory.getCloudPushService().unbindTag(target, tagStrs, alias, new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        promise.resolve(response);
      }
      @Override
      public void onFailed(String code, String message) {
        promise.reject(code, message);
      }
    });
  }

  @ReactMethod
  public void listTags(int target, final Promise promise) {
    PushServiceFactory.getCloudPushService().listTags(target, new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        promise.resolve(response);
      }
      @Override
      public void onFailed(String code, String message) {
        promise.reject(code, message);
      }
    });
  }

  @ReactMethod
  public void addAlias(String alias, final Promise promise) {
    PushServiceFactory.getCloudPushService().addAlias(alias, new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        promise.resolve(response);
      }
      @Override
      public void onFailed(String code, String message) {
        promise.reject(code, message);
      }
    });
  }

  @ReactMethod
  public void removeAlias(String alias, final Promise promise) {
    PushServiceFactory.getCloudPushService().removeAlias(alias, new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        promise.resolve(response);
      }
      @Override
      public void onFailed(String code, String message) {
        promise.reject(code, message);
      }
    });
  }

  @ReactMethod
  public void listAliases(final Promise promise) {
    PushServiceFactory.getCloudPushService().listAliases(new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        promise.resolve(response);
      }
      @Override
      public void onFailed(String code, String message) {
        promise.reject(code, message);
      }
    });
  }

  @Override
  public void onHostResume() {
  }

  @Override
  public void onHostPause() {

  }

  @Override
  public void onHostDestroy() {
  }

  public static boolean isRead = false;

  @ReactMethod
  public void getInitialMessage(final Promise promise){
    if(isRead) {
      promise.resolve(null);
      return;
    }
    promise.resolve(PushReceiver.initialMessage);
    isRead = true;
  }
}

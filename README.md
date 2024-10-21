
# react-native-mobilepush
###  本库是一个集成阿里云推送的react native module，方便为使用react native框架开发的App快速接入阿里云推送功能,降低android端和ios端分别接入阿里云推送SDK的相关配置

## Getting started

`$ npm install git+https://github.com/yebin-sesa524903/mobilepush.git`

### Mostly automatic installation

`$ react-native link react-native-mobilepush`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-mobilepush` and add `RNMobilepush.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMobilepush.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNMobilepushPackage;` to the imports at the top of the file
  - Add `new RNMobilepushPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-mobilepush'
  	project(':react-native-mobilepush').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mobilepush/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-mobilepush')
  	```


## 使用

#### android接入SDK
1.在android目录下的gradle.properties文件下引入如下的阿里云推送key,请根据实际申请的推送账户填写
```
pushKey=阿里云推送KEY
pushSecret=阿里云推送SECRET
xiaomiId=小米手机系统推送通道ID
xiaomiKey=小米手机系统推送通道KEY
huaweiAppId=华为手机系统推送通道ID
```
2. 在Project根目录下build.gradle文件中配置maven库URL:
```
allprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
        // 下面是添加的代码
        maven {
            url "http://maven.aliyun.com/nexus/content/repositories/releases/"
        }
        flatDir {
            dirs project(':react-native-aliyun-push').file('libs')
        }
        // 添加结束
    }
}
```
3.在Application的onCreate方法中初始化SDK
```java
@Override
  public void onCreate() {
        super.onCreate()
    ...
	//请根据app填写实际的NotificationChannel name 和 desc
	RNMobilepushPackage.initPushConfig(this,"NotificationChannel name","NotificationChannel desc");	
	...
  }
```
4.在项目的app module下build.gradle里配置相关推送信息，配置参考如下：
```gradle
	manifestPlaceholders=[
          pushKey:"$pushKey",
          pushSecret:"$pushSecret",
          xiaomiId:"$xiaomiId",
          xiaomiKey:"$xiaomiKey",
          huaweiAppId:"$huaweiAppId",
        ]
```

#### iOS接入SDK
1.请在项目的AppDelegate.m文件中引入头文件：
```objectc
#import "RNMobilepush.h"
```
2.在didFinishLaunchingWithOptions方法中调用如下初始化方法
```oc
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  ...

	[[RNMobilepush sharedInstance] setParams:@"阿里云推送key"
                                        appSecret:@"阿里云推送secret"
                                     lauchOptions:launchOptions
                createNotificationCategoryHandler:^{
                  //create customize notification category here
    }];
  
...
  return YES;
}
```
3.推送相关回调方法配置如下:
```oc
// APNs注册成功回调，将返回的deviceToken上传到CloudPush服务器
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  NSLog(@"APNS注册成功");
  [[RNMobilepush sharedInstance] application:application didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}


// APNs注册失败回调
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
  NSLog(@"APNS注册失败:%@",error);
  [[RNMobilepush sharedInstance] application:application didFailToRegisterForRemoteNotificationsWithError:error];
}

// 打开／删除通知回调
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult result))completionHandler
{
  [[RNMobilepush sharedInstance] application:application didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
}


// 请求注册设定后，回调
- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{
  [[RNMobilepush sharedInstance] application:application didRegisterUserNotificationSettings:notificationSettings];
}

```

#### React Native端使用
引入模块
```javascript
import RNMobilepush from 'react-native-mobilepush';
```
监听推送事件
```
componentDidMount() {
    this._push = DeviceEventEmitter.addListener('aliyunPushReceived',e => {
      console.log("Message Received. " + JSON.stringify(e));
    //e结构说明:
    //e.type: "notification":通知 或者 "message":消息
    //e.title: 推送通知/消息标题
    //e.body: 推送通知/消息具体内容
    //e.actionIdentifier: "opened":用户点击了通知, "removed"用户删除了通知, 其他非空值:用户点击了自定义action（仅限ios）
    //e.extras: 用户附加的{key:value}的对象
    })
}

componentWillUnmount() {
    //移除监听
    this._push.remove();
}
```

  <summary>阿里云SDK接口封装</summary>

详细参数说明请参考阿里云移动推送SDK [[android版]](https://help.aliyun.com/document_detail/30066.html?spm=5176.doc30064.6.643.Mu5vP0)    [[ios版]](https://help.aliyun.com/document_detail/42668.html?spm=5176.doc30066.6.649.VmzJfM)

**获取deviceId**

示例:
```
RNMobilepush.getDeviceId()
    .then((deviceId)=>{
        //console.log("deviceId:"+deviceId);
    })
    .catch((error)=>{
        console.log("getDeviceId() failed");
    });
```
**绑定账号**

参数：
- account 待绑定账号

示例:
```
RNMobilepush.bindAccount(account)
    .then((data)=>{
        console.log("bindAccount success");
        console.log(JSON.stringify(data));
    })
    .catch((error)=>{
        console.log("bindAccount error");
        console.log(JSON.stringify(error));
    });
```
**解绑定账号**

示例:
```
RNMobilepush.unbindAccount()
    .then((result)=>{
        console.log("unbindAccount success");
        console.log(JSON.stringify(result));
    })
    .catch((error)=>{
        console.log("bindAccount error");
        console.log(JSON.stringify(error));
    });
```
**绑定标签**

参数：
- target 目标类型，1：本设备；2：本设备绑定账号；3：别名
- tags 标签（数组输入）
- alias 别名（仅当target = 3时生效）

示例:
```
RNMobilepush.bindTag(1,["testtag1","testtag2"],"")
    .then((result)=>{
        console.log("bindTag success");
        console.log(JSON.stringify(result));
    })
    .catch((error)=>{
        console.log("bindTag error");
        console.log(JSON.stringify(error));
    });
```
**解绑定标签**

参数:
- target 目标类型，1：本设备；2：本设备绑定账号；3：别名
- tags 标签（数组输入）
- alias 别名（仅当target = 3时生效）

示例:
```
RNMobilepush.unbindTag(1,["testTag1"],"")
    .then((result)=>{
        console.log("unbindTag succcess");
        console.log(JSON.stringify(result));
    })
    .catch((error)=>{
        console.log("unbindTag error");
        console.log(JSON.stringify(error));
    });
```
**查询当前Tag列表**

参数:
- target 目标类型，1：本设备

示例:
```
RNMobilepush.listTags(1)
    .then((result)=>{
        console.log("listTags success");
        console.log(JSON.stringify(result));
    })
    .catch((error)=>{
        console.log("listTags error");
        console.log(JSON.stringify(error));
    });
```
**添加别名**

参数:
- alias 要添加的别名

示例:
```
RNMobilepush.addAlias("testAlias")
    .then((result)=>{
        console.log("addAlias success");
        console.log(JSON.stringify(result));
    })
    .catch((error)=>{
        console.log("addAlias error");
        console.log(JSON.stringify(error));
    });
```
**删除别名**

参数:
- alias 要移除的别名

示例:
```
RNMobilepush.removeAlias("testAlias")
    .then((result)=>{
        console.log("removeAlias success");
        console.log(JSON.stringify(result));
    })
    .catch((error)=>{
        console.log("removeAlias error");
        console.log(JSON.stringify(error));
    });
```
**查询别名列表**

示例:
```
RNMobilepush.listAliases()
    .then((result)=>{
        console.log("listAliases success");
        console.log(JSON.stringify(result));
    })
    .catch((error)=>{
        console.log("listAliases error");
        console.log(JSON.stringify(error));
    });
```

**获取初始消息**

app在未启动时收到通知后，点击通知启动app,
如果在向JS发消息时，JS没准备好或者没注册listener，则先临时保存该消息，
并提供getInitalMessage方法可以获取，在app的JS逻辑完成后可以继续处理该消息

##### android端需要在Activity里做相应的配置，才能支持app未启动时来的推送在启动时能接收到，例子代码如下：
```java
package com.start.A;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.sdk.android.push.popup.PopupNotifyClick;
import com.alibaba.sdk.android.push.popup.PopupNotifyClickListener;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactlibrary.PushReceiver;

import java.util.Map;

public class MainActivity extends ReactActivity implements PopupNotifyClickListener {

  static final String TAG = "MainActivity";

  PopupNotifyClick popupNotifyClick = new PopupNotifyClick(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    popupNotifyClick.onCreate(this,getIntent());
  }

  @Override
  public void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Log.e(TAG,"onNewIntent");
    Log.e("onNewIntent",intent.toString());
    popupNotifyClick.onNewIntent(intent);
  }

  @Override
  public void onSysNoticeOpened(String title, String content, Map<String, String> extraMap) {
    Log.e(TAG,"onSysNoticeOpened");
    WritableMap params = Arguments.createMap();
    params.putString("body", content);
    params.putString("title", title);
    WritableMap extraWritableMap = Arguments.createMap();
    for (Map.Entry<String, String> entry : extraMap.entrySet()) {
      extraWritableMap.putString(entry.getKey(),entry.getValue());
    }
    params.putMap("extras", extraWritableMap);
    params.putString("type", "notification");
    Log.e("onSysNoticeOpened",params.toString());
    PushReceiver.sendEvent("aliyunPushReceived", params);
  }

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "Start";
  }


}

```

示例:
```
async componentDidMount() {
    const msg = await RNMobilepush.getInitialMessage();
    if(msg){
        this.handleRNMobilepushMessage(msg);
    }
}
```

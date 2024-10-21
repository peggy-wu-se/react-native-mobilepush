#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <UIKit/UIKit.h>

@interface RNMobilepush : RCTEventEmitter<RCTBridgeModule>

+ (RNMobilepush *)sharedInstance;


- (void)setParams:(NSString *)appKey appSecret:(NSString *)appSecret lauchOptions:(NSDictionary *)lauchOptions createNotificationCategoryHandler:(void (^)(void))categoryHandler;

// notification settings
- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings;

// APNs register success and return deviceToken
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;
// APNs register failed
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error;


// Notification Open
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult result))completionHandler;

- (void)sendEventToJs:(NSMutableDictionary*)notification;

@end


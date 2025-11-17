
#import "AMARNReporter.h"
#import "AMARNAppMetricaUtils.h"
#import <AppMetricaCore/AppMetricaCore.h>
#import <AppMetricaCrashes/AppMetricaCrashes.h>
#import "AMARNUserProfileSerializer.h"
#import "AMARNExceptionSerializer.h"

@implementation AMARNReporter

RCT_EXPORT_MODULE(AppMetricaReporter)

- (void)reportError:(nonnull NSString *)apiKey identifier:(nonnull NSString *)identifier message:(nonnull NSString *)message error:(nonnull NSDictionary *)error
{
    id<AMAAppMetricaCrashReporting> reporter = [[AMAAppMetricaCrashes crashes] reporterForAPIKey:apiKey];
    [[reporter pluginExtension] reportErrorWithIdentifier:identifier
                                                  message:message
                                                  details:amarn_exceptionForDictionary(error)
                                                onFailure:^(NSError *error) {
        NSLog(@"Failed to report error to AppMetrica: %@", [error localizedDescription]);
    }];
}

- (void)reportErrorWithoutIdentifier:(nonnull NSString *)apiKey message:(NSString * _Nullable)message error:(nonnull NSDictionary *)error
{
    id<AMAAppMetricaCrashReporting> reporter = [[AMAAppMetricaCrashes crashes] reporterForAPIKey:apiKey];
    AMAPluginErrorDetails *details = amarn_exceptionForDictionary(error);
    if (details.backtrace.count == 0) {
        [[reporter pluginExtension] reportErrorWithIdentifier:@"Errors without stacktrace"
                                                      message:message
                                                      details:details
                                                    onFailure:^(NSError *error) {
            NSLog(@"Failed to report error to AppMetrica: %@", [error localizedDescription]);
        }];
    } else {
        [[reporter pluginExtension] reportError:details message:message onFailure:^(NSError *error) {
            NSLog(@"Failed to report error to AppMetrica: %@", [error localizedDescription]);
        }];
    }
}

- (void)reportUnhandledException:(nonnull NSString *)apiKey error:(nonnull NSDictionary *)error
{
    id<AMAAppMetricaCrashReporting> reporter = [[AMAAppMetricaCrashes crashes] reporterForAPIKey:apiKey];
    [[reporter pluginExtension] reportUnhandledException:amarn_exceptionForDictionary(error)
                                               onFailure:^(NSError *error) {
        NSLog(@"Failed to report unhandled exception to AppMetrica: %@", [error localizedDescription]);
    }];
}

- (void)reportEvent:(nonnull NSString *)apiKey eventName:(nonnull NSString *)eventName attributes:(nonnull NSDictionary *)attributes
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    if (attributes == nil) {
        [reporter reportEvent:eventName onFailure:^(NSError *error) {
            NSLog(@"error: %@", [error localizedDescription]);
        }];
    } else {
        [reporter reportEvent:eventName parameters:attributes onFailure:^(NSError *error) {
            NSLog(@"error: %@", [error localizedDescription]);
        }];
    }
}

- (void)pauseSession:(NSString *)apiKey
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter pauseSession];
}

- (void)resumeSession:(NSString *)apiKey
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter resumeSession];
}

- (void)sendEventsBuffer:(NSString *)apiKey
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter sendEventsBuffer];
}

- (void)putAppEnvironmentValue:(nonnull NSString *)apiKey key:(nonnull NSString *)key value:(nonnull NSString *)value
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter setAppEnvironmentValue:value forKey:key];
}

- (void)clearAppEnvironment:(NSString *)apiKey
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter clearAppEnvironment];
}

- (void)setUserProfileID:(nonnull NSString *)apiKey userProfileID:(nonnull NSString *)userProfileID
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter setUserProfileID:userProfileID];
}

- (void)setDataSendingEnabled:(nonnull NSString *)apiKey enabled:(BOOL)enabled
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter setDataSendingEnabled:enabled];
}

- (void)reportUserProfile:(nonnull NSString *)apiKey userProfile:(nonnull NSDictionary *)userProfile
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter reportUserProfile:[AMARNAppMetricaUtils userProfileForDict:userProfile] onFailure:nil];
}

- (void)reportRevenue:(nonnull NSString *)apiKey revenue:(nonnull NSDictionary *)revenue
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter reportRevenue:[AMARNAppMetricaUtils revenueForDict:revenue] onFailure:nil];
}

- (void)reportAdRevenue:(nonnull NSString *)apiKey adRevenue:(nonnull NSDictionary *)adRevenue
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter reportAdRevenue:[AMARNAppMetricaUtils adRevenueForDict:adRevenue] onFailure:nil];
}

- (void)reportECommerce:(nonnull NSString *)apiKey event:(nonnull NSDictionary *)event
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter reportECommerce:[AMARNAppMetricaUtils ecommerceForDict:event] onFailure:nil];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeReporterSpecJSI>(params);
}

@end

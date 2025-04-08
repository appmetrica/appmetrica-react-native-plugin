
#import "AMARNReporter.h"
#import "AMARNAppMetricaUtils.h"
#import <AppMetricaCore/AppMetricaCore.h>
#import <AppMetricaCrashes/AppMetricaCrashes.h>
#import "AMARNUserProfileSerializer.h"
#import "AMARNExceptionSerializer.h"

@implementation AMARNReporter

RCT_EXPORT_MODULE(AppMetricaReporter)

RCT_EXPORT_METHOD(reportError:(NSString *)apiKey:(NSString *)identifier:(NSString *)message:(NSDictionary *)_reason)
{
    id<AMAAppMetricaCrashReporting> reporter = [[AMAAppMetricaCrashes crashes] reporterForAPIKey:apiKey];
    [[reporter pluginExtension] reportErrorWithIdentifier:identifier
                                                  message:message
                                                  details:amarn_exceptionForDictionary(_reason)
                                                onFailure:^(NSError *error) {
        NSLog(@"Failed to report error to AppMetrica: %@", [error localizedDescription]);
    }];
}

RCT_EXPORT_METHOD(reportErrorWithoutIdentifier:(NSString *)apiKey:(NSString *)message:(NSDictionary *)error)
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

RCT_EXPORT_METHOD(reportUnhandledException:(NSString *)apiKey:(NSDictionary *)error)
{
    id<AMAAppMetricaCrashReporting> reporter = [[AMAAppMetricaCrashes crashes] reporterForAPIKey:apiKey];
    [[reporter pluginExtension] reportUnhandledException:amarn_exceptionForDictionary(error)
                                               onFailure:^(NSError *error) {
        NSLog(@"Failed to report unhandled exception to AppMetrica: %@", [error localizedDescription]);
    }];
}

RCT_EXPORT_METHOD(reportEvent:(NSString *)apiKey:(NSString *)eventName:(NSDictionary *)attributes)
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

RCT_EXPORT_METHOD(pauseSession:(NSString *)apiKey)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter pauseSession];
}

RCT_EXPORT_METHOD(resumeSession:(NSString *)apiKey)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter resumeSession];
}

RCT_EXPORT_METHOD(sendEventsBuffer:(NSString *)apiKey)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter sendEventsBuffer];
}

RCT_EXPORT_METHOD(putAppEnvironmentValue:(NSString *)apiKey:(NSString *)key:(NSString *)value)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter setAppEnvironmentValue:value forKey:key];
}

RCT_EXPORT_METHOD(clearAppEnvironment:(NSString *)apiKey)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter clearAppEnvironment];
}

RCT_EXPORT_METHOD(setUserProfileID:(NSString *)apiKey:(NSString *)userProfileID)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter setUserProfileID:userProfileID];
}

RCT_EXPORT_METHOD(setDataSendingEnabled:(NSString *)apiKey:(BOOL)enabled)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter setDataSendingEnabled:enabled];
}

RCT_EXPORT_METHOD(reportUserProfile:(NSString *)apiKey:(NSDictionary *)userProfileDict)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter reportUserProfile:[AMARNAppMetricaUtils userProfileForDict:userProfileDict] onFailure:nil];
}

RCT_EXPORT_METHOD(reportRevenue:(NSString *)apiKey:(NSDictionary *)revenueDict)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter reportRevenue:[AMARNAppMetricaUtils revenueForDict:revenueDict] onFailure:nil];
}

RCT_EXPORT_METHOD(reportAdRevenue:(NSString *)apiKey:(NSDictionary *)revenueDict)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter reportAdRevenue:[AMARNAppMetricaUtils adRevenueForDict:revenueDict] onFailure:nil];
}

RCT_EXPORT_METHOD(reportECommerce:(NSString *)apiKey:(NSDictionary *)ecommerceDict)
{
    id<AMAAppMetricaReporting> reporter = [AMAAppMetrica reporterForAPIKey:apiKey];
    [reporter reportECommerce:[AMARNAppMetricaUtils ecommerceForDict:ecommerceDict] onFailure:nil];
}

@end

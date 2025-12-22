
#import "AMARNAppMetrica.h"
#import "AMARNAppMetricaUtils.h"
#import "AMARNStartupParamsUtils.h"
#import "AMARNUserProfileSerializer.h"
#import "AMARNExternalAttribution.h"
#import <AppMetricaCrashes/AppMetricaCrashes.h>
#import "AMARNExceptionSerializer.h"

@implementation AMARNAppMetrica

RCT_EXPORT_MODULE(AppMetrica)

- (void)activate:(NSDictionary *)configDict
{
    [[AMAAppMetricaCrashes crashes] setConfiguration:[AMARNAppMetricaUtils crashConfigurationForDictionary:configDict]];
    [AMAAppMetrica activateWithConfiguration:[AMARNAppMetricaUtils configurationForDictionary:configDict]];
}

- (NSNumber *)getLibraryApiLevel
{
    // It does nothing for iOS
    return 0;
}

- (NSString *)getLibraryVersion
{
    return [AMAAppMetrica libraryVersion];
}

- (void)pauseSession
{
    [AMAAppMetrica pauseSession];
}

- (void)reportAppOpen:(NSString *)deeplink
{
    [AMAAppMetrica trackOpeningURL:[NSURL URLWithString:deeplink]];
}

- (void)reportError:(NSString *)identifier
            message:(NSString *)message
              error:(NSDictionary *)error {
    [[[AMAAppMetricaCrashes crashes] pluginExtension] reportErrorWithIdentifier:identifier
                                                                        message:message
                                                                        details:amarn_exceptionForDictionary(error)
                                                                      onFailure:^(NSError *error) {
        NSLog(@"Failed to report error to AppMetrica: %@", [error localizedDescription]);
    }];
}

- (void)reportEvent:(NSString *)eventName
         attributes:(NSDictionary *)attributes
{
    if (attributes == nil) {
        [AMAAppMetrica reportEvent:eventName onFailure:^(NSError *error) {
            NSLog(@"error: %@", [error localizedDescription]);
        }];
    } else {
        [AMAAppMetrica reportEvent:eventName parameters:attributes onFailure:^(NSError *error) {
            NSLog(@"error: %@", [error localizedDescription]);
        }];
    }
}

- (void)requestStartupParams:(RCTResponseSenderBlock)listener
                 identifiers:(NSArray *)identifiers
{
    AMAIdentifiersCompletionBlock block = ^(NSDictionary<AMAStartupKey,id> * _Nullable identifiers, NSError * _Nullable error) {
        NSDictionary *result = [AMARNStartupParamsUtils toStrartupParamsResult:identifiers];
        NSString *errorStr = [AMARNStartupParamsUtils stringFromRequestStartupParamsError:error];
        listener(@[[self wrap:result], [self wrap:errorStr]]);
    };
    [AMAAppMetrica requestStartupIdentifiersWithKeys:identifiers completionQueue:nil completionBlock:block];
}

- (void)resumeSession
{
    [AMAAppMetrica resumeSession];
}

- (void)sendEventsBuffer
{
    [AMAAppMetrica sendEventsBuffer];
}

- (void)setLocation:(NSDictionary *)locationDict
{
    AMAAppMetrica.customLocation = [AMARNAppMetricaUtils locationForDictionary:locationDict];
}

- (void)setLocationTracking:(BOOL)enabled
{
    AMAAppMetrica.locationTrackingEnabled = enabled;
}

- (void)setDataSendingEnabled:(BOOL)enabled
{
    [AMAAppMetrica setDataSendingEnabled:enabled];
}

- (void)reportECommerce:(NSDictionary *)ecommerceDict
{
    [AMAAppMetrica reportECommerce:[AMARNAppMetricaUtils ecommerceForDict:ecommerceDict] onFailure:nil];
}

- (void)setUserProfileID:(NSString *)userProfileID
{
    [AMAAppMetrica setUserProfileID:userProfileID];
}

- (void)reportRevenue:(NSDictionary *)revenueDict
{
    [AMAAppMetrica reportRevenue:[AMARNAppMetricaUtils revenueForDict:revenueDict] onFailure:nil];
}

- (void)reportAdRevenue:(NSDictionary *)revenueDict
{
    [AMAAppMetrica reportAdRevenue:[AMARNAppMetricaUtils adRevenueForDict:revenueDict] onFailure:nil];
}

- (void)reportUserProfile:(NSDictionary *)userProfileDict
{
    [AMAAppMetrica reportUserProfile:[AMARNAppMetricaUtils userProfileForDict:userProfileDict] onFailure:nil];
}

- (void)putErrorEnvironmentValue:(NSString *)key
                           value:(NSString *)value
{
    [[AMAAppMetricaCrashes crashes] setErrorEnvironmentValue:value forKey:key];
}

- (void)reportExternalAttribution:(NSDictionary *)externalAttributionsDict
{

    NSString *sourceStr = externalAttributionsDict[@"source"];
    AMAAttributionSource source = amarn_getExternalAttributionSource(sourceStr);
    if (source == nil) {
        NSLog(@"Failed to report external attribution to AppMetrica. Unknown source %@", sourceStr);
        return;
    }

    NSDictionary *value = externalAttributionsDict[@"value"];

    [AMAAppMetrica reportExternalAttribution:value source:source onFailure:^(NSError *error) {
        NSLog(@"Failed to report external attribution to AppMetrica: %@", [error localizedDescription]);
    }];
}

- (void)reportErrorWithoutIdentifier:(NSString * _Nullable)message
                               error:(NSDictionary *)error
{
    AMAPluginErrorDetails *details = amarn_exceptionForDictionary(error);
    if (details.backtrace.count == 0) {
        [[[AMAAppMetricaCrashes crashes] pluginExtension] reportErrorWithIdentifier:@"Errors without stacktrace"
                                                                            message:message
                                                                            details:details
                                                                          onFailure:^(NSError *error) {
            NSLog(@"Failed to report error to AppMetrica: %@", [error localizedDescription]);
        }];
    } else {
        [[[AMAAppMetricaCrashes crashes] pluginExtension] reportError:details message:message onFailure:^(NSError *error) {
            NSLog(@"Failed to report error to AppMetrica: %@", [error localizedDescription]);
        }];
    }
}

- (void)reportUnhandledException:(NSDictionary *)error
{
    [[[AMAAppMetricaCrashes crashes] pluginExtension] reportUnhandledException:amarn_exceptionForDictionary(error)
                                                                     onFailure:^(NSError *error) {
        NSLog(@"Failed to report unhandled exception to AppMetrica: %@", [error localizedDescription]);
    }];
}

- (void)putAppEnvironmentValue:(NSString *)key
                         value:(NSString *)value
{
    [AMAAppMetrica setAppEnvironmentValue:value forKey:key];
}

- (void)clearAppEnvironment
{
    [AMAAppMetrica clearAppEnvironment];
}

- (void)activateReporter:(NSDictionary *)configDict
{
    [AMAAppMetrica activateReporterWithConfiguration:[AMARNAppMetricaUtils reporterConfigurationForDictionary:configDict]];
}

- (void)touchReporter:(NSString *)apiKey
{
    [AMAAppMetrica reporterForAPIKey:apiKey];
}

- (NSString *)getDeviceId
{
    return [AMAAppMetrica deviceID];
}

- (NSString *)getUuid
{
    return [AMAAppMetrica UUID];
}

- (void)requestDeferredDeeplink:(RCTResponseSenderBlock)onFailure
                      onSuccess:(RCTResponseSenderBlock)onSuccess
{
  // It does nothing for iOS
}

- (void)requestDeferredDeeplinkParameters:(RCTResponseSenderBlock)onFailure
                                onSuccess:(RCTResponseSenderBlock)onSuccess
{
  // It does nothing for iOS
}

- (id)constantsToExport {
    return @{
        @"DEVICE_ID_HASH_KEY": kAMADeviceIDHashKey,
        @"DEVICE_ID_KEY": kAMADeviceIDKey,
        @"UUID_KEY": kAMAUUIDKey
    };
}


- (id)getConstants {
    return [self constantsToExport];
}


- (NSObject *)wrap:(NSObject *)value
{
    if (value == nil) {
        return [NSNull null];
    }
    return value;
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeAppMetricaSpecJSI>(params);
}

@end

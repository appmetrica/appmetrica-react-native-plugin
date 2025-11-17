
#import "AMARNStartupParamsUtils.h"
#import <AppMetricaCore/AppMetricaCore.h>

@implementation AMARNStartupParamsUtils

+ (NSDictionary *)toStrartupParamsResult:(NSDictionary *)resultDict
{
    if (resultDict == nil) {
        return nil;
    }
    NSMutableDictionary *result = [[NSMutableDictionary alloc] init];
    if (resultDict[kAMADeviceIDHashKey] != nil) {
        [result setValue:resultDict[kAMADeviceIDHashKey] forKey:@"deviceIdHash"];
    }
    if (resultDict[kAMADeviceIDKey] != nil) {
        [result setValue:resultDict[kAMADeviceIDKey] forKey:@"deviceId"];
    }
    if (resultDict[kAMAUUIDKey] != nil) {
        [result setValue:resultDict[kAMAUUIDKey] forKey:@"uuid"];
    }
    return result;
}

+ (NSString *)stringFromRequestStartupParamsError:(NSError *)error
{
    if (error == nil) {
        return nil;
    }
    if ([error.domain isEqualToString:NSURLErrorDomain]) {
        return @"NETWORK";
    }
    return @"UNKNOWN";
}

@end

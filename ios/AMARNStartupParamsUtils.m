
#import "AMARNStartupParamsUtils.h"
#import <AppMetricaCore/AppMetricaCore.h>

@implementation AMARNStartupParamsUtils

+ (NSArray *)toStartupKeys:(NSArray *)keys
{
    NSMutableArray *keysArray = [[NSMutableArray alloc] init];
    for (NSString *item in keys) {
        if ([item isEqualToString:@"appmetrica_device_id_hash"]) {
            [keysArray addObject:kAMADeviceIDHashKey];
        }
        if ([item isEqualToString:@"appmetrica_device_id"]) {
            [keysArray addObject:kAMADeviceIDKey];
        }
        if ([item isEqualToString:@"appmetrica_uuid"]) {
            [keysArray addObject:kAMAUUIDKey];
        }
    }
    return keysArray;
}

+ (NSDictionary *)toStrartupParamsResult:(NSDictionary *)resultDict
{
    if (resultDict == nil) {
        return nil;
    }
    NSMutableDictionary *result = [[NSMutableDictionary alloc] init];
    if (resultDict[@"appmetrica_deviceIDHash"] != nil) {
        [result setValue:resultDict[kAMADeviceIDHashKey] forKey:@"deviceIdHash"];
    }
    if (resultDict[@"appmetrica_deviceID"] != nil) {
        [result setValue:resultDict[kAMADeviceIDKey] forKey:@"deviceId"];
    }
    if (resultDict[@"appmetrica_uuid"] != nil) {
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

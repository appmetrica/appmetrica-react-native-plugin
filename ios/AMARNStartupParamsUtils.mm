
#import "AMARNStartupParamsUtils.h"
#import <AppMetricaCore/AppMetricaCore.h>

@implementation AMARNStartupParamsUtils

+ (NSDictionary *)toStrartupParamsResult:(NSDictionary *)resultDict
{
    if (resultDict == nil) {
        return nil;
    }
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    for (NSString *key in resultDict) {
        parameters[key] = @{
            @"id": resultDict[key],
            @"status": @"OK",
        };
    }
    return parameters;
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

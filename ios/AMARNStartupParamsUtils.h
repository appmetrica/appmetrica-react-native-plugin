
@interface AMARNStartupParamsUtils : NSObject

+ (NSArray *)toStartupKeys:(NSArray *)keys;
+ (NSDictionary *)toStrartupParamsResult:(NSDictionary *)resultDict;
+ (NSString *)stringFromRequestStartupParamsError:(NSError *)error;

@end


#import "AMARNExceptionSerializer.h"

NSNumber *amarn_parseNumber(NSString *string)
{
    NSNumberFormatter *format = [[NSNumberFormatter alloc] init];
    format.numberStyle = NSNumberFormatterDecimalStyle;
    NSNumber *myNumber = [format numberFromString:string];
    return myNumber;
}

AMAStackTraceElement *amarn_deserializeTraceItem(NSDictionary *traceDict)
{
    AMAStackTraceElement *item = [[AMAStackTraceElement alloc] init];
    if (traceDict[@"fileName"] != nil) {
        item.fileName = traceDict[@"fileName"];
    }
    if (traceDict[@"className"] != nil) {
        item.className = traceDict[@"className"];
    }
    if (traceDict[@"methodName"] != nil) {
        item.methodName = traceDict[@"methodName"];
    }
    if (traceDict[@"line"] != nil) {
        item.line = amarn_parseNumber(traceDict[@"line"]);
    }
    if (traceDict[@"column"] != nil) {
        item.column = amarn_parseNumber(traceDict[@"column"]);
    }
    return item;
}

NSArray<AMAStackTraceElement *> *amarn_deserializeStackTrace(NSArray *frames)
{
    NSMutableArray<AMAStackTraceElement *> *items = [[NSMutableArray alloc] init];
    for (NSDictionary *frameDict in frames) {
        [items addObject:amarn_deserializeTraceItem(frameDict)];
    }
    return items;
}

AMAPluginErrorDetails *amarn_exceptionForDictionary(NSDictionary *configDict)
{
    if (configDict == nil) {
        return nil;
    }
    AMAPluginErrorDetails *error = [[AMAPluginErrorDetails alloc] init];
    error.platform = kAMAPlatformReactNative;
    if (configDict[@"errorName"] != nil) {
        error.exceptionClass = configDict[@"errorName"];
    }
    if (configDict[@"message"] != nil) {
        error.message = configDict[@"message"];
    }
    if (configDict[@"stackTrace"] != nil) {
        error.backtrace = amarn_deserializeStackTrace(configDict[@"stackTrace"]);
    }
    if (configDict[@"virtualMachineVersion"] != nil) {
        error.virtualMachineVersion = configDict[@"virtualMachineVersion"];
    }
    if (configDict[@"pluginEnvironment"] != nil) {
        error.pluginEnvironment = configDict[@"pluginEnvironment"];
    }
    return error;
}

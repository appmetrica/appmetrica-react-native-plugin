
#import "AMARNAppMetricaUtils.h"

@implementation AMARNAppMetricaUtils

+ (AMAAppMetricaConfiguration *)configurationForDictionary:(NSDictionary *)configDict
{
    NSString *apiKey = configDict[@"apiKey"];
    AMAAppMetricaConfiguration *configuration = [[AMAAppMetricaConfiguration alloc] initWithAPIKey:apiKey];
    if (configDict[@"appVersion"] != nil) {
        configuration.appVersion = configDict[@"appVersion"];
    }
    if (configDict[@"activationAsSessionStart"] != nil) {
        configuration.handleActivationAsSessionStart = [configDict[@"activationAsSessionStart"] boolValue];
    }
    if (configDict[@"firstActivationAsUpdate"] != nil) {
        configuration.handleFirstActivationAsUpdate = [configDict[@"firstActivationAsUpdate"] boolValue];
    }
    if (configDict[@"location"] != nil) {
        configuration.customLocation = [self locationForDictionary:configDict[@"location"]];
    }
    if (configDict[@"locationTracking"] != nil) {
        configuration.locationTracking = [configDict[@"locationTracking"] boolValue];
    }
    if (configDict[@"logs"] != nil) {
        configuration.logsEnabled = [configDict[@"logs"] boolValue];
    }
    if (configDict[@"preloadInfo"] != nil) {
        configuration.preloadInfo = [[self class] preloadInfoForDictionary:configDict[@"preloadInfo"]];
    }
    if (configDict[@"sessionsAutoTracking"] != nil) {
        configuration.sessionsAutoTracking = [configDict[@"sessionsAutoTracking"] boolValue];
    }
    if (configDict[@"sessionTimeout"] != nil) {
        configuration.sessionTimeout = [configDict[@"sessionTimeout"] unsignedIntegerValue];
    }
    if (configDict[@"statisticsSending"] != nil) {
        configuration.dataSendingEnabled = [configDict[@"statisticsSending"] boolValue];
    }
    if (configDict[@"maxReportsInDatabaseCount"] != nil) {
        configuration.maxReportsInDatabaseCount = [configDict[@"maxReportsInDatabaseCount"] unsignedIntegerValue];
    }
    if (configDict[@"userProfileID"] != nil) {
        configuration.userProfileID = configDict[@"userProfileID"];
    }
    if (configDict[@"appEnvironment"] != nil) {
        NSDictionary *appEnvironmentMap = configDict[@"appEnvironment"];
        for (NSString *key in appEnvironmentMap) {
            [AMAAppMetrica setAppEnvironmentValue:appEnvironmentMap[key] forKey:key];
        }
    }
    if (configDict[@"maxReportsCount"] != nil) {
        configuration.maxReportsCount = [configDict[@"maxReportsCount"] unsignedIntegerValue];
    }
    if (configDict[@"dispatchPeriodSeconds"] != nil) {
        configuration.dispatchPeriod = [configDict[@"dispatchPeriodSeconds"] unsignedIntegerValue];
    }

    return configuration;
}

+ (AMAAppMetricaCrashesConfiguration *)crashConfigurationForDictionary:(NSDictionary *)crashConfigDict
{
    AMAAppMetricaCrashesConfiguration *crashesConfiguration = [[AMAAppMetricaCrashesConfiguration alloc] init];
    if (crashConfigDict[@"crashReporting"] != nil) {
        crashesConfiguration.autoCrashTracking = [crashConfigDict[@"crashReporting"] boolValue];
    }
    if (crashConfigDict[@"errorEnvironment"] != nil) {
        NSDictionary *errorEnvironmentDict = crashConfigDict[@"errorEnvironment"];
        for (NSString *key in errorEnvironmentDict) {
            [[AMAAppMetricaCrashes crashes] setErrorEnvironmentValue:errorEnvironmentDict[key] forKey:key];
        }
    }
    return crashesConfiguration;
}

+ (CLLocation *)locationForDictionary:(NSDictionary *)locationDict
{
    if (locationDict == nil) {
        return nil;
    }

    NSNumber *latitude = locationDict[@"latitude"];
    NSNumber *longitude = locationDict[@"longitude"];
    NSNumber *altitude = locationDict[@"altitude"];
    NSNumber *horizontalAccuracy = locationDict[@"accuracy"];
    NSNumber *verticalAccuracy = locationDict[@"verticalAccuracy"];
    NSNumber *course = locationDict[@"course"];
    NSNumber *speed = locationDict[@"speed"];
    NSNumber *timestamp = locationDict[@"timestamp"];

    NSDate *locationDate = timestamp != nil ? [NSDate dateWithTimeIntervalSince1970:timestamp.doubleValue] : [NSDate date];
    CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake(latitude.doubleValue, longitude.doubleValue);
    CLLocation *location = [[CLLocation alloc] initWithCoordinate:coordinate
                                                         altitude:altitude.doubleValue
                                               horizontalAccuracy:horizontalAccuracy.doubleValue
                                                 verticalAccuracy:verticalAccuracy.doubleValue
                                                           course:course.doubleValue
                                                            speed:speed.doubleValue
                                                        timestamp:locationDate];

    return location;
}

+ (AMAAppMetricaPreloadInfo *)preloadInfoForDictionary:(NSDictionary *)preloadInfoDict
{
    if (preloadInfoDict == nil) {
        return nil;
    }

    NSString *trackingId = preloadInfoDict[@"trackingId"];
    AMAAppMetricaPreloadInfo *preloadInfo = [[AMAAppMetricaPreloadInfo alloc] initWithTrackingIdentifier:trackingId];

    NSDictionary *additionalInfo = preloadInfoDict[@"additionalInfo"];
    if (additionalInfo != nil) {
        for (NSString *key in additionalInfo) {
            [preloadInfo setAdditionalInfo:additionalInfo[key] forKey:key];
        }
    }

    return preloadInfo;
}

+ (AMAECommerceScreen *)ecommerceScreenForDict:(NSDictionary *)ecommerceScreenDict
{
    if (ecommerceScreenDict == nil) {
        return nil;
    }
    NSString *name = ecommerceScreenDict[@"name"];
    NSString *searchQuery = ecommerceScreenDict[@"searchQuery"];
    NSArray *categoriesPath = ecommerceScreenDict[@"categoriesPath"];
    NSDictionary *payload = ecommerceScreenDict[@"payload"];
    AMAECommerceScreen *screen = [[AMAECommerceScreen alloc] initWithName:name
                                                       categoryComponents:categoriesPath
                                                              searchQuery:searchQuery
                                                                  payload:payload];

    return screen;
}

+ (AMAECommercePrice *)ecommercePriceForDict:(NSDictionary *)ecommercePriceDict
{
    if (ecommercePriceDict == nil) {
        return nil;
    }
    AMAECommerceAmount *amount = [self ecommerceAmountForDict:ecommercePriceDict[@"amount"]];
    NSArray *componentsArray = ecommercePriceDict[@"internalComponents"];

    NSMutableArray *components = [[NSMutableArray alloc] init];

    for (NSDictionary *item in componentsArray) {
        [components addObject:[self ecommerceAmountForDict:item]];
    }

    AMAECommercePrice *price = [[AMAECommercePrice alloc] initWithFiat:amount
                                                    internalComponents:components];

    return price;
}

+ (AMAECommerceReferrer *)ecommerceReferrerForDict:(NSDictionary *)ecommerceReferrerDict
{
    if (ecommerceReferrerDict == nil) {
        return nil;
    }
    NSString *type = ecommerceReferrerDict[@"type"];
    NSString *identifier = ecommerceReferrerDict[@"identifier"];
    AMAECommerceScreen *screen = [self ecommerceScreenForDict:ecommerceReferrerDict[@"screen"]];

    AMAECommerceReferrer *referrer = [[AMAECommerceReferrer alloc] initWithType:type
                                                                     identifier:identifier
                                                                         screen:screen];
    return referrer;
}

+ (AMAECommerceProduct *)ecommerceProductForDict:(NSDictionary *)ecommerceProductDict
{
    if (ecommerceProductDict == nil) {
        return nil;
    }
    NSString *name = ecommerceProductDict[@"name"];
    NSString *sku = ecommerceProductDict[@"sku"];
    NSArray *categoriesPath = ecommerceProductDict[@"categoriesPath"];
    NSArray *promocodes = ecommerceProductDict[@"promocodes"];
    NSDictionary *payload = ecommerceProductDict[@"payload"];
    AMAECommercePrice *actualPrice = [self ecommercePriceForDict:ecommerceProductDict[@"actualPrice"]];
    AMAECommercePrice *originalPrice = [self ecommercePriceForDict:ecommerceProductDict[@"originalPrice"]];

    AMAECommerceProduct *product = [[AMAECommerceProduct alloc] initWithSKU:sku
                                                                       name:name
                                                         categoryComponents:categoriesPath
                                                                    payload:payload
                                                                actualPrice:actualPrice
                                                              originalPrice:originalPrice
                                                                 promoCodes:promocodes];
    return product;
}

+ (AMAECommerceCartItem *)ecommerceCartItemForDict:(NSDictionary *)ecommerceCartItemDict
{
    if (ecommerceCartItemDict == nil) {
        return nil;
    }
    AMAECommerceProduct *product = [self ecommerceProductForDict:ecommerceCartItemDict[@"product"]];
    AMAECommercePrice *price = [self ecommercePriceForDict:ecommerceCartItemDict[@"price"]];
    NSDecimalNumber *quantity = [NSDecimalNumber decimalNumberWithDecimal:[ecommerceCartItemDict[@"quantity"] decimalValue]];
    AMAECommerceReferrer *referrer = [self ecommerceReferrerForDict:ecommerceCartItemDict[@"referrer"]];

    AMAECommerceCartItem *items = [[AMAECommerceCartItem alloc] initWithProduct:product quantity:quantity revenue:price referrer:referrer];
    return items;
}

+ (AMAECommerceOrder *)ecommerceOrderForDict:(NSDictionary *)ecommerceOrderDict
{
    if (ecommerceOrderDict == nil) {
        return nil;
    }
    NSString *orderId = ecommerceOrderDict[@"orderId"];
    NSDictionary *payload = ecommerceOrderDict[@"payload"];
    NSArray *cartItemsArrayDict = ecommerceOrderDict[@"products"];
    NSMutableArray *cartItems = [[NSMutableArray alloc] init];

    for (NSDictionary *item in cartItemsArrayDict) {
        [cartItems addObject:[self ecommerceCartItemForDict:item]];
    }

    AMAECommerceOrder *order = [[AMAECommerceOrder alloc] initWithIdentifier:orderId cartItems:cartItems payload:payload];
    return order;
}

+ (AMAECommerceAmount *)ecommerceAmountForDict:(NSDictionary *)ecommerceAmountDict
{
    if (ecommerceAmountDict == nil) {
        return nil;
    }

    NSString *unit = ecommerceAmountDict[@"unit"];
    NSDecimalNumber *value = [NSDecimalNumber decimalNumberWithDecimal:[ecommerceAmountDict[@"amount"] decimalValue]];
    AMAECommerceAmount *amount = [[AMAECommerceAmount alloc] initWithUnit:unit value:value];
    return amount;
}

+ (AMAECommerce *)ecommerceForDict:(NSDictionary *)ecommerceDict
{
    if (ecommerceDict == nil) {
        return nil;
    }
    NSString *type = ecommerceDict[@"ecommerceEvent"];
    if ([type isEqualToString:@"showSceenEvent"]) {
        NSDictionary *screenDict = ecommerceDict[@"ecommerceScreen"];
        AMAECommerceScreen *screen = [self ecommerceScreenForDict:screenDict];
        AMAECommerce *event = [AMAECommerce showScreenEventWithScreen:screen];
        return event;
    }
    if ([type isEqualToString:@"showProductCardEvent"]) {
        AMAECommerceScreen *screen = [self ecommerceScreenForDict:ecommerceDict[@"ecommerceScreen"]];
        AMAECommerceProduct *product = [self ecommerceProductForDict:ecommerceDict[@"product"]];
        AMAECommerce *event = [AMAECommerce showProductCardEventWithProduct:product screen:screen];
        return event;
    }
    if ([type isEqualToString:@"showProductDetailsEvent"]) {
        AMAECommerceProduct *product = [self ecommerceProductForDict:ecommerceDict[@"product"]];
        AMAECommerceReferrer *referrer =[self ecommerceReferrerForDict:ecommerceDict[@"referrer"]];
        AMAECommerce *event = [AMAECommerce showProductDetailsEventWithProduct:product referrer:referrer];
        return event;
    }
    if ([type isEqualToString:@"addCartItemEvent"]) {
        AMAECommerceCartItem *item = [self ecommerceCartItemForDict:ecommerceDict[@"cartItem"]];
        AMAECommerce *event = [AMAECommerce addCartItemEventWithItem:item];
        return event;
    }
    if ([type isEqualToString:@"removeCartItemEvent"]) {
        AMAECommerceCartItem *item = [self ecommerceCartItemForDict:ecommerceDict[@"cartItem"]];
        AMAECommerce *event = [AMAECommerce removeCartItemEventWithItem:item];

        return event;
    }
    if ([type isEqualToString:@"beginCheckoutEvent"]) {
        AMAECommerceOrder *order=[self ecommerceOrderForDict:ecommerceDict[@"order"]];
        AMAECommerce *event = [AMAECommerce beginCheckoutEventWithOrder:order];
        return event;
    }
    if ([type isEqualToString:@"purchaseEvent"]) {
        AMAECommerceOrder *order=[self ecommerceOrderForDict:ecommerceDict[@"order"]];
        AMAECommerce *event = [AMAECommerce purchaseEventWithOrder:order];
        return event;
    }
    return nil;
}

+ (AMARevenueInfo *)revenueForDict:(NSDictionary *)revenueDict
{
    NSNumber *number = revenueDict[@"price"];
    NSDecimalNumber *price = [NSDecimalNumber decimalNumberWithDecimal:number.decimalValue];
    NSString *currency = revenueDict[@"currency"];
    AMAMutableRevenueInfo *revenueInfo = [[AMAMutableRevenueInfo alloc] initWithPriceDecimal:price currency:currency];
    if (revenueDict[@"productID"] != nil) {
        revenueInfo.productID = revenueDict[@"productID"];
    }
    if (revenueDict[@"quantity"] != nil) {
        NSNumber *quantity = revenueDict[@"quantity"];
        revenueInfo.quantity = quantity.unsignedIntValue;
    }
    if (revenueDict[@"payload"] != nil) {
        revenueInfo.payload = [self convertRevenueInfoPayload:revenueDict[@"payload"]];
    }
    NSDictionary *receiptDict = revenueDict[@"receipt"];
    if (receiptDict[@"transactionID"] != nil) {
        revenueInfo.transactionID = receiptDict[@"transactionID"];
    }
    if (receiptDict[@"receiptData"] != nil) {
        NSString *receiptDataString = receiptDict[@"receiptData"];
        revenueInfo.receiptData = [receiptDataString dataUsingEncoding:NSUTF8StringEncoding];
    }
    return revenueInfo;
}

+ (AMAAdRevenueInfo *)adRevenueForDict:(NSDictionary *)revenueDict
{
    NSNumber *number = revenueDict[@"price"];
    NSDecimalNumber *price = [NSDecimalNumber decimalNumberWithDecimal:number.decimalValue];
    NSString *currency = revenueDict[@"currency"];
    AMAMutableAdRevenueInfo *adRevenueInfo = [[AMAMutableAdRevenueInfo alloc] initWithAdRevenue:price currency:currency];
    if (revenueDict[@"adNetwork"] != nil) {
        adRevenueInfo.adNetwork = revenueDict[@"adNetwork"];
    }
    if (revenueDict[@"adUnitID"] != nil) {
        adRevenueInfo.adUnitID = revenueDict[@"adUnitID"];
    }
    if (revenueDict[@"adUnitName"] != nil) {
        adRevenueInfo.adUnitName = revenueDict[@"adUnitName"];
    }
    if (revenueDict[@"adPlacementID"] != nil) {
        adRevenueInfo.adPlacementID = revenueDict[@"adPlacementID"];
    }
    if (revenueDict[@"adPlacementName"] != nil) {
        adRevenueInfo.adPlacementName = revenueDict[@"adPlacementName"];
    }
    if (revenueDict[@"precision"] != nil) {
        adRevenueInfo.precision = revenueDict[@"precision"];
    }
    if (revenueDict[@"payload"] != nil) {
        adRevenueInfo.payload = revenueDict[@"payload"];
    }
    if (revenueDict[@"adType"] != nil) {
        adRevenueInfo.adType = [self toAdType:revenueDict[@"adType"]];
    }
    return adRevenueInfo;
}

+ (AMAAdType)toAdType:(NSString *)type
{
    if ([type isEqualToString:@"native"]){
        return AMAAdTypeNative;
    }
    if ([type isEqualToString:@"banner"]){
        return AMAAdTypeBanner;
    }
    if ([type isEqualToString:@"mrec"]){
        return AMAAdTypeMrec;
    }
    if ([type isEqualToString:@"interstitial"]){
        return AMAAdTypeInterstitial;
    }
    if ([type isEqualToString:@"rewarded"]){
        return AMAAdTypeRewarded;
    }
    if ([type isEqualToString:@"other"]){
        return AMAAdTypeOther;
    }
    return AMAAdTypeOther;
}

+ (NSDictionary *)convertRevenueInfoPayload:(NSString *)payload
{
    NSError *error = nil;
    NSDictionary *dict = [NSJSONSerialization JSONObjectWithData:[payload dataUsingEncoding:NSUTF8StringEncoding]
                                                         options:kNilOptions
                                                           error:&error];
    if (error == nil && (dict == nil || [dict isKindOfClass:[NSDictionary class]])) {
        return dict;
    }
    else {
        NSLog(@"Invalid revenue payload to report to AppMetrica %@", payload);
        return nil;
    }
}

+ (AMAUserProfile *)userProfileForDict:(NSDictionary *)userProfileDict
{
    return amarn_deserializeUserProfile(userProfileDict);
}

+ (AMAMutableReporterConfiguration *)reporterConfigurationForDictionary:(NSDictionary *)configDict
{
    NSString *apiKey = configDict[@"apiKey"];
    AMAMutableReporterConfiguration *configuration = [[AMAMutableReporterConfiguration alloc] initWithAPIKey:apiKey];
    if (configDict[@"logs"] != nil) {
        configuration.logsEnabled = [configDict[@"logs"] boolValue];
    }
    if (configDict[@"maxReportsInDatabaseCount"] != nil) {
        configuration.maxReportsInDatabaseCount = [configDict[@"maxReportsInDatabaseCount"] unsignedIntegerValue];
    }
    if (configDict[@"sessionTimeout"] != nil) {
        configuration.sessionTimeout = [configDict[@"sessionTimeout"] unsignedIntegerValue];
    }
    if (configDict[@"dataSendingEnabled"] != nil) {
        configuration.dataSendingEnabled = [configDict[@"dataSendingEnabled"] boolValue];
    }
    if (configDict[@"appEnvironment"] != nil) {
        NSDictionary *appEnvironmentMap = configDict[@"appEnvironment"];
        for (NSString *key in appEnvironmentMap) {
            [AMAAppMetrica setAppEnvironmentValue:appEnvironmentMap[key] forKey:key];
        }
    }
    if (configDict[@"userProfileID"] != nil) {
        configuration.userProfileID = configDict[@"userProfileID"];
    }
    if (configDict[@"dispatchPeriodSeconds"] != nil) {
        configuration.dispatchPeriod = [configDict[@"dispatchPeriodSeconds"] unsignedIntegerValue];
    }
    if (configDict[@"maxReportsCount"] != nil) {
        configuration.maxReportsCount = [configDict[@"maxReportsCount"] unsignedIntegerValue];
    }
    return configuration;
}

@end

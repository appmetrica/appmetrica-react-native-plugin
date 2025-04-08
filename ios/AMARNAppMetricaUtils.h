
#import <CoreLocation/CoreLocation.h>
#import <AppMetricaCore/AppMetricaCore.h>
#import <AppMetricaCrashes/AppMetricaCrashes.h>
#import "AMARNUserProfileSerializer.h"

@interface AMARNAppMetricaUtils : NSObject

+ (AMAAppMetricaConfiguration *)configurationForDictionary:(NSDictionary *)configDict;
+ (AMAAppMetricaCrashesConfiguration *)crashConfigurationForDictionary:(NSDictionary *)crashConfigDict;
+ (CLLocation *)locationForDictionary:(NSDictionary *)locationDict;
+ (AMAECommerceScreen *)ecommerceScreenForDict:(NSDictionary *)ecommerceScreenDict;
+ (AMAECommerceAmount *)ecommerceAmountForDict:(NSDictionary *)ecommerceAmountDict;
+ (AMAECommercePrice *)ecommercePriceForDict:(NSDictionary *)ecommercePriceDict;
+ (AMAECommerceReferrer *)ecommerceReferrerForDict:(NSDictionary *)ecommerceReferrerDict;
+ (AMAECommerceProduct *)ecommerceProductForDict:(NSDictionary *)ecommerceProductDict;
+ (AMAECommerceCartItem *)ecommerceCartItemForDict:(NSDictionary *)ecommerceCartItemDict;
+ (AMAECommerceOrder *)ecommerceOrderForDict:(NSDictionary *)ecommerceOrderDict;
+ (AMAECommerce *)ecommerceForDict:(NSDictionary *)ecommerceDict;
+ (AMARevenueInfo *)revenueForDict:(NSDictionary *)revenueDict;
+ (AMAAdRevenueInfo *)adRevenueForDict:(NSDictionary *)revenueDict;
+ (AMAAdType)toAdType:(NSString *)type;
+ (NSDictionary *)convertRevenueInfoPayload:(NSString *)payload;
+ (AMAUserProfile *)userProfileForDict:(NSDictionary *)userProfileDict;
+ (AMAMutableReporterConfiguration *)reporterConfigurationForDictionary:(NSDictionary *)configDict;

@end

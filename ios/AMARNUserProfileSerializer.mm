
#import "AMARNUserProfileSerializer.h"

AMAUserProfileUpdate *amarn_birthDateAttributeFromDictionary(NSDictionary *dictionary)
{
    NSString *type = dictionary[@"type"];
    if ([type isEqualToString:@"BirthDateWithAge"]) {
        return [[AMAProfileAttribute birthDate] withAge:[dictionary[@"age"] unsignedIntegerValue]];
    }
    if ([type isEqualToString:@"BirthDateWithYear"]) {
        return [[AMAProfileAttribute birthDate] withYear:[dictionary[@"year"] unsignedIntegerValue]];
    }
    if ([type isEqualToString:@"BirthDateWithMonth"]) {
        return [[AMAProfileAttribute birthDate] withYear:[dictionary[@"year"] unsignedIntegerValue]
                                                   month:[dictionary[@"month"] unsignedIntegerValue]];
    }
    if ([type isEqualToString:@"BirthDateWithDay"]) {
        return [[AMAProfileAttribute birthDate] withYear:[dictionary[@"year"] unsignedIntegerValue]
                                                   month:[dictionary[@"month"] unsignedIntegerValue]
                                                     day:[dictionary[@"day"] unsignedIntegerValue]];
    }
    if ([type isEqualToString:@"BirthDateValueReset"]) {
        return [[AMAProfileAttribute birthDate] withValueReset];
    }
    return nil;
}

AMAUserProfileUpdate *amarn_customBoolAttributeUpdateFromDictionary(NSDictionary *dictionary)
{
    NSString *type = dictionary[@"type"];
    if ([type isEqualToString:@"BooleanValue"]) {
        NSString *key = dictionary[@"key"];
        bool value = [dictionary[@"value"] boolValue];
        if ([dictionary[@"ifUndefined"] boolValue]) {
            return [[AMAProfileAttribute customBool:key] withValueIfUndefined:value];
        }
        return [[AMAProfileAttribute customBool:key] withValue:value];
    }
    if ([type isEqualToString:@"BooleanValueReset"]) {
        NSString *key = dictionary[@"key"];
        return [[AMAProfileAttribute customBool:key] withValueReset];
    }
    return nil;
}

AMAUserProfileUpdate *amarn_customCounterAttributeUpdateFromDictionary(NSDictionary *dictionary)
{
    NSString *type = dictionary[@"type"];
    if ([type isEqualToString:@"Counter"]) {
        NSString *key = dictionary[@"key"];
        double value = [dictionary[@"delta"] doubleValue];
        return [[AMAProfileAttribute customCounter:key] withDelta:value];
    }
    return nil;
}

AMAGenderType amarn_genderTypeForString(NSString *genderStr)
{
    if ([genderStr isEqualToString:@"male"]) {
        return AMAGenderTypeMale;
    }
    if ([genderStr isEqualToString:@"female"]) {
        return AMAGenderTypeFemale;
    }
    return AMAGenderTypeOther;
}

AMAUserProfileUpdate *amarn_genderAttributeFromDictionary(NSDictionary *dictionary)
{
    NSString *type = dictionary[@"type"];
    if ([type isEqualToString:@"GenderValue"]) {
        AMAGenderType value = amarn_genderTypeForString(dictionary[@"value"]);
        return [[AMAProfileAttribute gender] withValue:value];
    }
    if ([type isEqualToString:@"GenderValueReset"]) {
        return [[AMAProfileAttribute gender] withValueReset];
    }
    return nil;
}

AMAUserProfileUpdate *amarn_nameAttributeFromDictionary(NSDictionary *dictionary)
{
    NSString *type = dictionary[@"type"];
    if ([type isEqualToString:@"NameValue"]) {
        NSString *value = dictionary[@"value"];
        return [[AMAProfileAttribute name] withValue:value];
    }
    if ([type isEqualToString:@"NameValueReset"]) {
        return [[AMAProfileAttribute name] withValueReset];
    }
    return nil;
}

AMAUserProfileUpdate *amarn_notificationEnabledAttributeFromDictionary(NSDictionary *dictionary)
{
    NSString *type = dictionary[@"type"];
    if ([type isEqualToString:@"NotificationsEnabledValue"]) {
        bool value = [dictionary[@"value"] boolValue];
        return [[AMAProfileAttribute notificationsEnabled] withValue:value];
    }
    if ([type isEqualToString:@"NotificationsEnabledValueReset"]) {
        return [[AMAProfileAttribute notificationsEnabled] withValueReset];
    }
    return nil;
}

AMAUserProfileUpdate *amarn_customNumberAttributeUpdateFromDictionary(NSDictionary *dictionary)
{
    NSString *type = dictionary[@"type"];
    if ([type isEqualToString:@"NumberValue"]) {
        NSString *key = dictionary[@"key"];
        double value = [dictionary[@"value"] doubleValue];
        if ([dictionary[@"ifUndefined"] boolValue]) {
            return [[AMAProfileAttribute customNumber:key] withValueIfUndefined:value];
        }
        return [[AMAProfileAttribute customNumber:key] withValue:value];
    }
    if ([type isEqualToString:@"NumberValueReset"]) {
        NSString *key = dictionary[@"key"];
        return [[AMAProfileAttribute customNumber:key] withValueReset];
    }
    return nil;
}

AMAUserProfileUpdate *amarn_customStringAttributeUpdateFromDictionary(NSDictionary *dictionary)
{
    NSString *type = dictionary[@"type"];
    if ([type isEqualToString:@"StringValue"]) {
        NSString *key = dictionary[@"key"];
        NSString *value = dictionary[@"value"];
        if ([dictionary[@"ifUndefined"] boolValue]) {
            return [[AMAProfileAttribute customString:key] withValueIfUndefined:value];
        }
        return [[AMAProfileAttribute customString:key] withValue:value];
    }
    if ([type isEqualToString:@"StringValueReset"]) {
        NSString *key = dictionary[@"key"];
        return [[AMAProfileAttribute customString:key] withValueReset];
    }
    return nil;
}

AMAUserProfileUpdate *amarn_userProfileUpdateFromDictionary(NSDictionary *dictionary)
{
    return amarn_birthDateAttributeFromDictionary(dictionary)
        ?: amarn_customBoolAttributeUpdateFromDictionary(dictionary)
        ?: amarn_customCounterAttributeUpdateFromDictionary(dictionary)
        ?: amarn_genderAttributeFromDictionary(dictionary)
        ?: amarn_nameAttributeFromDictionary(dictionary)
        ?: amarn_notificationEnabledAttributeFromDictionary(dictionary)
        ?: amarn_customNumberAttributeUpdateFromDictionary(dictionary)
        ?: amarn_customStringAttributeUpdateFromDictionary(dictionary);
}

AMAUserProfile *amarn_deserializeUserProfile(NSDictionary *dictionary) {
    
    if (!dictionary) {
        return nil;
    }
    
    NSArray *attributes = dictionary[@"attributes"];
    
    if (!attributes && attributes.count == 0) {
        return nil;
    }
    
    AMAMutableUserProfile *userProfile = [[AMAMutableUserProfile alloc] init];
    
    for (NSDictionary* attribute in attributes) {
        
        AMAUserProfileUpdate *update = amarn_userProfileUpdateFromDictionary(attribute);
        
        if (!update) {
            return nil;
        }
        
        [userProfile apply:update];
    }
    
    
    return userProfile;
}

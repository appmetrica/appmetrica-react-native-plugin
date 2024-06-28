package io.appmetrica.analytics.reactnative;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import io.appmetrica.analytics.profile.Attribute;
import io.appmetrica.analytics.profile.BirthDateAttribute;
import io.appmetrica.analytics.profile.BooleanAttribute;
import io.appmetrica.analytics.profile.CounterAttribute;
import io.appmetrica.analytics.profile.GenderAttribute;
import io.appmetrica.analytics.profile.NameAttribute;
import io.appmetrica.analytics.profile.NotificationsEnabledAttribute;
import io.appmetrica.analytics.profile.NumberAttribute;
import io.appmetrica.analytics.profile.StringAttribute;
import io.appmetrica.analytics.profile.UserProfile;
import io.appmetrica.analytics.profile.UserProfileUpdate;

final class UserProfileSerializer {
    private UserProfileSerializer() {}

    @NonNull
    public static UserProfile fromReadableMap(@NonNull ReadableMap profileMap){

        ReadableArray attributes = profileMap.getArray("attributes");
        if (attributes == null) {
            throw new IllegalArgumentException();
        }
        UserProfile.Builder builder = UserProfile.newBuilder();

        for (int idx = 0; idx < attributes.size(); idx++) {

            ReadableMap attribute = attributes.getMap(idx);
            if (attribute != null) {
                builder.apply(parseUserProfileUpdate(attribute));
            }
        }

        return builder.build();
    }

    @NonNull
    private static UserProfileUpdate<?> parseUserProfileUpdate(@NonNull ReadableMap map){
        String type = map.getString("type");

        if (type == null) {
            throw new IllegalArgumentException("Type should not be null");
        }

        if (type.startsWith("BirthDate")) {
            return parseBirthDate(type, map);
        }
        if (type.startsWith("Boolean")) {
            return parseBoolean(type, map);
        }
        if (type.startsWith("Counter")) {
            return parseCounter(type, map);
        }
        if (type.startsWith("Gender")) {
            return parseGender(type, map);
        }
        if (type.startsWith("Name")) {
            return parseName(type, map);
        }
        if (type.startsWith("NotificationsEnabled")) {
            return parseNotificationsEnabled(type, map);
        }
        if (type.startsWith("Number")) {
            return parseNumber(type, map);
        }
        if (type.startsWith("String")) {
            return parseString(type, map);
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }

    @NonNull
    private static UserProfileUpdate<?> parseBirthDate(@NonNull String type, @NonNull ReadableMap map) {
        BirthDateAttribute att = Attribute.birthDate();
        switch (type) {
            case "BirthDateWithAge": {
                int age = map.getInt("age");
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withAgeIfUndefined(age) : att.withAge(age);
            }
            case "BirthDateWithYear": {
                int year = map.getInt("year");
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withBirthDateIfUndefined(year) : att.withBirthDate(year);
            }
            case "BirthDateWithMonth": {
                int year = map.getInt("year");
                int month = map.getInt("month");
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withBirthDateIfUndefined(year, month) : att.withBirthDate(year, month);
            }
            case "BirthDateWithDay": {
                int year = map.getInt("year");
                int month = map.getInt("month");
                int days = map.getInt("day");
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withBirthDateIfUndefined(year, month, days) : att.withBirthDate(year, month, days);
            }
            case "BirthDateValueReset": {
                return att.withValueReset();
            }
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }

    @NonNull
    private static UserProfileUpdate<?> parseBoolean(@NonNull String type, @NonNull ReadableMap map) {
        String key = map.getString("key");
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null");
        }
        BooleanAttribute att = Attribute.customBoolean(key);
        switch (type) {
            case "BooleanValue": {
                boolean value = map.getBoolean("value");
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withValueIfUndefined(value) : att.withValue(value);
            }
            case "BooleanValueReset": {
                return att.withValueReset();
            }
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }

    @NonNull
    private static UserProfileUpdate<?> parseCounter(@NonNull String type, @NonNull ReadableMap map)  {
        String key = map.getString("key");
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null");
        }
        CounterAttribute att = Attribute.customCounter(key);
        if (type.equals("Counter")) {
            double delta = map.getDouble("delta");
            return att.withDelta(delta);
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }

    @NonNull
    private static UserProfileUpdate<?> parseGender(@NonNull String type, @NonNull ReadableMap map) {
        GenderAttribute att = Attribute.gender();
        // BirthDate
        switch (type) {
            case "GenderValue": {
                String value = map.getString("value");
                if (value == null) {
                    throw new IllegalArgumentException("Value should not be null");
                }

                GenderAttribute.Gender gender = getGender(value);
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withValueIfUndefined(gender) : att.withValue(gender);
            }
            case "GenderValueReset": {
                return att.withValueReset();
            }
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }

    @NonNull
    private static GenderAttribute.Gender getGender(@NonNull String value) {
        switch (value) {
            case "female":
                return GenderAttribute.Gender.FEMALE;
            case "male":
                return GenderAttribute.Gender.MALE;
            default:
                return GenderAttribute.Gender.OTHER;
        }
    }

    @NonNull
    private static UserProfileUpdate<?> parseName(@NonNull String type, @NonNull ReadableMap map) {
        NameAttribute att = Attribute.name();
        switch (type) {
            case "NameValue": {
                String value = map.getString("value");
                if (value == null) {
                    throw new IllegalArgumentException("Value should not be null");
                }
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withValueIfUndefined(value) : att.withValue(value);
            }
            case "NameValueReset": {
                return att.withValueReset();
            }
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }

    @NonNull
    private static UserProfileUpdate<?> parseNotificationsEnabled(@NonNull String type, @NonNull ReadableMap map) {
        NotificationsEnabledAttribute att = Attribute.notificationsEnabled();
        switch (type) {
            case "NotificationsEnabledValue": {
                boolean value = map.getBoolean("value");
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withValueIfUndefined(value) : att.withValue(value);
            }
            case "NotificationsEnabledValueReset": {
                return att.withValueReset();
            }
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }

    @NonNull
    private static UserProfileUpdate<?> parseNumber(@NonNull String type, @NonNull ReadableMap map) {
        String key = map.getString("key");
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null");
        }
        NumberAttribute att = Attribute.customNumber(key);
        switch (type) {
            case "NumberValue": {
                double value = map.getDouble("value");
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withValueIfUndefined(value) : att.withValue(value);
            }
            case "NumberValueReset": {
                return att.withValueReset();
            }
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }

    @NonNull
    private static UserProfileUpdate<?> parseString(@NonNull String type, @NonNull ReadableMap map) {
        String key = map.getString("key");
        if (key == null) {
            throw new IllegalArgumentException("Key should not be null");
        }
        StringAttribute att = Attribute.customString(key);
        switch (type) {
            case "StringValue": {
                String value = map.getString("value");
                if (value == null) {
                    throw new IllegalArgumentException("Value should not be null");
                }
                boolean ifUndefined = map.getBoolean("ifUndefined");
                return ifUndefined ? att.withValueIfUndefined(value) : att.withValue(value);
            }
            case "StringValueReset": {
                return att.withValueReset();
            }
        }
        throw new IllegalArgumentException("Unknown UserProfile type " + type);
    }
}

package io.appmetrica.analytics.reactnative;

import android.text.TextUtils;

import com.facebook.react.bridge.ReadableMap;

import org.json.JSONObject;

import io.appmetrica.analytics.ModulesFacade;

final class ExternalAttributionSerializer {
    private ExternalAttributionSerializer() {}

    public static int parseSource(String sourceString) {
        if (TextUtils.isEmpty(sourceString)) {
            return -1;
        }

        switch (sourceString) {
            case "AppsFlyer":
                return ModulesFacade.EXTERNAL_ATTRIBUTION_APPSFLYER;
            case "Adjust":
                return ModulesFacade.EXTERNAL_ATTRIBUTION_ADJUST;
            case "Kochava":
                return ModulesFacade.EXTERNAL_ATTRIBUTION_KOCHAVA;
            case "Tenjin":
                return ModulesFacade.EXTERNAL_ATTRIBUTION_TENJIN;
            case "Airbridge":
                return ModulesFacade.EXTERNAL_ATTRIBUTION_AIRBRIDGE;
            case "Singular":
                return ModulesFacade.EXTERNAL_ATTRIBUTION_SINGULAR;
            default:
                return -1;
        }
    }

    public static String parseValue(ReadableMap valueMap) {
        if (valueMap == null) {
            return "";
        }

        return new JSONObject(valueMap.toHashMap()).toString();
    }
}

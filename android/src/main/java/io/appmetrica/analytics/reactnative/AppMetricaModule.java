package io.appmetrica.analytics.reactnative;

import android.app.Activity;
import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.AppMetricaConfig;
import io.appmetrica.analytics.ecommerce.ECommerceEvent;

@ReactModule(name = AppMetricaModule.NAME)
public class AppMetricaModule extends ReactContextBaseJavaModule {

    public static final String NAME = "AppMetrica";
    public static final String TAG = "AppMetricaModule";

    @NonNull
    private final ReactApplicationContext reactContext;

    public AppMetricaModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void activate(ReadableMap configMap) {
        AppMetricaConfig config = Utils.toAppMetricaConfig(configMap);
        AppMetrica.activate(reactContext, config);
        if (Boolean.TRUE.equals(config.sessionsAutoTrackingEnabled)) {
            AppMetrica.resumeSession(getCurrentActivity());
        }
    }

    @ReactMethod
    public void getLibraryApiLevel(Promise promise) {
        promise.resolve(AppMetrica.getLibraryApiLevel());
    }

    @ReactMethod
    public void getLibraryVersion(Promise promise) {
        promise.resolve(AppMetrica.getLibraryVersion());
    }

    @ReactMethod
    public void pauseSession() {
        AppMetrica.pauseSession(getCurrentActivity());
    }

    @ReactMethod
    public void reportAppOpen(String deeplink) {
        AppMetrica.reportAppOpen(deeplink);
    }

    @ReactMethod
    public void reportError(String identifier, String message) {
        try {
            Integer.valueOf("00xffWr0ng");
        } catch (Throwable error) {
            AppMetrica.reportError(identifier, message, error);
        }
    }

    @ReactMethod
    public void reportEvent(String eventName, ReadableMap attributes) {
        if (attributes == null) {
            AppMetrica.reportEvent(eventName);
        } else {
            AppMetrica.reportEvent(eventName, attributes.toHashMap());
        }
    }

    @ReactMethod
    public void requestStartupParams(ReadableArray identifiers, Callback listener) {
        AppMetrica.requestStartupParams(reactContext, new ReactNativeStartupParamsListener(listener), Utils.toStartupKeyList(identifiers));
    }

    @ReactMethod
    public void resumeSession() {
        AppMetrica.resumeSession(getCurrentActivity());
    }

    @ReactMethod
    public void sendEventsBuffer() {
        AppMetrica.sendEventsBuffer();
    }

    @ReactMethod
    public void setLocation(ReadableMap locationMap) {
        AppMetrica.setLocation(Utils.toLocation(locationMap));
    }

    @ReactMethod
    public void setLocationTracking(boolean enabled) {
        AppMetrica.setLocationTracking(enabled);
    }

    @ReactMethod
    public void setDataSendingEnabled(boolean enabled) {
        AppMetrica.setDataSendingEnabled(enabled);
    }

    @ReactMethod
    public void setUserProfileID(String userProfileID) {
        AppMetrica.setUserProfileID(userProfileID);
    }

    @ReactMethod
    public void reportECommerce(ReadableMap ecommerceEvent) {
        ECommerceEvent event = Utils.toECommerceEvent(ecommerceEvent);
        if (event != null) {
            AppMetrica.reportECommerce(event);
        } else {
            Log.w(TAG, "ECommerceEvent is null");
        }
    }

    @ReactMethod
    public void reportRevenue(ReadableMap revenue) {
        AppMetrica.reportRevenue(Utils.toRevenue(revenue));
    }

    @ReactMethod
    public void reportAdRevenue(ReadableMap revenue) {
        AppMetrica.reportAdRevenue(Utils.toAdRevenue(revenue));
    }

    @ReactMethod
    public void reportUserProfile(ReadableMap userProfile) {
        try {
            AppMetrica.reportUserProfile(Utils.toUserProfile(userProfile));
        } catch (Throwable e) {
            Log.w(TAG, "Cannot parse user profile", e);
        }
    }

    @ReactMethod
    public void putErrorEnvironmentValue(String key, String value) {
        AppMetrica.putErrorEnvironmentValue(key, value);
    }
}

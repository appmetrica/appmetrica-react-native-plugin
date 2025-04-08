package io.appmetrica.analytics.reactnative;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.ecommerce.ECommerceEvent;
import io.appmetrica.analytics.plugins.PluginErrorDetails;

public class ReporterModule extends ReactContextBaseJavaModule {

    public static final String NAME = "AppMetricaReporter";

    private static final String TAG = "ReporterModule";

    @NonNull
    private final ReactApplicationContext reactContext;

    public ReporterModule(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void reportError(String apiKey, String identifier, String message, ReadableMap _reason) {
        PluginErrorDetails errorDetails = _reason != null ? ExceptionSerializer.fromObject(_reason) : null;
        AppMetrica.getReporter(reactContext, apiKey).getPluginExtension().reportError(identifier, message, errorDetails);
    }

    @ReactMethod
    public void reportErrorWithoutIdentifier(String apiKey, String message, ReadableMap error) {
        PluginErrorDetails details = ExceptionSerializer.fromObject(error);
        if (details.getStacktrace().isEmpty()) {
            AppMetrica.getReporter(reactContext, apiKey).getPluginExtension().reportError("Errors without stacktrace", message, details);
        } else {
            AppMetrica.getReporter(reactContext, apiKey).getPluginExtension().reportError(details, message);
        }
    }

    @ReactMethod
    public void reportUnhandledException(String apiKey, ReadableMap error) {
        PluginErrorDetails details = ExceptionSerializer.fromObject(error);
        AppMetrica.getReporter(reactContext, apiKey).getPluginExtension().reportUnhandledException(details);
    }

    @ReactMethod
    public void reportEvent(String apiKey, String eventName, ReadableMap attributes) {
        if (attributes == null) {
            AppMetrica.getReporter(reactContext, apiKey).reportEvent(eventName);
        } else {
            AppMetrica.getReporter(reactContext, apiKey).reportEvent(eventName, attributes.toHashMap());
        }
    }

    @ReactMethod
    public void pauseSession(String apiKey) {
        AppMetrica.getReporter(reactContext, apiKey).pauseSession();
    }

    @ReactMethod
    public void resumeSession(String apiKey) {
        AppMetrica.getReporter(reactContext, apiKey).resumeSession();
    }

    @ReactMethod
    public void sendEventsBuffer(String apiKey) {
        AppMetrica.getReporter(reactContext, apiKey).sendEventsBuffer();
    }

    @ReactMethod
    public void clearAppEnvironment(String apiKey) {
        AppMetrica.getReporter(reactContext, apiKey).clearAppEnvironment();
    }

    @ReactMethod
    public void putAppEnvironmentValue(String apiKey, String key, String value) {
        AppMetrica.getReporter(reactContext, apiKey).putAppEnvironmentValue(key, value);
    }

    @ReactMethod
    public void setUserProfileID(String apiKey, String userProfileID) {
        AppMetrica.getReporter(reactContext, apiKey).setUserProfileID(userProfileID);
    }

    @ReactMethod
    public void reportUserProfile(String apiKey, ReadableMap userProfile) {
        try {
            AppMetrica.getReporter(reactContext, apiKey).reportUserProfile(Utils.toUserProfile(userProfile));
        } catch (Throwable e) {
            Log.w(TAG, "Cannot parse user profile", e);
        }
    }

    @ReactMethod
    public void setDataSendingEnabled(String apiKey, boolean enabled) {
        AppMetrica.getReporter(reactContext, apiKey).setDataSendingEnabled(enabled);
    }

    @ReactMethod
    public void reportAdRevenue(String apiKey, ReadableMap AdRevenueMap) {
        AppMetrica.getReporter(reactContext, apiKey).reportAdRevenue(Utils.toAdRevenue(AdRevenueMap));
    }

    @ReactMethod
    public void reportECommerce(String apiKey, ReadableMap ecommerceEvent) {
        ECommerceEvent event = Utils.toECommerceEvent(ecommerceEvent);
        if (event != null) {
            AppMetrica.getReporter(reactContext, apiKey).reportECommerce(event);
        } else {
            Log.w(TAG, "ECommerceEvent is null");
        }
    }

    @ReactMethod
    public void reportRevenue(String apiKey, ReadableMap revenueMap) {
        AppMetrica.getReporter(reactContext, apiKey).reportRevenue(Utils.toRevenue(revenueMap));
    }
}

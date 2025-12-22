package io.appmetrica.analytics.reactnative;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;
import java.util.Map;

import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.AppMetricaConfig;
import io.appmetrica.analytics.ModulesFacade;
import io.appmetrica.analytics.StartupParamsCallback;
import io.appmetrica.analytics.ecommerce.ECommerceEvent;
import io.appmetrica.analytics.plugins.PluginErrorDetails;


public class AppMetricaModule extends NativeAppMetricaSpec {

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

    @Override
    public void activate(ReadableMap configMap) {
        AppMetricaConfig config = Utils.toAppMetricaConfig(configMap);
        AppMetrica.activate(reactContext, config);
        if (!Boolean.FALSE.equals(config.sessionsAutoTrackingEnabled)) {
            AppMetrica.resumeSession(getCurrentActivity());
        }
    }

    @Override
    public double getLibraryApiLevel() {
        return AppMetrica.getLibraryApiLevel();
    }

    @Override
    public String getLibraryVersion() {
        return AppMetrica.getLibraryVersion();
    }

    @Override
    public void pauseSession() {
        AppMetrica.pauseSession(reactContext.getCurrentActivity());
    }

    @Override
    public void reportAppOpen(String deeplink) {
        AppMetrica.reportAppOpen(deeplink);
    }

    @Override
    public void reportError(String identifier, String message, ReadableMap _reason) {
        PluginErrorDetails errorDetails = _reason != null ? ExceptionSerializer.fromObject(_reason) : null;
        AppMetrica.getPluginExtension().reportError(identifier, message, errorDetails);
    }

    @Override
    public void reportEvent(String eventName, ReadableMap attributes) {
        if (attributes == null) {
            AppMetrica.reportEvent(eventName);
        } else {
            AppMetrica.reportEvent(eventName, attributes.toHashMap());
        }
    }

    @Override
    public void requestStartupParams(Callback listener, ReadableArray identifiers) {
        AppMetrica.requestStartupParams(reactContext, new ReactNativeStartupParamsListener(listener), Utils.toListOfStrings(identifiers));
    }

    @Override
    public void resumeSession() {
        AppMetrica.resumeSession(getCurrentActivity());
    }

    @Override
    public void sendEventsBuffer() {
        AppMetrica.sendEventsBuffer();
    }

    @Override
    public void setLocation(ReadableMap locationMap) {
        AppMetrica.setLocation(Utils.toLocation(locationMap));
    }

    @Override
    public void setLocationTracking(boolean enabled) {
        AppMetrica.setLocationTracking(enabled);
    }

    @Override
    public void setDataSendingEnabled(boolean enabled) {
        AppMetrica.setDataSendingEnabled(enabled);
    }

    @Override
    public void setUserProfileID(String userProfileID) {
        AppMetrica.setUserProfileID(userProfileID);
    }

    @Override
    public void reportECommerce(ReadableMap ecommerceEvent) {
        ECommerceEvent event = Utils.toECommerceEvent(ecommerceEvent);
        if (event != null) {
            AppMetrica.reportECommerce(event);
        } else {
            Log.w(TAG, "ECommerceEvent is null");
        }
    }

    @Override
    public void reportRevenue(ReadableMap revenue) {
        AppMetrica.reportRevenue(Utils.toRevenue(revenue));
    }

    @Override
    public void reportAdRevenue(ReadableMap revenue) {
        AppMetrica.reportAdRevenue(Utils.toAdRevenue(revenue));
    }

    @Override
    public void reportUserProfile(ReadableMap userProfile) {
        try {
            AppMetrica.reportUserProfile(Utils.toUserProfile(userProfile));
        } catch (Throwable e) {
            Log.w(TAG, "Cannot parse user profile", e);
        }
    }

    @Override
    public void putErrorEnvironmentValue(String key, String value) {
        AppMetrica.putErrorEnvironmentValue(key, value);
    }

    @Override
    public void reportErrorWithoutIdentifier(String message, ReadableMap error) {
        PluginErrorDetails details = ExceptionSerializer.fromObject(error);
        if (details.getStacktrace().isEmpty()) {
            AppMetrica.getPluginExtension().reportError("Errors without stacktrace", message, details);
        } else {
            AppMetrica.getPluginExtension().reportError(details, message);
        }
    }

    @Override
    public void reportUnhandledException(ReadableMap error) {
        PluginErrorDetails details = ExceptionSerializer.fromObject(error);
        AppMetrica.getPluginExtension().reportUnhandledException(details);
    }

    @Override
    public void reportExternalAttribution(ReadableMap attribution) {
        ModulesFacade.reportExternalAttribution(
                ExternalAttributionSerializer.parseSource(attribution.getString("source")),
                ExternalAttributionSerializer.parseValue(attribution.getMap("value"))
        );
    }

    @Override
    public void putAppEnvironmentValue(String key, String value) {
        AppMetrica.putAppEnvironmentValue(key, value);
    }

    @Override
    public void clearAppEnvironment() {
        AppMetrica.clearAppEnvironment();
    }

    @Override
    public void activateReporter(ReadableMap configMap) {
        AppMetrica.activateReporter(reactContext, Utils.toReporterConfig(configMap));
    }

    @Override
    public void touchReporter(String apiKey) {
        AppMetrica.getReporter(reactContext, apiKey);
    }

    @Override
    public String getDeviceId() {
        return AppMetrica.getDeviceId(reactContext);
    }

    @Override
    public String getUuid() {
        return AppMetrica.getUuid(reactContext);
    }
    @Override
    public void requestDeferredDeeplink(Callback failureCallback, Callback successCallback) {
        AppMetrica.requestDeferredDeeplink(new ReactNativeDeferredDeeplinkListener(failureCallback, successCallback));
    }

    @Override
    public void requestDeferredDeeplinkParameters(Callback failureCallback, Callback successCallback) {
        AppMetrica.requestDeferredDeeplinkParameters(new ReactNativeDeferredDeeplinkParametersListener(failureCallback, successCallback));
    }

    @Override
    protected Map<String, Object> getTypedExportedConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put("DEVICE_ID_HASH_KEY", StartupParamsCallback.APPMETRICA_DEVICE_ID_HASH);
        constants.put("DEVICE_ID_KEY", StartupParamsCallback.APPMETRICA_DEVICE_ID);
        constants.put("UUID_KEY", StartupParamsCallback.APPMETRICA_UUID);
        return constants;
    }
}

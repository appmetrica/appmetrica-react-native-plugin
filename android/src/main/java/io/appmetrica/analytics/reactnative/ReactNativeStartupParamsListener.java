package io.appmetrica.analytics.reactnative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import io.appmetrica.analytics.StartupParamsCallback;

public class ReactNativeStartupParamsListener implements StartupParamsCallback {

    @NonNull
    private final Callback listener;

    ReactNativeStartupParamsListener(@NonNull Callback listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(@Nullable Result result) {
        listener.invoke(toParamsMap(result), null);
    }

    @Override
    public void onRequestError(@NonNull Reason reason, @Nullable Result result) {
        listener.invoke(null, reason.value);
    }

    private static WritableMap toParamsMap(@Nullable Result result){
        if (result == null) return null;
        WritableMap map = new WritableNativeMap();
        map.putString("deviceId", result.deviceId);
        map.putString("deviceIdHash", result.deviceIdHash);
        map.putString("uuid", result.uuid);
        return map;
    }
}

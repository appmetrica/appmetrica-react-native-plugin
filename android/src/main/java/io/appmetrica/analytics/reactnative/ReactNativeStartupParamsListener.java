package io.appmetrica.analytics.reactnative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;

import java.util.Map;

import io.appmetrica.analytics.StartupParamsCallback;
import io.appmetrica.analytics.StartupParamsItem;

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
        listener.invoke(toParamsMap(result), reason.value);
    }

    private static WritableMap convertParametersItem(StartupParamsItem paramsItem) {
        WritableMap itemMap = Arguments.createMap();
        if (paramsItem.getId() != null) {
            itemMap.putString("id", paramsItem.getId());
        }
        if (paramsItem.getErrorDetails() != null) {
            itemMap.putString("errorDetails", paramsItem.getErrorDetails());
        }
        itemMap.putString("status", paramsItem.getStatus().name());
        return itemMap;
    }

    private static WritableMap toParamsMap(@Nullable Result result){
        if (result == null) return null;
        WritableMap paramsMap = Arguments.createMap();
        for (Map.Entry<String, StartupParamsItem> entry : result.parameters.entrySet()) {
            paramsMap.putMap(entry.getKey(), convertParametersItem(entry.getValue()));
        }
        return paramsMap;
    }
}

package io.appmetrica.analytics.reactnative;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.Arguments;

import io.appmetrica.analytics.DeferredDeeplinkListener;
import io.appmetrica.analytics.DeferredDeeplinkParametersListener;
import java.util.Map;

public class ReactNativeDeferredDeeplinkParametersListener implements DeferredDeeplinkParametersListener {
    @NonNull
    private final Callback failureListener;

    @NonNull
    private final Callback successListener;

    ReactNativeDeferredDeeplinkParametersListener(@NonNull Callback failureCallback, @NonNull Callback successCallback) {
        this.failureListener = failureCallback;
        this.successListener = successCallback;
    }

    @Override
    public void onParametersLoaded(@NonNull Map<String, String> map) {
      WritableMap writableMap = Arguments.createMap();
      for (Map.Entry<String, String> entry : map.entrySet()) {
        writableMap.putString(entry.getKey(), entry.getValue());
      }
      successListener.invoke(writableMap, null);
    }

    @Override
    public void onError(@NonNull Error error, @NonNull String referrer) {
        failureListener.invoke(getErrorStr(error), referrer);
    }

    @NonNull
    private static String getErrorStr(@NonNull DeferredDeeplinkParametersListener.Error error) {
        switch (error) {
          case NO_REFERRER:
              return "NO_REFERRER";
          case NOT_A_FIRST_LAUNCH:
              return "NOT_A_FIRST_LAUNCH";
          case PARSE_ERROR:
              return "PARSE_ERROR";
          default:
              return "UNKNOWN";
        }
    }
}

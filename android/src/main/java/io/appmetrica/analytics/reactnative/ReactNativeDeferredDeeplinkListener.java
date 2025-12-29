package io.appmetrica.analytics.reactnative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Callback;

import io.appmetrica.analytics.DeferredDeeplinkListener;

public class ReactNativeDeferredDeeplinkListener implements DeferredDeeplinkListener {

    @NonNull
    private final Callback failureListener;

    @NonNull
    private final Callback successListener;

    ReactNativeDeferredDeeplinkListener(@NonNull Callback failureCallback, @NonNull Callback successCallback) {
        this.failureListener = failureCallback;
        this.successListener = successCallback;
    }

    @Override
    public void onDeeplinkLoaded(@NonNull String deeplink) {
        successListener.invoke(deeplink);
    }

    @Override
    public void onError(@NonNull Error error, @Nullable String referrer) {
        failureListener.invoke(getErrorStr(error), referrer);
    }

    @NonNull
    private static String getErrorStr(@NonNull Error error) {
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

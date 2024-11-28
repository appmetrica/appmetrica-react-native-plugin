package io.appmetrica.analytics.reactnative;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.LinkedList;
import java.util.List;

import io.appmetrica.analytics.plugins.PluginErrorDetails;
import io.appmetrica.analytics.plugins.StackTraceItem;

final class ExceptionSerializer {
    private ExceptionSerializer() {}
    private static final String TAG = "ExceptionSerializer";

    @NonNull
    public static PluginErrorDetails fromObject(@NonNull ReadableMap exception) {
        PluginErrorDetails.Builder builder = new PluginErrorDetails.Builder();
        builder.withPlatform(PluginErrorDetails.Platform.REACT_NATIVE);
        if (exception.hasKey("errorName")) {
            builder.withExceptionClass(exception.getString("errorName"));
        }
        if (exception.hasKey("message")) {
            builder.withMessage(exception.getString("message"));
        }
        if (exception.hasKey("stackTrace")) {
            builder.withStacktrace(getStackTrace(exception.getArray("stackTrace")));
        }
        if (exception.hasKey("virtualMachineVersion")) {
            builder.withVirtualMachineVersion(exception.getString("virtualMachineVersion"));
        }
        if (exception.hasKey("pluginEnvironment")) {
            builder.withPluginEnvironment(Utils.toMapOfStrings(exception.getMap("pluginEnvironment")));
        }
        return builder.build();
    }

    @Nullable
    private static List<StackTraceItem> getStackTrace(@Nullable ReadableArray stackTraceArray) {
        if (stackTraceArray == null) {
            return null;
        }
        List<StackTraceItem> items = new LinkedList<>();
        for (int idx = 0; idx < stackTraceArray.toArrayList().size(); idx++) {
            items.add(getStackTraceItem(stackTraceArray.getMap(idx)));
        }
        return items;
    }

    @NonNull
    private static StackTraceItem getStackTraceItem(@Nullable ReadableMap item) {
        StackTraceItem.Builder builder = new StackTraceItem.Builder();
        if (item == null) {
            return builder.build();
        }
        if (item.hasKey("fileName")) {
            builder.withFileName(item.getString("fileName"));
        }
        if (item.hasKey("className")) {
            builder.withClassName(item.getString("className"));
        }
        if (item.hasKey("methodName")) {
            builder.withMethodName(item.getString("methodName"));
        }
        if (item.hasKey("line")) {
            builder.withLine(parseInt(item.getString("line")));
        }
        if (item.hasKey("column")) {
           builder.withColumn(parseInt(item.getString("column")));
        }
        return builder.build();
    }

    @Nullable
    private static Integer parseInt(@Nullable String string) {
        if (string == null) {
            return null;
        }
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            Log.w(TAG, "uncorrected number in the stacktrace line or column: " + string);
            return null;
        }
    }
}

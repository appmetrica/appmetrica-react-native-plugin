package io.appmetrica.analytics.reactnative;

import androidx.annotation.NonNull;

import com.facebook.react.BaseReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;

import java.util.HashMap;
import java.util.Map;

public class AppMetricaPackage extends BaseReactPackage {

    @Override
    public NativeModule getModule(String name, @NonNull ReactApplicationContext reactContext) {
        if (name.equals(AppMetricaModule.NAME)) {
            return new AppMetricaModule(reactContext);
        } else if (name.equals(ReporterModule.NAME)) {
            return new ReporterModule(reactContext);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return new ReactModuleInfoProvider() {
            @NonNull
            @Override
            public Map<String, ReactModuleInfo> getReactModuleInfos() {
                Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
                moduleInfos.put(AppMetricaModule.NAME, new ReactModuleInfo(AppMetricaModule.NAME, AppMetricaModule.NAME, false, false, false, true));
                moduleInfos.put(ReporterModule.NAME, new ReactModuleInfo(ReporterModule.NAME, ReporterModule.NAME, false, false, false, true));
                return moduleInfos;
            }
        };
    }
}

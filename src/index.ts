import { Linking } from 'react-native';
import AppMetricaNative from './specs/NativeAppMetrica';
import type { ECommerceEvent } from './ecommerce';
import type { AdRevenue, Revenue } from './revenue';
import type { UserProfile } from './userProfile';
import type { ExternalAttribution } from './externalAttribution';
import { normalizeAdRevenue } from './utils';
import { AppMetricaError } from './error';
import { Reporter } from './reporter';
import type { IReporter, ReporterConfig } from './reporter';
import type {
  DeferredDeeplinkListener,
  DeferredDeeplinkParametersListener,
  DeferredDeeplinkError
} from './deferredDeeplink';

import type {
  AppMetricaConfig,
  StartupParamsReason,
  StartupParamsCallback,
  Location,
  StartupParamsItem
} from './types';
import { StartupParams } from './types';

var activated = false;

function appOpenTracking() {
  const getUrlAsync = async () => {
    const initialUrl = await Linking.getInitialURL();
    if (initialUrl != null) {
      AppMetricaNative.reportAppOpen(initialUrl);
    }
  };
  const callback = (event: { url: string }) => {
    AppMetricaNative.reportAppOpen(event.url);
  };
  getUrlAsync();
  Linking.addEventListener('url', callback);
}

export * from './ecommerce';
export * from './revenue';
export * from './userProfile';
export * from './externalAttribution';
export type { IReporter, ReporterConfig } from './reporter';
export * from './deferredDeeplink';
export * from './types';

export default class AppMetrica {

  private static reporters: Map<string, Reporter> = new Map();

  static activate(config: AppMetricaConfig) {
    if (!activated) {
      AppMetricaNative.activate(config);
      if (config.appOpenTrackingEnabled !== false) {
        appOpenTracking();
      }
      activated = true;
    }
  }

  // Android only
  static getLibraryApiLevel(): number {
    return AppMetricaNative.getLibraryApiLevel();
  }

  static getLibraryVersion(): string {
    return AppMetricaNative.getLibraryVersion();
  }

  static pauseSession() {
    AppMetricaNative.pauseSession();
  }

  static reportAppOpen(deeplink?: string) {
    AppMetricaNative.reportAppOpen(deeplink);
  }

  static reportError(
    identifier: string,
    message?: string,
    _reason?: Error | Object
  ) {
    AppMetricaNative.reportError(
      identifier,
      message,
      _reason instanceof Error ? AppMetricaError.withError(_reason) : AppMetricaError.withObject(_reason)
    );
  }

  static reportUnhandledException(error: Error) {
    AppMetricaNative.reportUnhandledException(AppMetricaError.withError(error));
  }

  static reportErrorWithoutIdentifier(message: string | undefined, error: Error) {
    AppMetricaNative.reportErrorWithoutIdentifier(message, AppMetricaError.withError(error));
  }

  static reportEvent(eventName: string, attributes?: Record<string, any>) {
    AppMetricaNative.reportEvent(eventName, attributes);
  }

  static requestStartupParams(
    listener: StartupParamsCallback,
    identifiers: Array<string>
  ) {
    const adapter = (params?: Object, reason?: Object) => {
      const startupParams = params ? new StartupParams(params as Record<string, StartupParamsItem>) : undefined;
      listener(startupParams, reason as StartupParamsReason);
    };
    AppMetricaNative.requestStartupParams(adapter, identifiers);
  }

  static resumeSession() {
    AppMetricaNative.resumeSession();
  }

  static sendEventsBuffer() {
    AppMetricaNative.sendEventsBuffer();
  }

  static setLocation(location?: Location) {
    AppMetricaNative.setLocation(location);
  }

  static setLocationTracking(enabled: boolean) {
    AppMetricaNative.setLocationTracking(enabled);
  }

  static setDataSendingEnabled(enabled: boolean) {
    AppMetricaNative.setDataSendingEnabled(enabled);
  }

  static setUserProfileID(userProfileID?: string) {
    AppMetricaNative.setUserProfileID(userProfileID);
  }

  static reportECommerce(event: ECommerceEvent) {
    AppMetricaNative.reportECommerce(event);
  }

  static reportRevenue(revenue: Revenue) {
    AppMetricaNative.reportRevenue(revenue);
  }

  static reportAdRevenue(adRevenue: AdRevenue) {
    AppMetricaNative.reportAdRevenue(normalizeAdRevenue(adRevenue));
  }

  static reportUserProfile(userProfile: UserProfile) {
    AppMetricaNative.reportUserProfile(userProfile);
  }

  static putErrorEnvironmentValue(key: string, value?: string) {
    AppMetricaNative.putErrorEnvironmentValue(key, value);
  }

  static reportExternalAttribution(attribution: ExternalAttribution) {
    AppMetricaNative.reportExternalAttribution(attribution);
  }

  static putAppEnvironmentValue(key: string, value?: string) {
    AppMetricaNative.putAppEnvironmentValue(key, value);
  }

  static clearAppEnvironment() {
    AppMetricaNative.clearAppEnvironment();
  }

  static getReporter(apiKey: string): IReporter {
    if (AppMetrica.reporters.has(apiKey)) {
      return AppMetrica.reporters.get(apiKey)!;
    } else {
      AppMetricaNative.touchReporter(apiKey);
      const reporter = new Reporter(apiKey);
      AppMetrica.reporters.set(apiKey, reporter);
      return reporter;
    }
  }

  static activateReporter(config: ReporterConfig) {
    AppMetricaNative.activateReporter(config);
  }

  static getDeviceId(): string | null {
    return AppMetricaNative.getDeviceId();
  }

  static getUuid(): string | null {
    return AppMetricaNative.getUuid();
  }

  static requestDeferredDeeplink(listener: DeferredDeeplinkListener) {
    const adaptedOnFailure = (error: string, referrer?: string) => {
      listener.onFailure(error as DeferredDeeplinkError, referrer);
    };

    AppMetricaNative.requestDeferredDeeplink(
      adaptedOnFailure,
      listener.onSuccess
    );
  }

  static requestDeferredDeeplinkParameters(
    listener: DeferredDeeplinkParametersListener
  ) {
    const adaptedOnFailure = (error: string, referrer?: string) => {
      listener.onFailure(error as DeferredDeeplinkError, referrer);
    };
    const adaptedOnSuccess = (parameters: Object) => {
      listener.onSuccess(parameters as Record<string, string>);
    };
    AppMetricaNative.requestDeferredDeeplinkParameters(
      adaptedOnFailure,
      adaptedOnSuccess
    );
  }
}

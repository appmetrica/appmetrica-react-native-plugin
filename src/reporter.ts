import { NativeModules, Platform } from 'react-native';
import type { UserProfile } from './userProfile';
import type { AdRevenue, Revenue } from './revenue';
import type { ECommerceEvent } from './ecommerce';
import { AppMetricaError } from './error';

const LINKING_ERROR =
  `The package '@appmetrica/react-native-analytics' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ReporterNativeModule = NativeModules.AppMetricaReporter
  ? NativeModules.AppMetricaReporter
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export interface IReporter {
  reportError(identifier: string, message?: string, _reason?: Error | Object): void;
  reportErrorWithoutIdentifier(message: string | undefined, error: Error): void;
  reportUnhandledException(error: Error): void;
  reportEvent(eventName: string, attributes?: Record<string, any>): void;
  pauseSession(): void;
  resumeSession(): void;
  sendEventsBuffer(): void;
  clearAppEnvironment(): void;
  putAppEnvironmentValue(key: string, value?: string): void;
  setUserProfileID(userProfileID?: string): void;
  setDataSendingEnabled(enabled: boolean): void;
  reportUserProfile(userProfile: UserProfile): void;
  reportAdRevenue(adRevenue: AdRevenue): void;
  reportECommerce(event: ECommerceEvent): void;
  reportRevenue(revenue: Revenue): void;
}

export class Reporter implements IReporter {

  private apiKey: string;

  constructor(apiKey: string) {
    this.apiKey = apiKey;
  }

  reportError(identifier: string, message?: string, _reason?: Error | Object) {
    ReporterNativeModule.reportError(
      this.apiKey,
      identifier,
      message,
      _reason instanceof Error ? AppMetricaError.withError(_reason) : AppMetricaError.withObject(_reason)
    );
  }

  reportErrorWithoutIdentifier(message: string | undefined, error: Error) {
    ReporterNativeModule.reportErrorWithoutIdentifier(this.apiKey, message, AppMetricaError.withError(error));
  }

  reportUnhandledException(error: Error) {
    ReporterNativeModule.reportUnhandledException(this.apiKey, AppMetricaError.withError(error));
  }

  reportEvent(eventName: string, attributes?: Record<string, any>) {
    ReporterNativeModule.reportEvent(this.apiKey, eventName, attributes);
  }

  pauseSession() {
    ReporterNativeModule.pauseSession(this.apiKey);
  }

  resumeSession() {
    ReporterNativeModule.resumeSession(this.apiKey);
  }

  sendEventsBuffer() {
    ReporterNativeModule.sendEventsBuffer(this.apiKey);
  }

  clearAppEnvironment() {
    ReporterNativeModule.clearAppEnvironment(this.apiKey);
  }

  putAppEnvironmentValue(key: string, value?: string) {
    ReporterNativeModule.putAppEnvironmentValue(this.apiKey, key, value);
  }

  setUserProfileID(userProfileID: string) {
    ReporterNativeModule.setUserProfileID(this.apiKey, userProfileID);
  }

  setDataSendingEnabled(enabled: boolean) {
    ReporterNativeModule.setDataSendingEnabled(this.apiKey, enabled);
  }

  reportUserProfile(profile: UserProfile) {
    ReporterNativeModule.reportUserProfile(this.apiKey, profile);
  }

  reportAdRevenue(adRevenue: AdRevenue) {
    ReporterNativeModule.reportAdRevenue(this.apiKey, adRevenue);
  }

  reportECommerce(ecommerce: ECommerceEvent) {
    ReporterNativeModule.reportECommerce(this.apiKey, ecommerce);
  }

  reportRevenue(revenue: Revenue) {
    ReporterNativeModule.reportRevenue(this.apiKey, revenue)
  }
}

export type ReporterConfig = {
  apiKey: string;
  logs?: boolean;
  maxReportsInDatabaseCount?: number;
  sessionTimeout?: number;
  dataSendingEnabled?: boolean;
  appEnvironment?: Record<string, string | undefined>;
  dispatchPeriodSeconds?: number;
  userProfileID?: string;
  maxReportsCount?: number;
}

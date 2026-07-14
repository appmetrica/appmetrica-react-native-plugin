import type { AdRevenue } from '../public/adRevenue';
import type { ECommerceEvent } from '../public/ecommerce';
import type { IReporter } from '../public/reporter';
import type { Revenue } from '../public/revenue';
import type { UserProfile } from '../public/userProfile';
import ReporterNativeModule from '../specs/NativeReporter';
import { AppMetricaError } from './appMetricaError';

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
      _reason instanceof Error
        ? AppMetricaError.withError(_reason)
        : AppMetricaError.withObject(_reason)
    );
  }

  reportErrorWithoutIdentifier(message: string | undefined, error: Error) {
    ReporterNativeModule.reportErrorWithoutIdentifier(
      this.apiKey,
      message,
      AppMetricaError.withError(error)
    );
  }

  reportUnhandledException(error: Error) {
    ReporterNativeModule.reportUnhandledException(
      this.apiKey,
      AppMetricaError.withError(error)
    );
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
    ReporterNativeModule.reportRevenue(this.apiKey, revenue);
  }
}

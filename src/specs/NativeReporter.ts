import type { TurboModule } from 'react-native/Libraries/TurboModule/RCTExport';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  pauseSession(apiKey: string): void;
  resumeSession(apiKey: string): void;
  sendEventsBuffer(apiKey: string): void;
  setDataSendingEnabled(apiKey: string, enabled: boolean): void;
  setUserProfileID(apiKey: string, userProfileID?: string): void;
  reportEvent(apiKey: string, eventName: string, attributes?: Object): void;
  reportECommerce(apiKey: string, event: Object): void;
  reportRevenue(apiKey: string, revenue: Object): void;
  reportAdRevenue(apiKey: string, adRevenue: Object): void;
  reportUserProfile(apiKey: string, userProfile: Object): void;
  reportError(apiKey: string, identifier: string, message?: string, error?: Object): void;
  reportErrorWithoutIdentifier(apiKey: string, message: string | undefined, error: Object): void;
  reportUnhandledException(apiKey: string, error: Object): void;
  putAppEnvironmentValue(apiKey: string, key: string, value?: string): void;
  clearAppEnvironment(apiKey: string): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('AppMetricaReporter');

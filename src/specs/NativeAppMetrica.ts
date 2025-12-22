import type { TurboModule } from 'react-native/Libraries/TurboModule/RCTExport';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  activate(config: Object): void;
  pauseSession(): void;
  resumeSession(): void;
  sendEventsBuffer(): void;
  setDataSendingEnabled(enabled: boolean): void;
  setLocationTracking(enabled: boolean): void;
  setLocation(location?: Object): void;
  setUserProfileID(userProfileID?: string): void;
  reportAppOpen(deeplink?: string): void;
  reportEvent(eventName: string, attributes?: Object): void;
  reportECommerce(event: Object): void;
  reportRevenue(revenue: Object): void;
  reportAdRevenue(adRevenue: Object): void;
  reportUserProfile(userProfile: Object): void;
  reportExternalAttribution(attribution: Object): void;
  reportError(identifier: string, message?: string, error?: Object): void;
  reportErrorWithoutIdentifier(message: string | undefined, error: Object): void;
  reportUnhandledException(error: Object): void;
  putAppEnvironmentValue(key: string, value?: string): void;
  putErrorEnvironmentValue(key: string, value?: string): void;
  clearAppEnvironment(): void;
  activateReporter(config: Object): void;
  touchReporter(apiKey: string): void;
  requestStartupParams(
    listener: (params: Object, reason: Object) => void,
    identifiers: Array<string>
  ): void;
  requestDeferredDeeplink(
    onFailure: (error: string, referrer?: string) => void,
    onSuccess: (deeplink: string) => void
  ): void;
  requestDeferredDeeplinkParameters(
    onFailure: (error: string, referrer?: string) => void,
    onSuccess: (parameters: Object) => void
  ): void;
  getDeviceId(): string | null;
  getUuid(): string | null;
  getLibraryVersion(): string;
  getLibraryApiLevel(): number;

  readonly getConstants: () => {
    DEVICE_ID_HASH_KEY: string;
    DEVICE_ID_KEY: string;
    UUID_KEY: string;
  };
}

export default TurboModuleRegistry.getEnforcing<Spec>('AppMetrica');

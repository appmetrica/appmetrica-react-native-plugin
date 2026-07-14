import type { AdRevenue } from './adRevenue';
import type { ECommerceEvent } from './ecommerce';
import type { Revenue } from './revenue';
import type { UserProfile } from './userProfile';

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
};

export interface IReporter {
  reportError(
    identifier: string,
    message?: string,
    _reason?: Error | Object
  ): void;
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

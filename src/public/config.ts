export type AppMetricaConfig = {
  apiKey: string;
  appVersion?: string;
  crashReporting?: boolean;
  firstActivationAsUpdate?: boolean;
  location?: Location;
  locationTracking?: boolean;
  logs?: boolean;
  sessionTimeout?: number;
  statisticsSending?: boolean;
  preloadInfo?: PreloadInfo;
  maxReportsInDatabaseCount?: number;
  nativeCrashReporting?: boolean;
  activationAsSessionStart?: boolean;
  sessionsAutoTracking?: boolean;
  appOpenTrackingEnabled?: boolean;
  userProfileID?: string;
  errorEnvironment?: Record<string, string | undefined>;
  appEnvironment?: Record<string, string | undefined>;
  maxReportsCount?: number;
  dispatchPeriodSeconds?: number;
  appBuildNumber?: number;
  revenueAutoTrackingEnabled?: boolean;
  deviceType?: string;
  advIdentifiersTracking?: boolean;
};

export type PreloadInfo = {
  trackingId: string;
  additionalInfo?: Record<string, string>;
};

export type Location = {
  latitude: number;
  longitude: number;
  altitude?: number;
  accuracy?: number;
  course?: number;
  speed?: number;
  timestamp?: number;
};

export enum PredefinedDeviceTypes {
  PHONE = 'phone',
  TABLET = 'tablet',
  TV = 'tv',
  CAR = 'car',
}

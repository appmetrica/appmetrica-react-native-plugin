import AppMetricaNative from './specs/NativeAppMetrica';

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

export type StartupParamsReason = 'UNKNOWN' | 'NETWORK' | 'INVALID_RESPONSE';

export class StartupParams {

  private static constants = AppMetricaNative.getConstants()
  static readonly DEVICE_ID_HASH_KEY = this.constants.DEVICE_ID_HASH_KEY;
  static readonly DEVICE_ID_KEY = this.constants.DEVICE_ID_KEY;
  static readonly UUID_KEY = this.constants.UUID_KEY;

  readonly deviceIdHash?: string;
  readonly deviceId?: string;
  readonly uuid?: string;

  constructor(
    readonly params?: Record<string, StartupParamsItem>
  ) {
      if (params) {
        this.deviceIdHash = this.parameterForKey(StartupParams.DEVICE_ID_HASH_KEY);
        this.deviceId = this.parameterForKey(StartupParams.DEVICE_ID_KEY);
        this.uuid = this.parameterForKey(StartupParams.UUID_KEY);
      }
    }

  parameterForKey(key: string): string | undefined {
    return this.params?.[key]?.id;
  }
}

export type StartupParamsCallback = (
  params?: StartupParams,
  reason?: StartupParamsReason
) => void;

export type ReporterConfig = {
  apiKey: string;
  sessionTimeout?: number;
  statisticsSending?: boolean;
  maxReportsCount?: number;
  dispatchPeriodSeconds?: number;
  logs?: boolean;
};

export type StartupParamsItem = {
  id?: string;
  errorDetails?: string;
  status: StartupParamsItemStatus;
}

export type StartupParamsItemStatus =
  | 'OK'
  | 'FEATURE_DISABLED'
  | 'INVALID_VALUE_FROM_PROVIDER'
  | 'NETWORK_ERROR'
  | 'PROVIDER_UNAVAILABLE'
  | 'UNKNOWN_ERROR';

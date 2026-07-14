import AppMetricaNative from '../specs/NativeAppMetrica';

export type StartupParamsReason = 'UNKNOWN' | 'NETWORK' | 'INVALID_RESPONSE';

export class StartupParams {
  private static constants = AppMetricaNative.getConstants();
  static readonly DEVICE_ID_HASH_KEY = this.constants.DEVICE_ID_HASH_KEY;
  static readonly DEVICE_ID_KEY = this.constants.DEVICE_ID_KEY;
  static readonly UUID_KEY = this.constants.UUID_KEY;

  readonly deviceIdHash?: string;
  readonly deviceId?: string;
  readonly uuid?: string;

  constructor(readonly params?: Record<string, StartupParamsItem>) {
    if (params) {
      this.deviceIdHash = this.parameterForKey(
        StartupParams.DEVICE_ID_HASH_KEY
      );
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

export type StartupParamsItem = {
  id?: string;
  errorDetails?: string;
  status: StartupParamsItemStatus;
};

export type StartupParamsItemStatus =
  | 'OK'
  | 'FEATURE_DISABLED'
  | 'INVALID_VALUE_FROM_PROVIDER'
  | 'NETWORK_ERROR'
  | 'PROVIDER_UNAVAILABLE'
  | 'UNKNOWN_ERROR';

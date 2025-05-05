export type DeferredDeeplinkError = 'NO_REFERRER' | 'NOT_A_FIRST_LAUNCH' | 'PARSE_ERROR' | 'UNKNOWN';

export interface DeferredDeeplinkListener {
  onSuccess: (deeplink: string) => void;
  onFailure: (error: DeferredDeeplinkError, referrer?: string) => void;
}

export interface DeferredDeeplinkParametersListener {
  onSuccess: (parameters: Record<string, string>) => void;
  onFailure: (error: DeferredDeeplinkError, referrer?: string) => void;
}

export type AdRevenue = {
  price: number | string;
  currency: string;
  payload?: Map<string, string> | Record<string, string>;
  adNetwork?: string;
  adPlacementID?: string;
  adPlacementName?: string;
  adType?: AdType;
  adUnitID?: string;
  adUnitName?: string;
  precision?: string;
};

export enum AdType {
  NATIVE = 'native',
  BANNER = 'banner',
  MREC = 'mrec',
  INTERSTITIAL = 'interstitial',
  REWARDED = 'rewarded',
  APP_OPEN = 'app_open',
  OTHER = 'other',
}

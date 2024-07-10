export type Revenue = {
  price: number;
  currency: string;
  productID?: string;
  quantity?: number;
  payload?: string;
  receipt?: Receipt;
};

export type Receipt = {
  transactionID?: string;
  receiptData?: string;
  signature?: string;
};

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
  OTHER = 'other',
}

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

import {
  normalizeECommerceCartItem,
  normalizeECommerceOrder,
  normalizeECommerceProduct,
  normalizeECommerceReferrer,
  normalizeECommerceScreen,
} from './utils';

export type ECommerceScreen = {
  name: string;
  searchQuery?: string;
  payload?: Map<string, string> | Record<string, string>;
  categoriesPath?: Array<string>;
};

export type ECommerceAmount = {
  amount: number | string;
  unit: string;
};

export type ECommercePrice = {
  amount: ECommerceAmount;
  internalComponents?: Array<ECommerceAmount>;
};

export type ECommerceProduct = {
  sku: string;
  name?: string;
  actualPrice?: ECommercePrice;
  originalPrice?: ECommercePrice;
  promocodes?: Array<string>;
  categoriesPath?: Array<string>;
  payload?: Map<string, string> | Record<string, string>;
};

export type ECommerceReferrer = {
  type?: string;
  identifier?: string;
  screen?: ECommerceScreen;
};

export type ECommerceCartItem = {
  product: ECommerceProduct;
  price: ECommercePrice;
  quantity: number | string;
  referrer?: ECommerceReferrer;
};

export type ECommerceOrder = {
  orderId: string;
  products: Array<ECommerceCartItem>;
  payload?: Map<string, string> | Record<string, string>;
};

export type ECommerceEventType =
  | 'showScreenEvent'
  | 'showProductCardEvent'
  | 'showProductDetailsEvent'
  | 'addCartItemEvent'
  | 'removeCartItemEvent'
  | 'beginCheckoutEvent'
  | 'purchaseEvent';

export interface ECommerceEvent {
  ecommerceEvent: ECommerceEventType;
  ecommerceScreen?: ECommerceScreen;
  product?: ECommerceProduct;
  referrer?: ECommerceReferrer;
  cartItem?: ECommerceCartItem;
  order?: ECommerceOrder;
}

export class ECommerce {
  static showScreenEvent(screen: ECommerceScreen): ECommerceEvent {
    return {
      ecommerceEvent: 'showScreenEvent',
      ecommerceScreen: normalizeECommerceScreen(screen),
    };
  }

  static showProductCardEvent(
    product: ECommerceProduct,
    screen: ECommerceScreen
  ): ECommerceEvent {
    return {
      ecommerceEvent: 'showProductCardEvent',
      ecommerceScreen: normalizeECommerceScreen(screen),
      product: normalizeECommerceProduct(product),
    };
  }

  static showProductDetailsEvent(
    product: ECommerceProduct,
    referrer?: ECommerceReferrer
  ): ECommerceEvent {
    return {
      ecommerceEvent: 'showProductDetailsEvent',
      product: normalizeECommerceProduct(product),
      referrer: normalizeECommerceReferrer(referrer),
    };
  }

  static addCartItemEvent(item: ECommerceCartItem): ECommerceEvent {
    return {
      ecommerceEvent: 'addCartItemEvent',
      cartItem: normalizeECommerceCartItem(item),
    };
  }

  static removeCartItemEvent(item: ECommerceCartItem): ECommerceEvent {
    return {
      ecommerceEvent: 'removeCartItemEvent',
      cartItem: normalizeECommerceCartItem(item),
    };
  }

  static beginCheckoutEvent(order: ECommerceOrder): ECommerceEvent {
    return {
      ecommerceEvent: 'beginCheckoutEvent',
      order: normalizeECommerceOrder(order),
    };
  }

  static purchaseEvent(order: ECommerceOrder): ECommerceEvent {
    return {
      ecommerceEvent: 'purchaseEvent',
      order: normalizeECommerceOrder(order),
    };
  }
}

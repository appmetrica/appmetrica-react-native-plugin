import type {
  ECommerceCartItem,
  ECommerceOrder,
  ECommerceProduct,
  ECommerceReferrer,
  ECommerceScreen,
} from './ecommerce';
import type { AdRevenue } from './revenue';

function convertMap(
  map?: Map<string, string>
): Record<string, string> | undefined {
  return map !== undefined ? Object.fromEntries(map) : undefined;
}

export function normalizeECommerceOrder(order: ECommerceOrder): ECommerceOrder {
  const newOrder = { ...order } as ECommerceOrder;
  if (order.payload instanceof Map) {
    newOrder.payload = convertMap(order.payload);
  }
  newOrder.products = order.products.map(normalizeECommerceCartItem)
  return newOrder;
}

export function normalizeECommerceCartItem(
  item: ECommerceCartItem
): ECommerceCartItem {
  const newItem = { ...item } as ECommerceCartItem;
  newItem.product = normalizeECommerceProduct(item.product);
  newItem.referrer = normalizeECommerceReferrer(item.referrer);
  return newItem;
}

export function normalizeECommerceProduct(
  product: ECommerceProduct
): ECommerceProduct {
  const newProduct = { ...product } as ECommerceProduct;
  if (product.payload instanceof Map) {
    newProduct.payload = convertMap(product.payload);
  }
  return newProduct;
}

export function normalizeECommerceReferrer(
  referrer?: ECommerceReferrer
): ECommerceReferrer | undefined {
  if (referrer === undefined) {
    return undefined;
  }
  const newReferrer = { ...referrer } as ECommerceReferrer;
  if (referrer.screen != undefined) {
    newReferrer.screen = normalizeECommerceScreen(referrer.screen);
  }
  return newReferrer;
}

export function normalizeECommerceScreen(screen: ECommerceScreen): ECommerceScreen {
  const newScreen = { ...screen } as ECommerceScreen;
  if (screen.payload instanceof Map) {
    newScreen.payload = convertMap(screen.payload);
  }
  return newScreen;
}

export function normalizeAdRevenue(adRevenue: AdRevenue): AdRevenue {
  const newAdRevenue = { ...adRevenue } as AdRevenue;
  if (adRevenue.payload instanceof Map) {
    newAdRevenue.payload = convertMap(adRevenue.payload);
  }
  return newAdRevenue;
}

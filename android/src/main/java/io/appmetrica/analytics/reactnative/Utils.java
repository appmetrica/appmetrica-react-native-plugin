package io.appmetrica.analytics.reactnative;

import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import io.appmetrica.analytics.AdRevenue;
import io.appmetrica.analytics.AdType;
import io.appmetrica.analytics.AppMetricaConfig;
import io.appmetrica.analytics.PreloadInfo;
import io.appmetrica.analytics.ReporterConfig;
import io.appmetrica.analytics.Revenue;
import io.appmetrica.analytics.profile.UserProfile;
import io.appmetrica.analytics.ecommerce.ECommerceAmount;
import io.appmetrica.analytics.ecommerce.ECommerceCartItem;
import io.appmetrica.analytics.ecommerce.ECommerceEvent;
import io.appmetrica.analytics.ecommerce.ECommerceOrder;
import io.appmetrica.analytics.ecommerce.ECommercePrice;
import io.appmetrica.analytics.ecommerce.ECommerceProduct;
import io.appmetrica.analytics.ecommerce.ECommerceReferrer;
import io.appmetrica.analytics.ecommerce.ECommerceScreen;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

abstract class Utils {

    @NonNull
    static AppMetricaConfig toAppMetricaConfig(@NonNull ReadableMap configMap) {
        AppMetricaConfig.Builder builder = AppMetricaConfig.newConfigBuilder(configMap.getString("apiKey"));

        if (configMap.hasKey("appVersion")) {
            builder.withAppVersion(configMap.getString("appVersion"));
        }
        if (configMap.hasKey("crashReporting")) {
            builder.withCrashReporting(configMap.getBoolean("crashReporting"));
        }
        if (configMap.hasKey("firstActivationAsUpdate")) {
            builder.handleFirstActivationAsUpdate(configMap.getBoolean("firstActivationAsUpdate"));
        }
        if (configMap.hasKey("location")) {
            builder.withLocation(toLocation(configMap.getMap("location")));
        }
        if (configMap.hasKey("locationTracking")) {
            builder.withLocationTracking(configMap.getBoolean("locationTracking"));
        }
        if (configMap.hasKey("logs") && configMap.getBoolean("logs")) {
            builder.withLogs();
        }
        if (configMap.hasKey("maxReportsInDatabaseCount")) {
            builder.withMaxReportsInDatabaseCount(configMap.getInt("maxReportsInDatabaseCount"));
        }
        if (configMap.hasKey("nativeCrashReporting")) {
            builder.withNativeCrashReporting(configMap.getBoolean("nativeCrashReporting"));
        }
        if (configMap.hasKey("preloadInfo")) {
            builder.withPreloadInfo(toPreloadInfo(configMap.getMap("preloadInfo")));
        }
        if (configMap.hasKey("sessionTimeout")) {
            builder.withSessionTimeout(configMap.getInt("sessionTimeout"));
        }
        if (configMap.hasKey("statisticsSending")) {
            builder.withDataSendingEnabled(configMap.getBoolean("statisticsSending"));
        }
        if (configMap.hasKey("sessionsAutoTracking")) {
            builder.withSessionsAutoTrackingEnabled(configMap.getBoolean("sessionsAutoTracking"));
        }
        if (configMap.hasKey("userProfileID")) {
            builder.withUserProfileID(configMap.getString("userProfileID"));
        }
        if (configMap.hasKey("errorEnvironment")) {
            ReadableMap errorEnvironmentMap = configMap.getMap("errorEnvironment");
            if (errorEnvironmentMap != null) {
                for (Map.Entry<String, Object> entry : errorEnvironmentMap.toHashMap().entrySet()) {
                    Object value = entry.getValue();
                    builder.withErrorEnvironmentValue(entry.getKey(), value == null ? null : value.toString());
                }
            }
        }
        if (configMap.hasKey("appEnvironment")) {
            ReadableMap appEnvironmentMap = configMap.getMap("appEnvironment");
            if (appEnvironmentMap != null) {
                for (Map.Entry<String, Object> entry : appEnvironmentMap.toHashMap().entrySet()) {
                    Object value = entry.getValue();
                    builder.withAppEnvironmentValue(entry.getKey(), value == null ? null : value.toString());
                }
            }
        }
        if (configMap.hasKey("maxReportsCount")) {
            builder.withMaxReportsCount(configMap.getInt("maxReportsCount"));
        }
        if (configMap.hasKey("dispatchPeriodSeconds")) {
            builder.withDispatchPeriodSeconds(configMap.getInt("dispatchPeriodSeconds"));
        }

        return builder.build();
    }

    @Nullable
    static Location toLocation(@Nullable ReadableMap locationMap) {
        if (locationMap == null) {
            return null;
        }

        Location location = new Location("Custom");

        if (locationMap.hasKey("latitude")) {
            location.setLatitude(locationMap.getDouble("latitude"));
        }
        if (locationMap.hasKey("longitude")) {
            location.setLongitude(locationMap.getDouble("longitude"));
        }
        if (locationMap.hasKey("altitude")) {
            location.setAltitude(locationMap.getDouble("altitude"));
        }
        if (locationMap.hasKey("accuracy")) {
            location.setAccuracy((float) locationMap.getDouble("accuracy"));
        }
        if (locationMap.hasKey("course")) {
            location.setBearing((float) locationMap.getDouble("course"));
        }
        if (locationMap.hasKey("speed")) {
            location.setSpeed((float) locationMap.getDouble("speed"));
        }
        if (locationMap.hasKey("timestamp")) {
            location.setTime((long) locationMap.getDouble("timestamp"));
        }

        return location;
    }

    @Nullable
    private static PreloadInfo toPreloadInfo(@Nullable ReadableMap preloadInfoMap) {
        if (preloadInfoMap == null) {
            return null;
        }

        PreloadInfo.Builder builder = PreloadInfo.newBuilder(preloadInfoMap.getString("trackingId"));

        if (preloadInfoMap.hasKey("additionalInfo")) {
            ReadableMap additionalInfo = preloadInfoMap.getMap("additionalInfo");
            if (additionalInfo != null) {
                for (Map.Entry<String, Object> entry : additionalInfo.toHashMap().entrySet()) {
                    Object value = entry.getValue();
                    builder.setAdditionalParams(entry.getKey(), value == null ? null : value.toString());
                }
            }
        }

        return builder.build();
    }

    @NonNull
    static ECommerceScreen toECommerceScreen(@NonNull ReadableMap ecommerceEventMap) {
        ECommerceScreen screen = new ECommerceScreen();
        if (ecommerceEventMap.hasKey("name")) {
            screen.setName(ecommerceEventMap.getString("name"));
        }
        if (ecommerceEventMap.hasKey("searchQuery")) {
            screen.setSearchQuery(ecommerceEventMap.getString("searchQuery"));
        }
        if (ecommerceEventMap.hasKey("payload")) {
            ReadableMap payloadMap = ecommerceEventMap.getMap("payload");
            screen.setPayload(toMapOfStrings(payloadMap));
        }
        if (ecommerceEventMap.hasKey("categoriesPath")) {
            ReadableArray categoriesPathArray = ecommerceEventMap.getArray("categoriesPath");
            screen.setCategoriesPath(toListOfStrings(categoriesPathArray));
        }
        return screen;
    }

    @Nullable
    static ECommerceAmount toEcommerceAmount(@NonNull ReadableMap amountMap) {
        ReadableType amountType = amountMap.getType("amount");
        String unit = amountMap.getString("unit");
        if (unit != null) {
            if (amountType == ReadableType.Number) {
                return new ECommerceAmount(amountMap.getDouble("amount"), unit);
            } else {
                String amountString = amountMap.getString("amount");
                return new ECommerceAmount(new BigDecimal(amountString), unit);
            }
        } else {
            Log.w(AppMetricaModule.TAG, "ECommerceAmount unit is null");
            return null;
        }
    }

    @Nullable
    static ECommercePrice toECommercePrice(@Nullable ReadableMap priceMap) {
        if (priceMap == null) {
            return null;
        }
        ReadableMap amountMap = Objects.requireNonNull(priceMap.getMap("amount"));
        ECommerceAmount amount = toEcommerceAmount(amountMap);
        if (amount == null) {
            return null;
        }
        ECommercePrice price = new ECommercePrice(amount);
        if (priceMap.hasKey("internalComponents")) {
            ReadableArray list = priceMap.getArray("internalComponents");
            if (list != null) {
                List<ECommerceAmount> newlist = new ArrayList<ECommerceAmount>(list.size());
                for (int i = 0; i < list.size(); i++) {
                    ECommerceAmount component = toEcommerceAmount(list.getMap(i));
                    newlist.add(component);
                }
                price.setInternalComponents(newlist);
            }
        }
        return price;
    }

    @NonNull
    static ECommerceProduct toECommerceProduct(@NonNull ReadableMap productMap) {
        ECommerceProduct product = new ECommerceProduct(Objects.requireNonNull(productMap.getString("sku")));

        if (productMap.hasKey("name")) {
            product.setName(productMap.getString("name"));
        }
        if (productMap.hasKey("actualPrice")) {
            ReadableMap priceMap = productMap.getMap("actualPrice");
            product.setActualPrice(toECommercePrice(priceMap));
        }
        if (productMap.hasKey("originalPrice")) {
            ReadableMap priceMap = productMap.getMap("originalPrice");
            product.setOriginalPrice(toECommercePrice(priceMap));
        }
        if (productMap.hasKey("promocodes")) {
            ReadableArray promocodesArray = productMap.getArray("promocodes");
            product.setPromocodes(toListOfStrings(promocodesArray));
        }
        if (productMap.hasKey("categoriesPath")) {
            ReadableArray categoriesArray = productMap.getArray("categoriesPath");
            product.setCategoriesPath(toListOfStrings(categoriesArray));
        }
        if (productMap.hasKey("payload")) {
            ReadableMap payloadMap = productMap.getMap("payload");
            product.setPayload(toMapOfStrings(payloadMap));
        }
        return product;
    }

    @Nullable
    static ECommerceReferrer toECommerceReferrer(@Nullable ReadableMap referrerMap) {
        if (referrerMap == null) return null;
        ECommerceReferrer referrer = new ECommerceReferrer();
        if (referrerMap.hasKey("type")) {
            referrer.setType(referrerMap.getString("type"));
        }
        if (referrerMap.hasKey("identifier")) {
            referrer.setIdentifier(referrerMap.getString("identifier"));
        }
        if (referrerMap.hasKey("screen")) {
            ReadableMap screenMap = referrerMap.getMap("screen");
            if (screenMap != null) {
                referrer.setScreen(toECommerceScreen(screenMap));
            }
        }
        return referrer;
    }

    @Nullable
    static ECommerceCartItem toECommerceCartItem(@Nullable ReadableMap cartItemMap) {
        if (cartItemMap == null) {
            return null;
        }
        ECommerceProduct product = toECommerceProduct(Objects.requireNonNull(cartItemMap.getMap("product")));
        ECommercePrice price = toECommercePrice(Objects.requireNonNull(cartItemMap.getMap("price")));
        if (price == null) {
            return null;
        }
        ReadableType quantityType = cartItemMap.getType("quantity");
        if (quantityType == ReadableType.Number) {
            double quantity = cartItemMap.getDouble("quantity");
            ECommerceCartItem item = new ECommerceCartItem(product, price, quantity);
            if (cartItemMap.hasKey("referrer")) {
                ReadableMap referrer = cartItemMap.getMap("referrer");
                item.setReferrer(toECommerceReferrer(referrer));
            }
            return item;
        } else {
            BigDecimal quantity = new BigDecimal(cartItemMap.getString("quantity"));
            ECommerceCartItem item = new ECommerceCartItem(product, price, quantity);
            if (cartItemMap.hasKey("referrer")) {
                ReadableMap referrer = cartItemMap.getMap("referrer");
                item.setReferrer(toECommerceReferrer(referrer));
            }
            return item;
        }
    }

    @NonNull
    static ECommerceOrder toECommerceOrder(@NonNull ReadableMap orderMap) {
        String orderId = orderMap.getString("orderId");
        ReadableArray list = orderMap.getArray("products");
        List<ECommerceCartItem> newlist = new ArrayList<ECommerceCartItem>(Objects.requireNonNull(list).size());
        for (int i = 0; i < list.size(); i++) {
            ECommerceCartItem component = toECommerceCartItem(list.getMap(i));
            newlist.add(component);
        }
        ECommerceOrder order = new ECommerceOrder(Objects.requireNonNull(orderId), newlist);
        if (orderMap.hasKey("payload")) {
            ReadableMap payload = orderMap.getMap("payload");
            order.setPayload(toMapOfStrings(payload));
        }
        return order;
    }

    @Nullable
    static ECommerceEvent toECommerceEvent(@NonNull ReadableMap ecommerceEventMap) {
        String type = ecommerceEventMap.getString("ecommerceEvent");
        if (type == null) return null;
        if (type.equals("showScreenEvent")) {
            ReadableMap screenMap = ecommerceEventMap.getMap("ecommerceScreen");
            if (screenMap != null) {
                return ECommerceEvent.showScreenEvent(toECommerceScreen(screenMap));
            }
        }
        if (type.equals("showProductCardEvent")) {
            ReadableMap cardMap = ecommerceEventMap.getMap("product");
            ReadableMap screenMap = ecommerceEventMap.getMap("ecommerceScreen");
            if (cardMap != null && screenMap != null) {
                return ECommerceEvent.showProductCardEvent(toECommerceProduct(cardMap), toECommerceScreen(screenMap));
            }
        }
        if (type.equals("showProductDetailsEvent")) {
            ReadableMap productMap = ecommerceEventMap.getMap("product");
            ReadableMap referrerMap = ecommerceEventMap.getMap("referrer");
            if (productMap != null) {
                return ECommerceEvent.showProductDetailsEvent(toECommerceProduct(productMap), toECommerceReferrer(referrerMap));
            }
        }
        if (type.equals("addCartItemEvent")) {
            ECommerceCartItem cartItem = toECommerceCartItem(ecommerceEventMap.getMap("cartItem"));
            if (cartItem != null) {
                return ECommerceEvent.addCartItemEvent(cartItem);
            }
        }
        if (type.equals("removeCartItemEvent")) {
            ECommerceCartItem cartItem = toECommerceCartItem(ecommerceEventMap.getMap("cartItem"));
            if (cartItem != null) {
                return ECommerceEvent.removeCartItemEvent(cartItem);
            }
        }
        if (type.equals("beginCheckoutEvent")) {
            ReadableMap orderMap = ecommerceEventMap.getMap("order");
            if (orderMap != null) {
                return ECommerceEvent.beginCheckoutEvent(toECommerceOrder(orderMap));
            }
        }
        if (type.equals("purchaseEvent")) {
            ReadableMap orderMap = ecommerceEventMap.getMap("order");
            if (orderMap != null) {
                return ECommerceEvent.purchaseEvent(toECommerceOrder(orderMap));
            }
        }
        return null;
    }

    @NonNull
    static Revenue toRevenue(@NonNull ReadableMap revenueMap) {
        long price = (long) (revenueMap.getDouble("price") * 1000000);
        String currency = revenueMap.getString("currency");
        Revenue.Builder revenue = Revenue.newBuilder(price, Currency.getInstance(currency));
        if (revenueMap.hasKey("productID")) {
            revenue.withProductID(revenueMap.getString("productID"));
        }
        if (revenueMap.hasKey("payload")) {
            revenue.withPayload(revenueMap.getString("payload"));
        }
        if (revenueMap.hasKey("quantity")) {
            revenue.withQuantity(revenueMap.getInt("quantity"));
        }
        revenue.withReceipt(toReceipt(revenueMap.getMap("receipt")));
        return revenue.build();
    }

    @Nullable
    static Revenue.Receipt toReceipt(@Nullable ReadableMap receipt) {
        if (receipt == null) {
          return null;
        }
        Revenue.Receipt.Builder revenueReceipt = Revenue.Receipt.newBuilder();
        if (receipt.hasKey("receiptData")) {
            revenueReceipt.withData(receipt.getString("receiptData"));
        }
        if (receipt.hasKey("signature")) {
            revenueReceipt.withSignature(receipt.getString("signature"));
        }
        return revenueReceipt.build();
    }

    @NonNull
    private static AdRevenue.Builder parseAdRevenuePrice(@NonNull ReadableMap revenueMap) {
        ReadableType priceType = revenueMap.getType("price");
        String currency = revenueMap.getString("currency");
        if (priceType == ReadableType.Number) {
            double price = revenueMap.getDouble("price");
            return AdRevenue.newBuilder(price, Currency.getInstance(currency));
        } else {
            String price = revenueMap.getString("price");
            return AdRevenue.newBuilder(new BigDecimal(price), Currency.getInstance(currency));
        }
    }

    @NonNull
    static AdRevenue toAdRevenue(@NonNull ReadableMap revenueMap) {
        AdRevenue.Builder adRevenue = parseAdRevenuePrice(revenueMap);
        if (revenueMap.hasKey("payload")) {
            ReadableMap payloadMap = revenueMap.getMap("payload");
            adRevenue.withPayload(toMapOfStrings(payloadMap));
        }
        if (revenueMap.hasKey("adType")) {
            String type = revenueMap.getString("adType");
            if (type != null) {
                adRevenue.withAdType(toAdType(type));
            }
        }
        if (revenueMap.hasKey("adNetwork")) {
            adRevenue.withAdNetwork(revenueMap.getString("adNetwork"));
        }
        if (revenueMap.hasKey("adPlacementID")) {
            adRevenue.withAdPlacementId(revenueMap.getString("adPlacementID"));
        }
        if (revenueMap.hasKey("adPlacementName")) {
            adRevenue.withAdPlacementName(revenueMap.getString("adPlacementName"));
        }
        if (revenueMap.hasKey("adUnitID")) {
            adRevenue.withAdUnitId(revenueMap.getString("adUnitID"));
        }
        if (revenueMap.hasKey("adUnitName")) {
            adRevenue.withAdUnitName(revenueMap.getString("adUnitName"));
        }
        if (revenueMap.hasKey("precision")) {
            adRevenue.withPrecision(revenueMap.getString("precision"));
        }
        return adRevenue.build();
    }

    @NonNull
    static AdType toAdType(@NonNull String type) {
        switch (type) {
            case "native":
                return AdType.NATIVE;
            case "banner":
                return AdType.BANNER;
            case "mrec":
                return AdType.MREC;
            case "interstitial":
                return AdType.INTERSTITIAL;
            case "rewarded":
                return AdType.REWARDED;
            case "app_open":
                return AdType.APP_OPEN;
          default:
                return AdType.OTHER;
        }
    }

    @NonNull
    static UserProfile toUserProfile(ReadableMap userProfileMap) {
        return UserProfileSerializer.fromReadableMap(userProfileMap);
    }

    @Nullable
    static Map<String, String> toMapOfStrings(@Nullable ReadableMap oldMap) {
        if (oldMap == null) {
            return null;
        }
        HashMap<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : oldMap.toHashMap().entrySet()) {
            if (entry.getValue() instanceof String) {
                newMap.put(entry.getKey(), (String) entry.getValue());
            }
        }
        return newMap;
    }

    @NonNull
    static List<String> toListOfStrings(@Nullable ReadableArray oldArray) {
        List<String> newArray = new ArrayList<>();
        if (oldArray == null) {
            return newArray;
        }
        for (int i = 0; i < oldArray.size(); i++) {
            newArray.add(oldArray.getString(i));
        }
        return newArray;
    }

    @NonNull
    static ReporterConfig toReporterConfig(@NonNull ReadableMap configMap) {
        ReporterConfig.Builder builder = ReporterConfig.newConfigBuilder(Objects.requireNonNull(configMap.getString("apiKey")));

        if (configMap.hasKey("logs") && Boolean.TRUE.equals(configMap.getBoolean("logs"))) {
            builder.withLogs();
        }
        if (configMap.hasKey("maxReportsInDatabaseCount")) {
            builder.withMaxReportsInDatabaseCount(configMap.getInt("maxReportsInDatabaseCount"));
        }
        if (configMap.hasKey("sessionTimeout")) {
            builder.withSessionTimeout(configMap.getInt("sessionTimeout"));
        }
        if (configMap.hasKey("dataSendingEnabled")) {
            builder.withDataSendingEnabled(configMap.getBoolean("dataSendingEnabled"));
        }
        if (configMap.hasKey("appEnvironment")) {
            ReadableMap appMap = configMap.getMap("appEnvironment");
            if (appMap != null) {
                for (Map.Entry<String, Object> entry : appMap.toHashMap().entrySet()) {
                    if (entry.getValue() instanceof String) {
                        builder.withAppEnvironmentValue(entry.getKey(), entry.getValue().toString());
                    }
                }
            }
        }
        if (configMap.hasKey("dispatchPeriodSeconds")) {
            builder.withDispatchPeriodSeconds(configMap.getInt("dispatchPeriodSeconds"));
        }
        if (configMap.hasKey("userProfileID")) {
            builder.withUserProfileID(configMap.getString("userProfileID"));
        }
        if (configMap.hasKey("maxReportsCount")) {
            builder.withMaxReportsCount(configMap.getInt("maxReportsCount"));
        }
        return builder.build();
    }
}

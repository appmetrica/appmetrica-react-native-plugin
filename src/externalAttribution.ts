type ExternalAttributionSource =
  | 'Adjust'
  | 'Airbridge'
  | 'AppsFlyer'
  | 'Kochava'
  | 'Singular'
  | 'Tenjin';

export interface ExternalAttribution {
  source: ExternalAttributionSource;
  value: Record<string, object>;
}

export class ExternalAttributions {
  static adjust(data: Record<string, any>): ExternalAttribution {
    return {
      source: 'Adjust',
      value: data,
    };
  }

  static airbridge(data: Record<string, any>): ExternalAttribution {
    return {
      source: 'Airbridge',
      value: data,
    };
  }

  static appsflyer(data: Record<string, any>): ExternalAttribution {
    return {
      source: 'AppsFlyer',
      value: data.data,
    };
  }

  static kochava(data: Record<string, any>): ExternalAttribution {
    return {
      source: 'Kochava',
      value: data.raw,
    };
  }

  static singular(data: Map<string, object>): ExternalAttribution {
    return {
      source: 'Singular',
      value: Object.fromEntries(data),
    };
  }

  static tenjin(data: Record<string, any>): ExternalAttribution {
    return {
      source: 'Tenjin',
      value: data,
    };
  }
}

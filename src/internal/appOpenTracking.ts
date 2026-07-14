import { Linking } from 'react-native';
import AppMetricaNative from '../specs/NativeAppMetrica';

export function startAppOpenTracking() {
  const getUrlAsync = async () => {
    const initialUrl = await Linking.getInitialURL();
    if (initialUrl != null) {
      AppMetricaNative.reportAppOpen(initialUrl);
    }
  };
  const callback = (event: { url: string }) => {
    AppMetricaNative.reportAppOpen(event.url);
  };
  getUrlAsync();
  Linking.addEventListener('url', callback);
}

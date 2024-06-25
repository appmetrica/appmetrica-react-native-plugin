# @appmetrica/react-native-analytics

React Native bridge to the [AppMetrica](https://appmetrica.io) on both iOS and Android.

## Installation

```sh
npm install @appmetrica/react-native-analytics
```

## Usage

```js
import AppMetrica from '@appmetrica/react-native-analytics';

// Starts the statistics collection process.
AppMetrica.activateWithConfig({
  apiKey: '...KEY...',
  sessionTimeout: 120,
  firstActivationAsUpdate: false,
});

// Sends a custom event message and additional parameters (optional).
AppMetrica.reportEvent('My event');
AppMetrica.reportEvent('My event', {foo: 'bar'});

// Send a custom error event.
AppMetrica.reportError('My error');
```

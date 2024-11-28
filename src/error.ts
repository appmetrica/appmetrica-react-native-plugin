import { Platform } from 'react-native';
import React from 'react';

const traceRegex = /^\s*at (.*?) ?\((.*?)(?::(\d+))?(?::(\d+))?\)?\s*$/i;

function parseTraceLine(line: string): StackTraceItem | undefined {
  const parts = traceRegex.exec(line);
  if (parts === null) {
    return undefined;
  }
  return {
    fileName: parts[2],
    methodName: parts[1],
    column: parts[3],
    line: parts[4],
  };
}

function getRNVersion(): string {
  const versions = Platform.constants.reactNativeVersion;
  return versions.major + '.' + versions.minor + '.' + versions.patch;
}

function getReactVersion(): string {
  return React.version;
}

function getPluginEnvironment(): Record<string, string> | undefined {
  return {
    reactVersion: getReactVersion(),
  };
}

function parseStackTrace(
  stackTrace?: string
): Array<StackTraceItem> | undefined {
  if (stackTrace === undefined) {
    return undefined;
  }
  try {
    const lines = stackTrace.split('\n');
    const array: Array<StackTraceItem> = [];
    lines?.forEach((line: string) => {
      const item = parseTraceLine(line);
      if (item !== undefined) {
        array.push(item);
      }
    });
    return array;
  } catch(e) {
    console.log(e);
    return undefined;
  }
}

type StackTraceItem = {
  fileName?: string;
  className?: string;
  methodName?: string;
  line?: string;
  column?: string;
};

export class AppMetricaError {
  message?: string;
  stackTrace?: Array<StackTraceItem>;
  errorName?: string;
  pluginEnvironment?: Record<string, string>;
  virtualMachineVersion?: string;

  constructor(errorName?: string, message?: string) {
    this.errorName = errorName;
    this.message = message;
    this.pluginEnvironment = getPluginEnvironment();
    this.virtualMachineVersion = getRNVersion();
  }

  static withError(error: Error): AppMetricaError {
    const newError = new AppMetricaError(error.name, error.message);
    newError.stackTrace = parseStackTrace(error.stack);
    return newError;
  }

  static withObject(error?: Object): AppMetricaError {
    return new AppMetricaError(undefined, JSON.stringify(error));
  }
}

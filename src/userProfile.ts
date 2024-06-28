export class UserProfile {
  private attributes: Array<UserProfileUpdate>;

  constructor() {
    this.attributes = [];
  }

  apply(attribute: UserProfileUpdate): UserProfile {
    this.attributes.push(attribute);

    return this;
  }
}

type UserProfileUpdateType =
  | 'BirthDateWithAge'
  | 'BirthDateWithYear'
  | 'BirthDateWithMonth'
  | 'BirthDateWithDay'
  | 'BirthDateValueReset'
  | 'BooleanValue'
  | 'BooleanValueReset'
  | 'Counter'
  | 'GenderValue'
  | 'GenderValueReset'
  | 'NameValue'
  | 'NameValueReset'
  | 'NotificationsEnabledValue'
  | 'NotificationsEnabledValueReset'
  | 'NumberValue'
  | 'NumberValueReset'
  | 'StringValue'
  | 'StringValueReset';

export interface UserProfileUpdate {
  type: UserProfileUpdateType;
  key?: string;
  value?: any;
  ifUndefined?: boolean;
  age?: number;
  year?: number;
  month?: number;
  day?: number;
  delta?: number;
}

export type UserProfileGender = 'male' | 'female' | 'other';

export class Attributes {
  static birthDate() {
    return new BirthDateAttribute();
  }

  static customBoolean(key: string) {
    return new BooleanAttribute(key);
  }

  static customCounter(key: string) {
    return new CounterAttribute(key);
  }

  static customNumber(key: string) {
    return new NumberAttribute(key);
  }

  static customString(key: string) {
    return new StringAttribute(key);
  }

  static gender() {
    return new GenderAttribute();
  }

  static customName() {
    return new NameAttribute();
  }

  static notificationsEnabled() {
    return new NotificationsEnabledAttribute();
  }
}

export class BirthDateAttribute {
  withAge(age: number): UserProfileUpdate {
    return {
      type: 'BirthDateWithAge',
      age: age,
      ifUndefined: false,
    };
  }

  withAgeIfUndefined(age: number): UserProfileUpdate {
    return {
      type: 'BirthDateWithAge',
      age: age,
      ifUndefined: true,
    };
  }

  withYear(year: number): UserProfileUpdate {
    return {
      type: 'BirthDateWithYear',
      year: year,
      ifUndefined: false,
    };
  }

  withYearIfUndefined(year: number): UserProfileUpdate {
    return {
      type: 'BirthDateWithYear',
      year: year,
      ifUndefined: true,
    };
  }

  withMonth(year: number, month: number): UserProfileUpdate {
    return {
      type: 'BirthDateWithMonth',
      year: year,
      month: month,
      ifUndefined: false,
    };
  }

  withMonthIfUndefined(year: number, month: number): UserProfileUpdate {
    return {
      type: 'BirthDateWithMonth',
      year: year,
      month: month,
      ifUndefined: true,
    };
  }

  withDay(year: number, month: number, day: number): UserProfileUpdate {
    return {
      type: 'BirthDateWithDay',
      year: year,
      month: month,
      day: day,
      ifUndefined: false,
    };
  }

  withDayIfUndefined(
    year: number,
    month: number,
    day: number
  ): UserProfileUpdate {
    return {
      type: 'BirthDateWithDay',
      year: year,
      month: month,
      day: day,
      ifUndefined: true,
    };
  }

  withDate(date: Date): UserProfileUpdate {
    return this.withDay(date.getFullYear(), date.getMonth(), date.getDate());
  }

  withDateIfUndefined(date: Date): UserProfileUpdate {
    return this.withDayIfUndefined(
      date.getFullYear(),
      date.getMonth(),
      date.getDate()
    );
  }

  withValueReset(): UserProfileUpdate {
    return {
      type: 'BirthDateValueReset',
    };
  }
}

export class BooleanAttribute {
  private readonly key: string;

  constructor(key: string) {
    this.key = key;
  }

  withValue(value: boolean): UserProfileUpdate {
    return {
      type: 'BooleanValue',
      key: this.key,
      value: value,
      ifUndefined: false,
    };
  }

  withValueIfUndefined(value: boolean): UserProfileUpdate {
    return {
      type: 'BooleanValue',
      key: this.key,
      value: value,
      ifUndefined: true,
    };
  }

  withValueReset(): UserProfileUpdate {
    return {
      type: 'BooleanValueReset',
      key: this.key,
    };
  }
}

export class CounterAttribute {
  private readonly key: string;

  constructor(key: string) {
    this.key = key;
  }

  withDelta(delta: number): UserProfileUpdate {
    return {
      type: 'Counter',
      key: this.key,
      delta: delta,
    };
  }
}

export class GenderAttribute {
  withValue(gender: UserProfileGender): UserProfileUpdate {
    return {
      type: 'GenderValue',
      value: gender,
      ifUndefined: false,
    };
  }

  withValueIfUndefined(gender: UserProfileGender): UserProfileUpdate {
    return {
      type: 'GenderValue',
      value: gender,
      ifUndefined: true,
    };
  }

  withValueReset(): UserProfileUpdate {
    return {
      type: 'GenderValueReset',
    };
  }
}

export class NameAttribute {
  withValue(value: string): UserProfileUpdate {
    return {
      type: 'NameValue',
      value: value,
      ifUndefined: false,
    };
  }

  withValueIfUndefined(value: string): UserProfileUpdate {
    return {
      type: 'NameValue',
      value: value,
      ifUndefined: true,
    };
  }

  withValueReset(): UserProfileUpdate {
    return {
      type: 'NameValueReset',
    };
  }
}

export class NotificationsEnabledAttribute {
  withValue(value: boolean): UserProfileUpdate {
    return {
      type: 'NotificationsEnabledValue',
      value: value,
      ifUndefined: false,
    };
  }

  withValueIfUndefined(value: boolean): UserProfileUpdate {
    return {
      type: 'NotificationsEnabledValue',
      value: value,
      ifUndefined: true,
    };
  }

  withValueReset(): UserProfileUpdate {
    return {
      type: 'NotificationsEnabledValueReset',
    };
  }
}

export class NumberAttribute {
  private readonly key: string;

  constructor(key: string) {
    this.key = key;
  }

  withValue(value: number): UserProfileUpdate {
    return {
      type: 'NumberValue',
      key: this.key,
      value: value,
      ifUndefined: false,
    };
  }

  withValueIfUndefined(value: number): UserProfileUpdate {
    return {
      type: 'NumberValue',
      key: this.key,
      value: value,
      ifUndefined: true,
    };
  }

  withValueReset(): UserProfileUpdate {
    return {
      type: 'NumberValueReset',
      key: this.key,
    };
  }
}

export class StringAttribute {
  private readonly key: string;

  constructor(key: string) {
    this.key = key;
  }

  withValue(value: string): UserProfileUpdate {
    return {
      type: 'StringValue',
      key: this.key,
      value: value,
      ifUndefined: false,
    };
  }

  withValueIfUndefined(value: string): UserProfileUpdate {
    return {
      type: 'StringValue',
      key: this.key,
      value: value,
      ifUndefined: true,
    };
  }

  withValueReset(): UserProfileUpdate {
    return {
      type: 'StringValueReset',
      key: this.key,
    };
  }
}

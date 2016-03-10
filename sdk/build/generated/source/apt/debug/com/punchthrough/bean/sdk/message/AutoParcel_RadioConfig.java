package com.punchthrough.bean.sdk.message;


final class AutoParcel_RadioConfig extends RadioConfig {
  private final String name;
  private final int advertisementInterval;
  private final int connectionInterval;
  private final RadioConfig.TxPower power;
  private final RadioConfig.AdvertisementMode advertisementMode;
  private final int beaconUuid;
  private final int beaconMajor;
  private final int beaconMinor;

  AutoParcel_RadioConfig(
      String name,
      int advertisementInterval,
      int connectionInterval,
      RadioConfig.TxPower power,
      RadioConfig.AdvertisementMode advertisementMode,
      int beaconUuid,
      int beaconMajor,
      int beaconMinor) {
    if (name == null) {
      throw new NullPointerException("Null name");
    }
    this.name = name;
    this.advertisementInterval = advertisementInterval;
    this.connectionInterval = connectionInterval;
    if (power == null) {
      throw new NullPointerException("Null power");
    }
    this.power = power;
    if (advertisementMode == null) {
      throw new NullPointerException("Null advertisementMode");
    }
    this.advertisementMode = advertisementMode;
    this.beaconUuid = beaconUuid;
    this.beaconMajor = beaconMajor;
    this.beaconMinor = beaconMinor;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public int advertisementInterval() {
    return advertisementInterval;
  }

  @Override
  public int connectionInterval() {
    return connectionInterval;
  }

  @Override
  public RadioConfig.TxPower power() {
    return power;
  }

  @Override
  public RadioConfig.AdvertisementMode advertisementMode() {
    return advertisementMode;
  }

  @Override
  public int beaconUuid() {
    return beaconUuid;
  }

  @Override
  public int beaconMajor() {
    return beaconMajor;
  }

  @Override
  public int beaconMinor() {
    return beaconMinor;
  }

  @Override
  public String toString() {
    return "RadioConfig{"
        + "name=" + name
        + ", advertisementInterval=" + advertisementInterval
        + ", connectionInterval=" + connectionInterval
        + ", power=" + power
        + ", advertisementMode=" + advertisementMode
        + ", beaconUuid=" + beaconUuid
        + ", beaconMajor=" + beaconMajor
        + ", beaconMinor=" + beaconMinor
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RadioConfig) {
      RadioConfig that = (RadioConfig) o;
      return (this.name.equals(that.name()))
          && (this.advertisementInterval == that.advertisementInterval())
          && (this.connectionInterval == that.connectionInterval())
          && (this.power.equals(that.power()))
          && (this.advertisementMode.equals(that.advertisementMode()))
          && (this.beaconUuid == that.beaconUuid())
          && (this.beaconMajor == that.beaconMajor())
          && (this.beaconMinor == that.beaconMinor());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= name.hashCode();
    h *= 1000003;
    h ^= advertisementInterval;
    h *= 1000003;
    h ^= connectionInterval;
    h *= 1000003;
    h ^= power.hashCode();
    h *= 1000003;
    h ^= advertisementMode.hashCode();
    h *= 1000003;
    h ^= beaconUuid;
    h *= 1000003;
    h ^= beaconMajor;
    h *= 1000003;
    h ^= beaconMinor;
    return h;
  }



  public static final android.os.Parcelable.Creator<RadioConfig> CREATOR = new android.os.Parcelable.Creator<RadioConfig>() {
    @Override public RadioConfig createFromParcel(android.os.Parcel in) {
      return new AutoParcel_RadioConfig(in);
    }
    @Override public RadioConfig[] newArray(int size) {
      return new RadioConfig[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_RadioConfig.class.getClassLoader();

  private AutoParcel_RadioConfig(android.os.Parcel in) {
    this(
      (String) in.readValue(CL),
      (Integer) in.readValue(CL),
      (Integer) in.readValue(CL),
      (RadioConfig.TxPower) in.readValue(CL),
      (RadioConfig.AdvertisementMode) in.readValue(CL),
      (Integer) in.readValue(CL),
      (Integer) in.readValue(CL),
      (Integer) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(name);
    dest.writeValue(advertisementInterval);
    dest.writeValue(connectionInterval);
    dest.writeValue(power);
    dest.writeValue(advertisementMode);
    dest.writeValue(beaconUuid);
    dest.writeValue(beaconMajor);
    dest.writeValue(beaconMinor);

  }

  @Override public int describeContents() {
    return 0;
  }

}

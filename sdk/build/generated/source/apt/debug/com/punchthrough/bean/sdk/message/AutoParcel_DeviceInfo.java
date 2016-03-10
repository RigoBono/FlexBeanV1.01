package com.punchthrough.bean.sdk.message;


final class AutoParcel_DeviceInfo extends DeviceInfo {
  private final String hardwareVersion;
  private final String softwareVersion;
  private final String firmwareVersion;

  AutoParcel_DeviceInfo(
      String hardwareVersion,
      String softwareVersion,
      String firmwareVersion) {
    if (hardwareVersion == null) {
      throw new NullPointerException("Null hardwareVersion");
    }
    this.hardwareVersion = hardwareVersion;
    if (softwareVersion == null) {
      throw new NullPointerException("Null softwareVersion");
    }
    this.softwareVersion = softwareVersion;
    if (firmwareVersion == null) {
      throw new NullPointerException("Null firmwareVersion");
    }
    this.firmwareVersion = firmwareVersion;
  }

  @Override
  public String hardwareVersion() {
    return hardwareVersion;
  }

  @Override
  public String softwareVersion() {
    return softwareVersion;
  }

  @Override
  public String firmwareVersion() {
    return firmwareVersion;
  }

  @Override
  public String toString() {
    return "DeviceInfo{"
        + "hardwareVersion=" + hardwareVersion
        + ", softwareVersion=" + softwareVersion
        + ", firmwareVersion=" + firmwareVersion
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof DeviceInfo) {
      DeviceInfo that = (DeviceInfo) o;
      return (this.hardwareVersion.equals(that.hardwareVersion()))
          && (this.softwareVersion.equals(that.softwareVersion()))
          && (this.firmwareVersion.equals(that.firmwareVersion()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= hardwareVersion.hashCode();
    h *= 1000003;
    h ^= softwareVersion.hashCode();
    h *= 1000003;
    h ^= firmwareVersion.hashCode();
    return h;
  }



  public static final android.os.Parcelable.Creator<DeviceInfo> CREATOR = new android.os.Parcelable.Creator<DeviceInfo>() {
    @Override public DeviceInfo createFromParcel(android.os.Parcel in) {
      return new AutoParcel_DeviceInfo(in);
    }
    @Override public DeviceInfo[] newArray(int size) {
      return new DeviceInfo[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_DeviceInfo.class.getClassLoader();

  private AutoParcel_DeviceInfo(android.os.Parcel in) {
    this(
      (String) in.readValue(CL),
      (String) in.readValue(CL),
      (String) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(hardwareVersion);
    dest.writeValue(softwareVersion);
    dest.writeValue(firmwareVersion);

  }

  @Override public int describeContents() {
    return 0;
  }

}

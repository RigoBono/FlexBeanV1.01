package com.punchthrough.bean.sdk.internal.upload.firmware;

import java.util.Arrays;

final class AutoParcel_FirmwareMetadata extends FirmwareMetadata {
  private final byte[] data;

  AutoParcel_FirmwareMetadata(
      byte[] data) {
    if (data == null) {
      throw new NullPointerException("Null data");
    }
    this.data = data;
  }

  @Override
  protected byte[] data() {
    return data.clone();
  }

  @Override
  public String toString() {
    return "FirmwareMetadata{"
        + "data=" + Arrays.toString(data)
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof FirmwareMetadata) {
      FirmwareMetadata that = (FirmwareMetadata) o;
      return (Arrays.equals(this.data, (that instanceof AutoParcel_FirmwareMetadata) ? ((AutoParcel_FirmwareMetadata) that).data : that.data()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= Arrays.hashCode(data);
    return h;
  }



  public static final android.os.Parcelable.Creator<FirmwareMetadata> CREATOR = new android.os.Parcelable.Creator<FirmwareMetadata>() {
    @Override public FirmwareMetadata createFromParcel(android.os.Parcel in) {
      return new AutoParcel_FirmwareMetadata(in);
    }
    @Override public FirmwareMetadata[] newArray(int size) {
      return new FirmwareMetadata[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_FirmwareMetadata.class.getClassLoader();

  private AutoParcel_FirmwareMetadata(android.os.Parcel in) {
    this(
      (byte[]) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(data);

  }

  @Override public int describeContents() {
    return 0;
  }

}

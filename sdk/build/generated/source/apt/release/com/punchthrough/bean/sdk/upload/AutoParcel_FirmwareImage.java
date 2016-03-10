package com.punchthrough.bean.sdk.upload;

import java.util.Arrays;

final class AutoParcel_FirmwareImage extends FirmwareImage {
  private final byte[] data;

  AutoParcel_FirmwareImage(
      byte[] data) {
    if (data == null) {
      throw new NullPointerException("Null data");
    }
    this.data = data;
  }

  @Override
  public byte[] data() {
    return data.clone();
  }

  @Override
  public String toString() {
    return "FirmwareImage{"
        + "data=" + Arrays.toString(data)
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof FirmwareImage) {
      FirmwareImage that = (FirmwareImage) o;
      return (Arrays.equals(this.data, (that instanceof AutoParcel_FirmwareImage) ? ((AutoParcel_FirmwareImage) that).data : that.data()));
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



  public static final android.os.Parcelable.Creator<FirmwareImage> CREATOR = new android.os.Parcelable.Creator<FirmwareImage>() {
    @Override public FirmwareImage createFromParcel(android.os.Parcel in) {
      return new AutoParcel_FirmwareImage(in);
    }
    @Override public FirmwareImage[] newArray(int size) {
      return new FirmwareImage[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_FirmwareImage.class.getClassLoader();

  private AutoParcel_FirmwareImage(android.os.Parcel in) {
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

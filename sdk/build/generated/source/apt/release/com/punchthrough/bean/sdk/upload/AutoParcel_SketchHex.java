package com.punchthrough.bean.sdk.upload;

import java.util.Arrays;

final class AutoParcel_SketchHex extends SketchHex {
  private final String sketchName;
  private final byte[] bytes;

  AutoParcel_SketchHex(
      String sketchName,
      byte[] bytes) {
    if (sketchName == null) {
      throw new NullPointerException("Null sketchName");
    }
    this.sketchName = sketchName;
    if (bytes == null) {
      throw new NullPointerException("Null bytes");
    }
    this.bytes = bytes;
  }

  @Override
  public String sketchName() {
    return sketchName;
  }

  @Override
  public byte[] bytes() {
    return bytes.clone();
  }

  @Override
  public String toString() {
    return "SketchHex{"
        + "sketchName=" + sketchName
        + ", bytes=" + Arrays.toString(bytes)
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof SketchHex) {
      SketchHex that = (SketchHex) o;
      return (this.sketchName.equals(that.sketchName()))
          && (Arrays.equals(this.bytes, (that instanceof AutoParcel_SketchHex) ? ((AutoParcel_SketchHex) that).bytes : that.bytes()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= sketchName.hashCode();
    h *= 1000003;
    h ^= Arrays.hashCode(bytes);
    return h;
  }



  public static final android.os.Parcelable.Creator<SketchHex> CREATOR = new android.os.Parcelable.Creator<SketchHex>() {
    @Override public SketchHex createFromParcel(android.os.Parcel in) {
      return new AutoParcel_SketchHex(in);
    }
    @Override public SketchHex[] newArray(int size) {
      return new SketchHex[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_SketchHex.class.getClassLoader();

  private AutoParcel_SketchHex(android.os.Parcel in) {
    this(
      (String) in.readValue(CL),
      (byte[]) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(sketchName);
    dest.writeValue(bytes);

  }

  @Override public int describeContents() {
    return 0;
  }

}

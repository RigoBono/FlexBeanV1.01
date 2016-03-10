package com.punchthrough.bean.sdk.message;

import java.util.Date;

final class AutoParcel_SketchMetadata extends SketchMetadata {
  private final int hexSize;
  private final int hexCrc;
  private final Date timestamp;
  private final String hexName;

  AutoParcel_SketchMetadata(
      int hexSize,
      int hexCrc,
      Date timestamp,
      String hexName) {
    this.hexSize = hexSize;
    this.hexCrc = hexCrc;
    if (timestamp == null) {
      throw new NullPointerException("Null timestamp");
    }
    this.timestamp = timestamp;
    if (hexName == null) {
      throw new NullPointerException("Null hexName");
    }
    this.hexName = hexName;
  }

  @Override
  public int hexSize() {
    return hexSize;
  }

  @Override
  public int hexCrc() {
    return hexCrc;
  }

  @Override
  public Date timestamp() {
    return timestamp;
  }

  @Override
  public String hexName() {
    return hexName;
  }

  @Override
  public String toString() {
    return "SketchMetadata{"
        + "hexSize=" + hexSize
        + ", hexCrc=" + hexCrc
        + ", timestamp=" + timestamp
        + ", hexName=" + hexName
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof SketchMetadata) {
      SketchMetadata that = (SketchMetadata) o;
      return (this.hexSize == that.hexSize())
          && (this.hexCrc == that.hexCrc())
          && (this.timestamp.equals(that.timestamp()))
          && (this.hexName.equals(that.hexName()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= hexSize;
    h *= 1000003;
    h ^= hexCrc;
    h *= 1000003;
    h ^= timestamp.hashCode();
    h *= 1000003;
    h ^= hexName.hashCode();
    return h;
  }



  public static final android.os.Parcelable.Creator<SketchMetadata> CREATOR = new android.os.Parcelable.Creator<SketchMetadata>() {
    @Override public SketchMetadata createFromParcel(android.os.Parcel in) {
      return new AutoParcel_SketchMetadata(in);
    }
    @Override public SketchMetadata[] newArray(int size) {
      return new SketchMetadata[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_SketchMetadata.class.getClassLoader();

  private AutoParcel_SketchMetadata(android.os.Parcel in) {
    this(
      (Integer) in.readValue(CL),
      (Integer) in.readValue(CL),
      (Date) in.readValue(CL),
      (String) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(hexSize);
    dest.writeValue(hexCrc);
    dest.writeValue(timestamp);
    dest.writeValue(hexName);

  }

  @Override public int describeContents() {
    return 0;
  }

}

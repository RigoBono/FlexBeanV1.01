package com.punchthrough.bean.sdk.message;

import java.util.Arrays;

final class AutoParcel_ScratchData extends ScratchData {
  private final int number;
  private final byte[] data;

  AutoParcel_ScratchData(
      int number,
      byte[] data) {
    this.number = number;
    if (data == null) {
      throw new NullPointerException("Null data");
    }
    this.data = data;
  }

  @Override
  public int number() {
    return number;
  }

  @Override
  public byte[] data() {
    return data.clone();
  }

  @Override
  public String toString() {
    return "ScratchData{"
        + "number=" + number
        + ", data=" + Arrays.toString(data)
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ScratchData) {
      ScratchData that = (ScratchData) o;
      return (this.number == that.number())
          && (Arrays.equals(this.data, (that instanceof AutoParcel_ScratchData) ? ((AutoParcel_ScratchData) that).data : that.data()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= number;
    h *= 1000003;
    h ^= Arrays.hashCode(data);
    return h;
  }



  public static final android.os.Parcelable.Creator<ScratchData> CREATOR = new android.os.Parcelable.Creator<ScratchData>() {
    @Override public ScratchData createFromParcel(android.os.Parcel in) {
      return new AutoParcel_ScratchData(in);
    }
    @Override public ScratchData[] newArray(int size) {
      return new ScratchData[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_ScratchData.class.getClassLoader();

  private AutoParcel_ScratchData(android.os.Parcel in) {
    this(
      (Integer) in.readValue(CL),
      (byte[]) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(number);
    dest.writeValue(data);

  }

  @Override public int describeContents() {
    return 0;
  }

}

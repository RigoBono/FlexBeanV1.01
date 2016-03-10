package com.punchthrough.bean.sdk.message;


final class AutoParcel_LedColor extends LedColor {
  private final int red;
  private final int green;
  private final int blue;

  AutoParcel_LedColor(
      int red,
      int green,
      int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  @Override
  public int red() {
    return red;
  }

  @Override
  public int green() {
    return green;
  }

  @Override
  public int blue() {
    return blue;
  }

  @Override
  public String toString() {
    return "LedColor{"
        + "red=" + red
        + ", green=" + green
        + ", blue=" + blue
        + "}";
  }


  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= red;
    h *= 1000003;
    h ^= green;
    h *= 1000003;
    h ^= blue;
    return h;
  }



  public static final android.os.Parcelable.Creator<LedColor> CREATOR = new android.os.Parcelable.Creator<LedColor>() {
    @Override public LedColor createFromParcel(android.os.Parcel in) {
      return new AutoParcel_LedColor(in);
    }
    @Override public LedColor[] newArray(int size) {
      return new LedColor[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_LedColor.class.getClassLoader();

  private AutoParcel_LedColor(android.os.Parcel in) {
    this(
      (Integer) in.readValue(CL),
      (Integer) in.readValue(CL),
      (Integer) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(red);
    dest.writeValue(green);
    dest.writeValue(blue);

  }

  @Override public int describeContents() {
    return 0;
  }

}

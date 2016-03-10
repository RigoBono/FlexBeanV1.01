package com.punchthrough.bean.sdk.message;


final class AutoParcel_Acceleration extends Acceleration {
  private final double x;
  private final double y;
  private final double z;

  AutoParcel_Acceleration(
      double x,
      double y,
      double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public double x() {
    return x;
  }

  @Override
  public double y() {
    return y;
  }

  @Override
  public double z() {
    return z;
  }

  @Override
  public String toString() {
    return "Acceleration{"
        + "x=" + x
        + ", y=" + y
        + ", z=" + z
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Acceleration) {
      Acceleration that = (Acceleration) o;
      return (Double.doubleToLongBits(this.x) == Double.doubleToLongBits(that.x()))
          && (Double.doubleToLongBits(this.y) == Double.doubleToLongBits(that.y()))
          && (Double.doubleToLongBits(this.z) == Double.doubleToLongBits(that.z()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= (Double.doubleToLongBits(x) >>> 32) ^ Double.doubleToLongBits(x);
    h *= 1000003;
    h ^= (Double.doubleToLongBits(y) >>> 32) ^ Double.doubleToLongBits(y);
    h *= 1000003;
    h ^= (Double.doubleToLongBits(z) >>> 32) ^ Double.doubleToLongBits(z);
    return h;
  }



  public static final android.os.Parcelable.Creator<Acceleration> CREATOR = new android.os.Parcelable.Creator<Acceleration>() {
    @Override public Acceleration createFromParcel(android.os.Parcel in) {
      return new AutoParcel_Acceleration(in);
    }
    @Override public Acceleration[] newArray(int size) {
      return new Acceleration[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_Acceleration.class.getClassLoader();

  private AutoParcel_Acceleration(android.os.Parcel in) {
    this(
      (Double) in.readValue(CL),
      (Double) in.readValue(CL),
      (Double) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(x);
    dest.writeValue(y);
    dest.writeValue(z);

  }

  @Override public int describeContents() {
    return 0;
  }

}

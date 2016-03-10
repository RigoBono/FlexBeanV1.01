package com.punchthrough.bean.sdk.upload;


final class AutoParcel_FirmwareBundle extends FirmwareBundle {
  private final FirmwareImage imageA;
  private final FirmwareImage imageB;

  AutoParcel_FirmwareBundle(
      FirmwareImage imageA,
      FirmwareImage imageB) {
    if (imageA == null) {
      throw new NullPointerException("Null imageA");
    }
    this.imageA = imageA;
    if (imageB == null) {
      throw new NullPointerException("Null imageB");
    }
    this.imageB = imageB;
  }

  @Override
  public FirmwareImage imageA() {
    return imageA;
  }

  @Override
  public FirmwareImage imageB() {
    return imageB;
  }

  @Override
  public String toString() {
    return "FirmwareBundle{"
        + "imageA=" + imageA
        + ", imageB=" + imageB
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof FirmwareBundle) {
      FirmwareBundle that = (FirmwareBundle) o;
      return (this.imageA.equals(that.imageA()))
          && (this.imageB.equals(that.imageB()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= imageA.hashCode();
    h *= 1000003;
    h ^= imageB.hashCode();
    return h;
  }



  public static final android.os.Parcelable.Creator<FirmwareBundle> CREATOR = new android.os.Parcelable.Creator<FirmwareBundle>() {
    @Override public FirmwareBundle createFromParcel(android.os.Parcel in) {
      return new AutoParcel_FirmwareBundle(in);
    }
    @Override public FirmwareBundle[] newArray(int size) {
      return new FirmwareBundle[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_FirmwareBundle.class.getClassLoader();

  private AutoParcel_FirmwareBundle(android.os.Parcel in) {
    this(
      (FirmwareImage) in.readValue(CL),
      (FirmwareImage) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(imageA);
    dest.writeValue(imageB);

  }

  @Override public int describeContents() {
    return 0;
  }

}

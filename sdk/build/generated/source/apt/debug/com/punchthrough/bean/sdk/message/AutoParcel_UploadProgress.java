package com.punchthrough.bean.sdk.message;


final class AutoParcel_UploadProgress extends UploadProgress {
  private final int blocksSent;
  private final int totalBlocks;

  AutoParcel_UploadProgress(
      int blocksSent,
      int totalBlocks) {
    this.blocksSent = blocksSent;
    this.totalBlocks = totalBlocks;
  }

  @Override
  public int blocksSent() {
    return blocksSent;
  }

  @Override
  public int totalBlocks() {
    return totalBlocks;
  }

  @Override
  public String toString() {
    return "UploadProgress{"
        + "blocksSent=" + blocksSent
        + ", totalBlocks=" + totalBlocks
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof UploadProgress) {
      UploadProgress that = (UploadProgress) o;
      return (this.blocksSent == that.blocksSent())
          && (this.totalBlocks == that.totalBlocks());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= blocksSent;
    h *= 1000003;
    h ^= totalBlocks;
    return h;
  }



  public static final android.os.Parcelable.Creator<UploadProgress> CREATOR = new android.os.Parcelable.Creator<UploadProgress>() {
    @Override public UploadProgress createFromParcel(android.os.Parcel in) {
      return new AutoParcel_UploadProgress(in);
    }
    @Override public UploadProgress[] newArray(int size) {
      return new UploadProgress[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_UploadProgress.class.getClassLoader();

  private AutoParcel_UploadProgress(android.os.Parcel in) {
    this(
      (Integer) in.readValue(CL),
      (Integer) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(blocksSent);
    dest.writeValue(totalBlocks);

  }

  @Override public int describeContents() {
    return 0;
  }

}

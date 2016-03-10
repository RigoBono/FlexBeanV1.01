package com.punchthrough.bean.sdk.message;

import com.punchthrough.bean.sdk.internal.upload.sketch.BeanState;
import com.punchthrough.bean.sdk.internal.upload.sketch.BeanSubstate;

final class AutoParcel_Status extends Status {
  private final BeanState beanState;
  private final BeanSubstate beanSubstate;
  private final int blocksSent;
  private final int bytesSent;

  AutoParcel_Status(
      BeanState beanState,
      BeanSubstate beanSubstate,
      int blocksSent,
      int bytesSent) {
    if (beanState == null) {
      throw new NullPointerException("Null beanState");
    }
    this.beanState = beanState;
    if (beanSubstate == null) {
      throw new NullPointerException("Null beanSubstate");
    }
    this.beanSubstate = beanSubstate;
    this.blocksSent = blocksSent;
    this.bytesSent = bytesSent;
  }

  @Override
  public BeanState beanState() {
    return beanState;
  }

  @Override
  public BeanSubstate beanSubstate() {
    return beanSubstate;
  }

  @Override
  public int blocksSent() {
    return blocksSent;
  }

  @Override
  public int bytesSent() {
    return bytesSent;
  }

  @Override
  public String toString() {
    return "Status{"
        + "beanState=" + beanState
        + ", beanSubstate=" + beanSubstate
        + ", blocksSent=" + blocksSent
        + ", bytesSent=" + bytesSent
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Status) {
      Status that = (Status) o;
      return (this.beanState.equals(that.beanState()))
          && (this.beanSubstate.equals(that.beanSubstate()))
          && (this.blocksSent == that.blocksSent())
          && (this.bytesSent == that.bytesSent());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= beanState.hashCode();
    h *= 1000003;
    h ^= beanSubstate.hashCode();
    h *= 1000003;
    h ^= blocksSent;
    h *= 1000003;
    h ^= bytesSent;
    return h;
  }



  public static final android.os.Parcelable.Creator<Status> CREATOR = new android.os.Parcelable.Creator<Status>() {
    @Override public Status createFromParcel(android.os.Parcel in) {
      return new AutoParcel_Status(in);
    }
    @Override public Status[] newArray(int size) {
      return new Status[size];
    }
  };

  private final static java.lang.ClassLoader CL = AutoParcel_Status.class.getClassLoader();

  private AutoParcel_Status(android.os.Parcel in) {
    this(
      (BeanState) in.readValue(CL),
      (BeanSubstate) in.readValue(CL),
      (Integer) in.readValue(CL),
      (Integer) in.readValue(CL));
  }

  @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(beanState);
    dest.writeValue(beanSubstate);
    dest.writeValue(blocksSent);
    dest.writeValue(bytesSent);

  }

  @Override public int describeContents() {
    return 0;
  }

}

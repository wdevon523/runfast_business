package com.gxuc.runfast.business.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 活动
 * Created by Berial on 2017/9/1.
 */
public class Activity implements Parcelable {

    public long id;
    public String name;
    public String startTime;
    public String endTime;
    public String statusName;
    public int status;
    public int type;
    public String describe;

    public String full;
    public String less;
    public String discount;
    public String disprice;
    public String freebie;
    public String typeName;
    public String goodsIds;
    public String standardIds;

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.statusName);
        dest.writeInt(this.status);
        dest.writeInt(this.type);
        dest.writeString(this.describe);
        dest.writeString(this.full);
        dest.writeString(this.less);
        dest.writeString(this.discount);
        dest.writeString(this.disprice);
        dest.writeString(this.freebie);
        dest.writeString(this.typeName);
        dest.writeString(this.goodsIds);
        dest.writeString(this.standardIds);
    }

    public Activity() {}

    protected Activity(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.statusName = in.readString();
        this.status = in.readInt();
        this.type = in.readInt();
        this.describe = in.readString();
        this.full = in.readString();
        this.less = in.readString();
        this.discount = in.readString();
        this.disprice = in.readString();
        this.freebie = in.readString();
        this.typeName = in.readString();
        this.goodsIds = in.readString();
        this.standardIds = in.readString();
    }

    public static final Parcelable.Creator<Activity> CREATOR = new Parcelable.Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel source) {return new Activity(source);}

        @Override
        public Activity[] newArray(int size) {return new Activity[size];}
    };
}

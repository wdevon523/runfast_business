package com.gxuc.runfast.business.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类
 * Created by Berial on 2017/8/28.
 */
public class GoodsSort implements Parcelable {

    public long id;
    public String describe;
    public String name;
    public boolean selected;
    public List<Goods> goods;

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.describe);
        dest.writeString(this.name);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeList(this.goods);
    }

    public GoodsSort() {}

    protected GoodsSort(Parcel in) {
        this.id = in.readLong();
        this.describe = in.readString();
        this.name = in.readString();
        this.selected = in.readByte() != 0;
        this.goods = new ArrayList<>();
        in.readList(this.goods, Goods.class.getClassLoader());
    }

    public static final Parcelable.Creator<GoodsSort> CREATOR = new Parcelable.Creator<GoodsSort>() {
        @Override
        public GoodsSort createFromParcel(Parcel source) {return new GoodsSort(source);}

        @Override
        public GoodsSort[] newArray(int size) {return new GoodsSort[size];}
    };
}

package com.ariaramin.crypto.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DataItem implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("cmc_rank")
    private int cmcRank;

    @SerializedName("marketPairCount")
    private int numMarketPairs;

    @SerializedName("circulatingSupply")
    private double circulatingSupply;

    @SerializedName("totalSupply")
    private Number totalSupply;

    @SerializedName("max_supply")
    private double maxSupply;

    @SerializedName("ath")
    private double ath;

    @SerializedName("atl")
    private double atl;

    @SerializedName("high24h")
    private double high24h;

    @SerializedName("low24h")
    private double low24h;

    @SerializedName("isActive")
    private int isActive;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("dateAdded")
    private String dateAdded;

    @SerializedName("quotes")
    private List<USD> listQuote;

    @SerializedName("slug")
    private String slug;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public int getCmcRank() {
        return cmcRank;
    }

    public int getNumMarketPairs() {
        return numMarketPairs;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public Number getTotalSupply() {
        return totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public double getAth() {
        return ath;
    }

    public double getAtl() {
        return atl;
    }

    public double getHigh24h() {
        return high24h;
    }

    public double getLow24h() {
        return low24h;
    }

    public int getIsActive() {
        return isActive;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public List<USD> getListQuote() {
        return listQuote;
    }

    public String getSlug() {
        return slug;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.symbol);
        dest.writeString(this.lastUpdated);
        dest.writeInt(this.cmcRank);
        dest.writeInt(this.numMarketPairs);
        dest.writeDouble(this.circulatingSupply);
        dest.writeSerializable(this.totalSupply);
        dest.writeDouble(this.maxSupply);
        dest.writeDouble(this.ath);
        dest.writeDouble(this.atl);
        dest.writeDouble(this.high24h);
        dest.writeDouble(this.low24h);
        dest.writeInt(this.isActive);
        dest.writeStringList(this.tags);
        dest.writeString(this.dateAdded);
        dest.writeList(this.listQuote);
        dest.writeString(this.slug);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.symbol = source.readString();
        this.lastUpdated = source.readString();
        this.cmcRank = source.readInt();
        this.numMarketPairs = source.readInt();
        this.circulatingSupply = source.readDouble();
        this.totalSupply = (Number) source.readSerializable();
        this.maxSupply = source.readDouble();
        this.ath = source.readDouble();
        this.atl = source.readDouble();
        this.high24h = source.readDouble();
        this.low24h = source.readDouble();
        this.isActive = source.readInt();
        this.tags = source.createStringArrayList();
        this.dateAdded = source.readString();
        this.listQuote = new ArrayList<USD>();
        source.readList(this.listQuote, USD.class.getClassLoader());
        this.slug = source.readString();
    }

    public DataItem() {
    }

    protected DataItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.symbol = in.readString();
        this.lastUpdated = in.readString();
        this.cmcRank = in.readInt();
        this.numMarketPairs = in.readInt();
        this.circulatingSupply = in.readDouble();
        this.totalSupply = (Number) in.readSerializable();
        this.maxSupply = in.readDouble();
        this.ath = in.readDouble();
        this.atl = in.readDouble();
        this.high24h = in.readDouble();
        this.low24h = in.readDouble();
        this.isActive = in.readInt();
        this.tags = in.createStringArrayList();
        this.dateAdded = in.readString();
        this.listQuote = new ArrayList<USD>();
        in.readList(this.listQuote, USD.class.getClassLoader());
        this.slug = in.readString();
    }

    public static final Parcelable.Creator<DataItem> CREATOR = new Parcelable.Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel source) {
            return new DataItem(source);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}

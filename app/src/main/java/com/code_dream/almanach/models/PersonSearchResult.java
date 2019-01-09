package com.code_dream.almanach.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 9/19/17.
 */

public class PersonSearchResult implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private int id;

    public PersonSearchResult(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(getName());
        dest.writeInt(getId());
    }

    private PersonSearchResult(Parcel in) {
        setName(in.readString());
        setId(in.readInt());
    }

    public static final Parcelable.Creator<PersonSearchResult> CREATOR = new Parcelable.Creator<PersonSearchResult>() {

        @Override
        public PersonSearchResult createFromParcel(Parcel in) {
            return new PersonSearchResult(in);
        }

        @Override
        public PersonSearchResult[] newArray(int size) {
            return new PersonSearchResult[size];
        }
    };
}

package com.code_dream.almanach.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 9/21/17.
 */

public class Person implements Parcelable {

    public enum FriendType {
        @SerializedName("friends")
        FRIENDS,
        @SerializedName("not_friends")
        NOT_FRIENDS,
        @SerializedName("request_sent")
        FRIEND_REQUEST_SENT,
        @SerializedName("request_received")
        FRIEND_REQUEST_RECEIVED
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("school_name")
    @Expose
    private String schoolName;

    @SerializedName("friend_type")
    @Expose
    private FriendType friendType;

    @SerializedName("id")
    @Expose
    private int id;

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, int id){
        this.name = name;
        this.id = id;
    }

    public Person(String name, String schoolName, FriendType friendType, int id) {
        this.name = name;
        this.schoolName = schoolName;
        this.friendType = friendType;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public FriendType getFriendType() {
        return friendType;
    }

    public void setFriendType(FriendType friendType) {
        this.friendType = friendType;
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
        dest.writeString(name);
        dest.writeString(schoolName);
        dest.writeString(friendType.name());
        dest.writeInt(id);
    }

    private Person(Parcel in){
        setName(in.readString());
        setSchoolName(in.readString());
        setFriendType(FriendType.valueOf(in.readString()));
        setId(in.readInt());
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {

        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}

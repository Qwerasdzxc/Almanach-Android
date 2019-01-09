package com.code_dream.almanach.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 9/22/17.
 */

public class ChatRoom implements Parcelable {

    @SerializedName("person")
    @Expose
    private Person person;

    @SerializedName("id")
    @Expose
    private int chatRoomId;

    public ChatRoom(Person person, int chatRoomId) {
        this.person = person;
        this.chatRoomId = chatRoomId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeParcelable(person, i);
        dest.writeInt(chatRoomId);
    }

    private ChatRoom(Parcel in){
        person = in.readParcelable(Person.class.getClassLoader());
        setChatRoomId(in.readInt());
    }

    public static final Parcelable.Creator<ChatRoom> CREATOR = new Parcelable.Creator<ChatRoom>() {

        @Override
        public ChatRoom createFromParcel(Parcel in) {
            return new ChatRoom(in);
        }

        @Override
        public ChatRoom[] newArray(int size) {
            return new ChatRoom[size];
        }
    };
}

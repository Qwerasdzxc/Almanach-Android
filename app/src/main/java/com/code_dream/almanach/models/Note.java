package com.code_dream.almanach.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.io.Serializable;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

// Nitrite Database indices
@Indices({
        @Index(value = "title", type = IndexType.NonUnique),
        @Index(value = "description", type = IndexType.NonUnique),
        @Index(value = "id", type = IndexType.Unique),
})
public class Note implements Parcelable, Serializable {

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("content")
    private String description;

    @Expose
    @SerializedName("id")
    @Id
    private int id;

    @Nullable
    private Subject subject;

    public Note() {
        // Default parameter-less constructor for Nitrite.
    }

    public Note(String title, String description, @Nullable Subject subject) {

        this.title = title;
        this.description = description;
        this.subject = subject;
    }

    public Note(String title, String description, @Nullable Subject subject, int id) {

        this.title = title;
        this.description = description;
        this.subject = subject;
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(@Nullable Subject subject) {
        this.subject = subject;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(id);
    }

    private Note(Parcel in){
        setTitle(in.readString());
        setDescription(in.readString());
        setId(in.readInt());
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}

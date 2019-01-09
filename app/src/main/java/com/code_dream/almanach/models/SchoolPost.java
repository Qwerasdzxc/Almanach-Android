package com.code_dream.almanach.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Qwerasdzxc on 2/14/17.
 */

public class SchoolPost implements Parcelable {

    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("image")
    @Expose
    private String imageUrl;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("reaction_count")
    @Expose
    private Integer reactionCount;
    @SerializedName("owner")
    @Expose
    private Boolean owner;
    @SerializedName("post_upvoted")
    @Expose
    private Boolean postUpvoted;
    @SerializedName("post_downvoted")
    @Expose
    private Boolean postDownvoted;

    private boolean[] parcelableBooleanArray = new boolean[ 3 ];

    public SchoolPost(String postDate, String content, String user, String school, int id, int reactionCount, boolean owner, boolean postUpvoted, boolean postDownvoted){
        this.postDate = postDate;
        this.content = content;
        this.user = user;
        this.school = school;
        this.id = id;
        this.reactionCount = reactionCount;
        this.owner = owner;
        this.postUpvoted = postUpvoted;
        this.postDownvoted = postDownvoted;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReactionCount() {
        return reactionCount;
    }

    public void setReactionCount(Integer reactionCount) {
        this.reactionCount = reactionCount;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public Boolean isUpvoted() {
        return postUpvoted;
    }

    public void setPostUpvoted(Boolean postUpvoted) {
        if (postUpvoted)
            setReactionCount(getReactionCount() + 1);
        else
            setReactionCount(getReactionCount() - 1);

        this.postUpvoted = postUpvoted;
    }

    public Boolean isDownvoted() {
        return postDownvoted;
    }

    public void setPostDownvoted(Boolean postDownvoted) {
        if (postDownvoted)
            setReactionCount(getReactionCount() - 1);
        else
            setReactionCount(getReactionCount() + 1);

        this.postDownvoted = postDownvoted;
    }

    private void parcelSetPostUpvoted(boolean postUpvoted){
        this.postUpvoted = postUpvoted;
    }

    private void parcelSetPostDownvoted(boolean postDownvoted){
        this.postDownvoted = postDownvoted;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getPostDate());
        dest.writeString(getContent());
        dest.writeString(getUser());
        dest.writeString(getSchool());
        dest.writeString(getImageUrl());
        dest.writeInt(getId());
        dest.writeInt(getReactionCount());
        dest.writeBooleanArray(new boolean[]{ owner, postUpvoted, postDownvoted });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private SchoolPost(Parcel in){
        setPostDate(in.readString());
        setContent(in.readString());
        setUser(in.readString());
        setSchool(in.readString());
        setImageUrl(in.readString());
        setId(in.readInt());
        setReactionCount(in.readInt());

        in.readBooleanArray(parcelableBooleanArray);

        setOwner(parcelableBooleanArray[0]);

        parcelSetPostUpvoted(parcelableBooleanArray[1]);
        parcelSetPostDownvoted(parcelableBooleanArray[2]);
    }

    public static final Parcelable.Creator<SchoolPost> CREATOR = new Parcelable.Creator<SchoolPost>() {

        @Override
        public SchoolPost createFromParcel(Parcel in) {
            return new SchoolPost(in);
        }

        @Override
        public SchoolPost[] newArray(int size) {
            return new SchoolPost[size];
        }
    };
}

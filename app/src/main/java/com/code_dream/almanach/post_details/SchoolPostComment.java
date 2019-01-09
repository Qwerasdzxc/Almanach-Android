package com.code_dream.almanach.post_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Qwerasdzxc on 3/13/17.
 */

public class SchoolPostComment {

    @SerializedName("comment_date")
    @Expose
    private String commentDate;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("id")
    @Expose
    private Integer commentId;
    @SerializedName("reaction_count")
    @Expose
    private Integer reactionCount;
    @SerializedName("comment_owner")
    @Expose
    private Boolean commentOwner;
    @SerializedName("comment_upvoted")
    @Expose
    private Boolean commentUpvoted;
    @SerializedName("comment_downvoted")
    @Expose
    private Boolean commentDownvoted;

    public SchoolPostComment(String commentDate, String content, String user, int commentId, int reactionCount, boolean commentOwner, boolean commentUpvoted, boolean commentDownvoted){
        this.commentDate = commentDate;
        this.content = content;
        this.user = user;
        this.commentId = commentId;
        this.reactionCount = reactionCount;
        this.commentOwner = commentOwner;
        this.commentUpvoted = commentUpvoted;
        this.commentDownvoted = commentDownvoted;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String postDate) {
        this.commentDate = commentDate;
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

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getReactionCount() {
        return reactionCount;
    }

    public void setReactionCount(Integer reactionCount) {
        this.reactionCount = reactionCount;
    }

    public Boolean isCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(Boolean commentOwner) {
        this.commentOwner = commentOwner;
    }

    public Boolean isUpvoted() {
        return commentUpvoted;
    }

    public void setCommentUpvoted(Boolean commentUpvoted) {
        if (commentUpvoted)
            setReactionCount(getReactionCount() + 1);
        else
            setReactionCount(getReactionCount() - 1);

        this.commentUpvoted = commentUpvoted;
    }

    public Boolean isDownvoted() {
        return commentDownvoted;
    }

    public void setCommentDownvoted(Boolean commentDownvoted) {
        if (commentDownvoted)
            setReactionCount(getReactionCount() - 1);
        else
            setReactionCount(getReactionCount() + 1);

        this.commentDownvoted = commentDownvoted;
    }
}

package com.xidian.xienong.model;

import java.io.Serializable;

/**
 * Created by koumiaojuan on 2017/7/6.
 */

public class CommodityComment implements Serializable{
    private String commentId;
    private String commentTime;
    private String commentContent;
    private float commentLevel;
    private User user;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public float getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(float commentLevel) {
        this.commentLevel = commentLevel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

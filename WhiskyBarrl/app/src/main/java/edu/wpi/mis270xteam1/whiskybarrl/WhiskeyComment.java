package edu.wpi.mis270xteam1.whiskybarrl;

/**
 * Created by Anthony J. Ruffa on 2/24/2016.
 */
public class WhiskeyComment {
    private int id;
    private String commentText;
    private int userId;
    private int whiskeyId;

    public WhiskeyComment() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWhiskeyId() {
        return whiskeyId;
    }

    public void setWhiskeyId(int whiskeyId) {
        this.whiskeyId = whiskeyId;
    }
}

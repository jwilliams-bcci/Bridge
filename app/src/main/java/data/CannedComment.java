package data;

import androidx.annotation.NonNull;

public class CannedComment {
    private int mCannedCommentId;
    private String mCannedCommentText;

    public CannedComment(int cannedCommentId, String cannedCommentText) {
        mCannedCommentId = cannedCommentId;
        mCannedCommentText = cannedCommentText;
    }

    public int getCannedCommentId() {
        return mCannedCommentId;
    }
    public void setCannedCommentId(int cannedCommentId) {
        mCannedCommentId = cannedCommentId;
    }

    public String getCannedCommentText() {
        return mCannedCommentText;
    }
    public void setCannedCommentText(String cannedCommentText) {
        mCannedCommentText = cannedCommentText;
    }

    @NonNull
    @Override
    public String toString() {
        return mCannedCommentText;
    }
}

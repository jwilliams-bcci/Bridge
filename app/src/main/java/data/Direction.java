package data;

import androidx.annotation.NonNull;

public class Direction {
    private int mDirectionId;
    private String mDirection;

    public Direction(int directionId, String direction) {
        mDirectionId = directionId;
        mDirection = direction;
    }

    public int getDirectionId() {
        return mDirectionId;
    }
    public void setDirectionId(int directionId) {
        mDirectionId = directionId;
    }

    public String getDirection() {
        return mDirection;
    }
    public void setDirection(String direction) {
        mDirection = direction;
    }

    @NonNull
    @Override
    public String toString() {
        return mDirection;
    }
}

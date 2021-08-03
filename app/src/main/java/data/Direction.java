package data;

import androidx.annotation.NonNull;

public class Direction {
    private int mDirectionId;
    private String mDirection;
    private int mOrder;

    public Direction(int directionId, String direction, int order) {
        mDirectionId = directionId;
        mDirection = direction;
        mOrder = order;
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

    public int getOrder() { return mOrder; }
    public void setOrder(int order) { mOrder = order; }

    @NonNull
    @Override
    public String toString() {
        return mDirection;
    }
}

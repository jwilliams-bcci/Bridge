package data;

import androidx.annotation.NonNull;

public class Room {
    private int mRoomId;
    private String mRoomName;

    public Room(int roomId, String roomName) {
        mRoomId = roomId;
        mRoomName = roomName;
    }

    public int getRoomId() {
        return mRoomId;
    }
    public void setRoomId(int roomId) {
        mRoomId = roomId;
    }

    public String getRoomName() {
        return mRoomName;
    }
    public void setRoomName(String roomName) {
        mRoomName = roomName;
    }

    @NonNull
    @Override
    public String toString() {
        return mRoomName;
    }
}

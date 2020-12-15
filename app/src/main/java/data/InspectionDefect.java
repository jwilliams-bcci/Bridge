package data;

public class InspectionDefect {
    private int mInspectionDefectId;
    private int mInspectionId;
    private int mDefectItemId;
    private int mDefectStatusId;
    private String mLocation;
    private String mRoom;
    private String mDirection;
    private String mCannedComment;
    private String mNote;

    public InspectionDefect (int inspectionDefectId, int inspectionId, int defectItemId, int defectStatusId, String location, String room, String direction, String cannedComment, String note) {
        mInspectionDefectId = inspectionDefectId;
        mInspectionId = inspectionId;
        mDefectItemId = defectItemId;
        mDefectStatusId = defectStatusId;
        mLocation = location;
        mRoom = room;
        mDirection = direction;
        mCannedComment = cannedComment;
        mNote = note;
    }

    public int getInspectionDefectId() {
        return mInspectionDefectId;
    }
    public void setInspectionDefectId(int inspectionDefectId) {
        mInspectionDefectId = inspectionDefectId;
    }

    public int getInspectionId() {
        return mInspectionId;
    }
    public void setInspectionId(int inspectionId) {
        mInspectionId = inspectionId;
    }

    public int getDefectItemId() {
        return mDefectItemId;
    }
    public void setDefectItemId(int defectItemId) {
        mDefectItemId = defectItemId;
    }

    public int getDefectStatusId() {
        return mDefectStatusId;
    }
    public void setDefectStatusId(int defectStatusId) {
        mDefectStatusId = defectStatusId;
    }

    public String getLocation() {
        return mLocation;
    }
    public void setLocation(String location) {
        mLocation = location;
    }

    public String getRoom() {
        return mRoom;
    }
    public void setRoom(String room) {
        mRoom = room;
    }

    public String getDirection() {
        return mDirection;
    }
    public void setDirection(String direction) {
        mDirection = direction;
    }

    public String getCannedComment() {
        return mCannedComment;
    }
    public void setCannedComment(String cannedComment) {
        mCannedComment = cannedComment;
    }

    public String getNote() {
        return mNote;
    }
    public void setNote(String note) {
        mNote = note;
    }
}

package data;

import android.os.Parcel;
import android.os.Parcelable;

public class Inspection implements Parcelable {
    private int mInspectionId;
    private int mBuilderId;
    private String mSuperintendent;
    private int mLocationId;
    private String mInspectionType;
    private String mNotes;
    private boolean mIsComplete;

    public Inspection(int inspectionId, int builderId, String superintendent, int locationId, String inspectionType, String notes){
        mInspectionId = inspectionId;
        mBuilderId = builderId;
        mSuperintendent = superintendent;
        mLocationId = locationId;
        mInspectionType = inspectionType;
        mNotes = notes;
        mIsComplete = false;
    }

    protected Inspection(Parcel in) {
        mInspectionId = in.readInt();
        mBuilderId = in.readInt();
        mSuperintendent = in.readString();
        mLocationId = in.readInt();
        mInspectionType = in.readString();
        mNotes = in.readString();
    }

    public int getInspectionId() {
        return mInspectionId;
    }
    public void setInspectionId(int inspectionId) {
        mInspectionId = inspectionId;
    }
    public int getBuilderId() {
        return mBuilderId;
    }
    public void setBuilderId(int builderId) {
        mBuilderId = builderId;
    }
    public String getSuperintendent() {
        return mSuperintendent;
    }
    public void setSuperintendent(String superintendent) {
        mSuperintendent = superintendent;
    }
    public int getLocationId() {
        return mLocationId;
    }
    public void setAddress(int locationId) {
        mLocationId = locationId;
    }
    public String getInspectionType() {
        return mInspectionType;
    }
    public void setInspectionType(String inspectionType) {
        mInspectionType = inspectionType;
    }
    public String getNotes() {
        return mNotes;
    }
    public void setNotes(String notes) {
        mNotes = notes;
    }
    public boolean getIsComplete() { return mIsComplete; }
    public void setIsComplete(boolean isComplete) { mIsComplete = isComplete; }

    public static final Creator<Inspection> CREATOR = new Creator<Inspection>() {
        @Override
        public Inspection createFromParcel(Parcel in) {
            return new Inspection(in);
        }

        @Override
        public Inspection[] newArray(int size) {
            return new Inspection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mInspectionId);
        parcel.writeInt(mBuilderId);
        parcel.writeString(mSuperintendent);
        parcel.writeInt(mLocationId);
        parcel.writeString(mInspectionType);
        parcel.writeString(mNotes);
    }
}

package com.example.bridge;

import android.os.Parcel;
import android.os.Parcelable;

public class Inspection implements Parcelable {
    private int mInspectionId;
    private String mCommunity;
    private String mAddress;
    private String mInspectionType;
    private String mNotes;

    public Inspection(int inspectionId, String community, String address, String inspectionType, String notes){
        mInspectionId = inspectionId;
        mCommunity = community;
        mAddress = address;
        mInspectionType = inspectionType;
        mNotes = notes;
    }

    protected Inspection(Parcel in) {
        mInspectionId = in.readInt();
        mCommunity = in.readString();
        mAddress = in.readString();
        mInspectionType = in.readString();
        mNotes = in.readString();
    }

    public int getInspectionId() {
        return mInspectionId;
    }
    public void setInspectionId(int inspectionId) {
        mInspectionId = inspectionId;
    }
    public String getCommunity() {
        return mCommunity;
    }
    public void setCommunity(String community) {
        mCommunity = community;
    }
    public String getAddress() {
        return mAddress;
    }
    public void setAddress(String address) {
        mAddress = address;
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
        parcel.writeString(mCommunity);
        parcel.writeString(mAddress);
        parcel.writeString(mInspectionType);
        parcel.writeString(mNotes);
    }
}

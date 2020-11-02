package com.example.bridge;

import android.os.Parcel;
import android.os.Parcelable;

public class InspectionHistory implements Parcelable {
    private int mInspectionHistoryId;
    private int mInspectionId;
    private int mInspectorId;
    private String mInspectionStatus;

    protected InspectionHistory(Parcel in) {
    }

    public static final Creator<InspectionHistory> CREATOR = new Creator<InspectionHistory>() {
        @Override
        public InspectionHistory createFromParcel(Parcel in) {
            return new InspectionHistory(in);
        }

        @Override
        public InspectionHistory[] newArray(int size) {
            return new InspectionHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}

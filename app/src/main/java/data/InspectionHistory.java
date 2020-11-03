package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class InspectionHistory implements Parcelable {
    private int mInspectionHistoryId;
    private int mLocationId;
    private Date mInspectionDate;
    private String mInspectionType;
    private String mInspectorName;
    private String mInspectionStatus;

    public InspectionHistory(int inspectionHistoryId, int locationId, Date inspectionDate, String inspectionType, String inspectorName, String inspectionStatus) {
        mInspectionHistoryId = inspectionHistoryId;
        mLocationId = locationId;
        mInspectionDate = inspectionDate;
        mInspectionType = inspectionType;
        mInspectorName = inspectorName;
        mInspectionStatus = inspectionStatus;
    }

    public int getInspectionHistoryId() {
        return mInspectionHistoryId;
    }
    public void setInspectionHistoryId(int inspectionHistoryId) {
        mInspectionHistoryId = inspectionHistoryId;
    }
    public int getLocationId() {
        return mLocationId;
    }
    public void setLocationId(int locationId) {
        mLocationId = locationId;
    }
    public Date getInspectionDate() {
        return mInspectionDate;
    }
    public void setInspectionDate(Date inspectionDate) {
        mInspectionDate = inspectionDate;
    }
    public String getInspectionType() {
        return mInspectionType;
    }
    public void setInspectionType(String inspectionType) {
        mInspectionType = inspectionType;
    }
    public String getInspectorName() {
        return mInspectorName;
    }
    public void setInspectorName(String inspectorName) {
        mInspectorName = inspectorName;
    }
    public String getInspectionStatus() {
        return mInspectionStatus;
    }
    public void setInspectionStatus(String inspectionStatus) {
        mInspectionStatus = inspectionStatus;
    }

    public String getFullInspectionDetails() {
        return mInspectionType + "\n" + mInspectorName + "\n" + mInspectionStatus;
    }

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

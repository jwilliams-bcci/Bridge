package data;

import androidx.annotation.NonNull;

public class InspectionResolution {
    private int mInspectionResolutionId;
    private String mInspectionResolutionTitle;

    public InspectionResolution(int inspectionResolutionId, String inspectionResolutionTitle) {
        mInspectionResolutionId = inspectionResolutionId;
        mInspectionResolutionTitle = inspectionResolutionTitle;
    }

    public int getInspectionResolutionId() {
        return mInspectionResolutionId;
    }
    public void setInspectionResolutionId(int inspectionResolutionId) {
        mInspectionResolutionId = inspectionResolutionId;
    }

    public String getInspectionResolutionTitle() {
        return mInspectionResolutionTitle;
    }
    public void setInspectionResolutionTitle(String inspectionResolutionTitle) {
        mInspectionResolutionTitle = inspectionResolutionTitle;
    }

    @NonNull
    @Override
    public String toString() {
        return mInspectionResolutionTitle;
    }
}

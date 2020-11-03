package data;

import androidx.annotation.NonNull;

public class Inspector {
    private int mInspectorId;
    private String mInspectorName;

    public Inspector(int inspectorId, String inspectorName) {
        mInspectorId = inspectorId;
        mInspectorName = inspectorName;
    }

    public int getInspectorId() {
        return mInspectorId;
    }
    public void setInspectorId(int inspectorId) {
        mInspectorId = inspectorId;
    }
    public String getInspectorName() {
        return mInspectorName;
    }
    public void setInspectorName(String inspectorName) {
        mInspectorName = inspectorName;
    }

    @NonNull
    @Override
    public String toString() {
        return mInspectorName;
    }
}

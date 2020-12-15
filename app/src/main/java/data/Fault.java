package data;

import androidx.annotation.NonNull;

public class Fault {
    private int mFaultId;
    private String mFault;

    public Fault(int faultId, String fault) {
        mFaultId = faultId;
        mFault = fault;
    }

    public int getFaultId() {
        return mFaultId;
    }
    public void setFaultId(int faultId) {
        mFaultId = faultId;
    }

    public String getFault() {
        return mFault;
    }
    public void setFault(String fault) {
        mFault = fault;
    }

    @NonNull
    @Override
    public String toString() {
        return mFault;
    }
}

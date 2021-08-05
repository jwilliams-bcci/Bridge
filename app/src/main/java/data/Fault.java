package data;

import androidx.annotation.NonNull;

public class Fault {
    private int mFaultId;
    private String mFault;
    private String mFaultPrint;

    public Fault(int faultId, String fault, String faultPrint) {
        mFaultId = faultId;
        mFault = fault;
        mFaultPrint = faultPrint;
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

    public String getFaultPrint() { return mFaultPrint; }
    public void setFaultPrint(String faultPrint) { mFaultPrint = faultPrint; }

    @NonNull
    @Override
    public String toString() {
        return mFault;
    }
}

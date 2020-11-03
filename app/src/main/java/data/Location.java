package data;

public class Location {
    private int mLocationId;
    private String mAddress;
    private int mZipCode;
    private String mCity;
    private String mState;
    private String mCommunity;

    public Location(int locationId, String address, int zipCode, String city, String state, String community) {
        mLocationId = locationId;
        mAddress = address;
        mZipCode = zipCode;
        mCity = city;
        mState = state;
        mCommunity = community;
    }

    public int getLocationId() {
        return mLocationId;
    }
    public void setLocationId(int locationId) {
        mLocationId = locationId;
    }
    public String getAddress() {
        return mAddress;
    }
    public void setAddress(String address) {
        mAddress = address;
    }
    public int getZipCode() {
        return mZipCode;
    }
    public void setZipCode(int zipCode) {
        mZipCode = zipCode;
    }
    public String getCity() {
        return mCity;
    }
    public void setCity(String city) {
        mCity = city;
    }
    public String getState() {
        return mState;
    }
    public void setState(String state) {
        mState = state;
    }
    public String getCommunity() {
        return mCommunity;
    }
    public void setCommunity(String community) {
        mCommunity = community;
    }
    public String getFullAddress() {
        return mAddress + ", " + mCity + ", " + mState + " " + mZipCode;
    }
}

import android.os.Parcel;
import android.os.Parcelable;

public class Builder implements Parcelable {
    private int mBuilderId;
    private String mBuilderName;

    public Builder(int builderId, String builderName) {
        mBuilderId = builderId;
        mBuilderName = builderName;
    }

    protected Builder(Parcel in) {
        mBuilderId = in.readInt();
        mBuilderName = in.readString();
    }

    public int getBuilderId() {
        return mBuilderId;
    }
    public void setBuilderId(int builderId) {
        mBuilderId = builderId;
    }
    public String getBuilderName() {
        return mBuilderName;
    }
    public void setBuilderName(String builderName) {
        mBuilderName = builderName;
    }

    public static final Creator<Builder> CREATOR = new Creator<Builder>() {
        @Override
        public Builder createFromParcel(Parcel in) {
            return new Builder(in);
        }

        @Override
        public Builder[] newArray(int size) {
            return new Builder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mBuilderId);
        parcel.writeString(mBuilderName);
    }
}

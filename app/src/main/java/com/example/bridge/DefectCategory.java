package com.example.bridge;

import android.os.Parcel;
import android.os.Parcelable;

public final class DefectCategory implements Parcelable {
    private int mDefectCategoryId;
    private String mCategoryName;

    public DefectCategory(int defectCategoryId, String categoryName) {
        mDefectCategoryId = defectCategoryId;
        mCategoryName = categoryName;
    }

    protected DefectCategory(Parcel in) {
        mDefectCategoryId = in.readInt();
        mCategoryName = in.readString();
    }

    public int getDefectCategoryId() {
        return mDefectCategoryId;
    }
    public void setDefectCategoryId(int defectCategoryId) {
        mDefectCategoryId = defectCategoryId;
    }
    public String getCategoryName() {
        return mCategoryName;
    }
    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public static final Creator<DefectCategory> CREATOR = new Creator<DefectCategory>() {
        @Override
        public DefectCategory createFromParcel(Parcel in) {
            return new DefectCategory(in);
        }

        @Override
        public DefectCategory[] newArray(int size) {
            return new DefectCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mDefectCategoryId);
        parcel.writeString(mCategoryName);
    }
}

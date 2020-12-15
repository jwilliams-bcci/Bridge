package data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public final class DefectCategory {
    private int mDefectCategoryId;
    private String mCategoryName;

    public DefectCategory(int defectCategoryId, String categoryName) {
        mDefectCategoryId = defectCategoryId;
        mCategoryName = categoryName;
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

    @NonNull
    @Override
    public String toString() {
        return mCategoryName;
    }
}

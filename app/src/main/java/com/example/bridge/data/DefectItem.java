package com.example.bridge.data;

public final class DefectItem {
    private final int mDefectItemId;
    private final int mDefectCategoryId;
    private final String mItemDescription;

    public DefectItem(int defectItemId, int defectCategoryId, String itemDescription) {
        mDefectItemId = defectItemId;
        mDefectCategoryId = defectCategoryId;
        mItemDescription = itemDescription;
    }
}

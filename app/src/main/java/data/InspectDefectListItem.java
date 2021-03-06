package data;

public class InspectDefectListItem {
    private int mDefectItemId;
    private int mDefectCategoryId;
    private int mItemNumber;
    private String mItemDescription;

    public InspectDefectListItem() {
    }

    public int getDefectItemId() {
        return mDefectItemId;
    }
    public void setDefectItemId(int defectItemId) {
        mDefectItemId = defectItemId;
    }

    public int getDefectCategoryId() {
        return mDefectCategoryId;
    }
    public void setDefectCategoryId(int defectCategoryId) {
        mDefectCategoryId = defectCategoryId;
    }

    public int getItemNumber() {
        return mItemNumber;
    }
    public void setItemNumber(int itemNumber) {
        mItemNumber = itemNumber;
    }

    public String getItemDescription() {
        return mItemDescription;
    }
    public void setItemDescription(String itemDescription) {
        mItemDescription = itemDescription;
    }
}

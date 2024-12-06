package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "defect_item_table", primaryKeys = {"DefectItemID", "DefectCategoryID", "CategoryName"})
public class DefectItem_Table {
    public int DefectItemID;
    public int ItemNumber;
    public String ItemDescription;
    public int DefectCategoryID;
    @NonNull
    public String CategoryName;
    public String SpanishItemDescription;
    public boolean ReInspectionRequired;

    public DefectItem_Table() {}

    public DefectItem_Table(int DefectItemID, int DefectCategoryID, String CategoryName, int ItemNumber,
                            String ItemDescription, String SpanishItemDescription, boolean ReInspectionRequired) {
        this.DefectItemID = DefectItemID;
        this.DefectCategoryID = DefectCategoryID;
        this.CategoryName = CategoryName;
        this.ItemNumber = ItemNumber;
        this.ItemDescription = ItemDescription;
        this.SpanishItemDescription = SpanishItemDescription;
        this.ReInspectionRequired = ReInspectionRequired;
    }
}

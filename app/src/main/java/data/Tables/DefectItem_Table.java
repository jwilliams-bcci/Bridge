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
    public boolean RequiresEntry;
    public boolean RequiresPhoto;
    public boolean ShowC;
    public boolean ShowNC;
    public boolean ShowR;
    public boolean ShowNA;

    public DefectItem_Table() {}

    public DefectItem_Table(int DefectItemID, int DefectCategoryID, String CategoryName, int ItemNumber,
                            String ItemDescription, String SpanishItemDescription, boolean ReInspectionRequired,
                            boolean RequiresEntry, boolean RequiresPhoto, boolean ShowC, boolean ShowNC,
                            boolean ShowR, boolean ShowNA) {
        this.DefectItemID = DefectItemID;
        this.DefectCategoryID = DefectCategoryID;
        this.CategoryName = CategoryName;
        this.ItemNumber = ItemNumber;
        this.ItemDescription = ItemDescription;
        this.SpanishItemDescription = SpanishItemDescription;
        this.ReInspectionRequired = ReInspectionRequired;
        this.RequiresEntry = RequiresEntry;
        this.RequiresPhoto = RequiresPhoto;
        this.ShowC = ShowC;
        this.ShowNC = ShowNC;
        this.ShowR = ShowR;
        this.ShowNA = ShowNA;
    }
}

package data.Tables;

import androidx.room.Entity;

@Entity(tableName = "defect_item_x_inspection_type", primaryKeys = {"DefectItemID", "InspectionTypeID"})
public class DefectItem_InspectionType_XRef {
    public int DefectItemID;
    public int InspectionTypeID;
}
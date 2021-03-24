package data.Tables;

import androidx.room.Entity;

@Entity(tableName = "defect_item_x_inspection_type", primaryKeys = {"defect_item_id", "inspection_type_id"})
public class DefectItem_InspectionType_XRef {
    public int defect_item_id;
    public int inspection_type_id;
}
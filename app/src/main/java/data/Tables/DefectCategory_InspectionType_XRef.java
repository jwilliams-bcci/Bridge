package data.Tables;

import androidx.room.Entity;

@Entity(tableName = "defect_category_x_inspection_type", primaryKeys = {"defect_category_id", "inspection_type_id"})
public class DefectCategory_InspectionType_XRef {
    public int defect_category_id;
    public int inspection_type_id;
}

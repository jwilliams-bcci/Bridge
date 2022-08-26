package data.Tables;

import androidx.room.Entity;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Update;

@Entity(tableName = "defect_item_table", primaryKeys = {"id", "defect_category_id"})
public class DefectItem_Table {
    public int id;
    public int defect_category_id;
    public String defect_category_name;
    public int item_number;
    public int inspection_type_id;
    public String item_description;
    public String spanish_item_description;
    public boolean reinspection_required;

    public DefectItem_Table() {}

    public DefectItem_Table(int id, int defect_category_id, String defect_category_name, int item_number, int inspection_type_id,
                            String item_description, String spanish_item_description, boolean reinspection_required) {
        this.id = id;
        this.defect_category_id = defect_category_id;
        this.defect_category_name = defect_category_name;
        this.item_number = item_number;
        this.inspection_type_id = inspection_type_id;
        this.item_description = item_description;
        this.spanish_item_description = spanish_item_description;
        this.reinspection_required = reinspection_required;
    }
}

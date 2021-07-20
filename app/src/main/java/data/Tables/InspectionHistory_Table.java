package data.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inspection_history_table")
public class InspectionHistory_Table {
    @PrimaryKey
    public int id;
    public int inspection_id;
    public int previous_inspection_id;
    public int defect_item_id;
    public int defect_item_number;
    public int defect_category_id;
    public String defect_category_name;
    public String defect_item_description;
    public String comment;
    public boolean is_reviewed;
}

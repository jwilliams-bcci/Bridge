package data.Tables;

import androidx.room.Entity;

@Entity(tableName = "inspection_history_table", primaryKeys = {"inspection_id", "defect_item_id"})
public class InspectionHistory_Table {
    public int id;
    public int inspection_id;
    public int previous_inspection_id;
    public int defect_item_id;
    public String comment;
}

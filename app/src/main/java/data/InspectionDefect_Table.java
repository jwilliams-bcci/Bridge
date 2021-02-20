package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inspection_defect_table")
public class InspectionDefect_Table {
    @PrimaryKey
    public int id;
    public int inspection_id;
    public int defect_item_id;
    public int defect_status_id;
    public int location_id;
    public int room_id;
    public int direction_id;
    public int canned_comment_id;
    public String note;
}

package data.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "defect_item_table")
public class DefectItem_Table {
    @PrimaryKey
    public int id;
    public int defect_category_id;
    public int item_number;
    public String item_description;
}

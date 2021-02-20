package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "defect_category_table")
public class DefectCategory_Table {
    @PrimaryKey
    public int id;
    public String category_name;
}

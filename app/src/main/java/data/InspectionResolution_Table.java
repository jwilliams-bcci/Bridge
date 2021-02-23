package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inspection_resolution_table")
public class InspectionResolution_Table {
    @PrimaryKey
    public int id;
    public String resolution_description;
}

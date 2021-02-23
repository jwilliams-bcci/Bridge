package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "direction_table")
public class Direction_Table {
    @PrimaryKey
    public int id;
    public String direction_description;
}

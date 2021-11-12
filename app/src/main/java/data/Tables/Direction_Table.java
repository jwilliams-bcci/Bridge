package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "direction_table")
public class Direction_Table {
    @PrimaryKey
    public int id;
    public String direction_description;
    public int direction_order;

    @NonNull
    @Override
    public String toString() {
        return direction_description;
    }
}

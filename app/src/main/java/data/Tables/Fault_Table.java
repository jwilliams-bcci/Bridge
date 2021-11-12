package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fault_table")
public class Fault_Table {
    @PrimaryKey
    public int id;
    public String text;
    public String display_text;

    @NonNull
    @Override
    public String toString() {
        return display_text;
    }
}

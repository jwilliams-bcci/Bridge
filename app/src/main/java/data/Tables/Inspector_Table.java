package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inspector_table")
public class Inspector_Table {
    @PrimaryKey
    public int id;
    public String inspector_name;
    public int division_id;

    @NonNull
    @Override
    public String toString() {
        return inspector_name;
    }
}

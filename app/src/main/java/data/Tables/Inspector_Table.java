package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inspector_table", primaryKeys = {"id", "division_id"})
public class Inspector_Table {
    public int id;
    public String inspector_name;
    public int division_id;

    @NonNull
    @Override
    public String toString() {
        return inspector_name;
    }
}

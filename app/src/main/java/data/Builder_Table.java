package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "builder_table")
public class Builder_Table {
    @PrimaryKey
    public int id;
    public String builder_name;
}

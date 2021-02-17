package data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.Date;

@Entity(tableName = "inspection_table")
public class Inspection_Table {
    @PrimaryKey
    public int id;
    public int inspection_number;
    @TypeConverters({TimestampConverter.class})
    public Date inspection_date;
    public int builder_id;
    public String superintendent;
    public int location_id;
    public String inspection_type;
    public String notes;
    public boolean is_complete;
    public boolean is_uploaded;
}

package data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;
import java.util.Date;

@Entity(tableName = "inspection_table")
public class Inspection_Table {
    @PrimaryKey
    public int id;
    public int inspection_number;
    @TypeConverters({DateConverter.class})
    public Date inspection_date;
    public int builder_id;
    public String superintendent;
    public int location_id;
    public String inspection_type;
    public String notes;
    public boolean is_complete;
    public boolean is_uploaded;

    public Inspection_Table(@NonNull int id, int inspection_number, Date inspection_date,
                            int builder_id, String superintendent, int location_id,
                            String inspection_type, String notes, boolean is_complete,
                            boolean is_uploaded) {
        this.id = id;
        this.inspection_number = inspection_number;
        this.inspection_date = inspection_date;
        this.builder_id = builder_id;
        this.superintendent = superintendent;
        this.location_id = location_id;
        this.inspection_type = inspection_type;
        this.notes = notes;
        this.is_complete = is_complete;
        this.is_uploaded = is_uploaded;
    }
}

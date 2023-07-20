package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;

import data.DateConverter;

@Entity(tableName = "past_inspection_table")
public class PastInspection_Table {
    @PrimaryKey
    public int id;
    @TypeConverters({DateConverter.class})
    public OffsetDateTime inspection_submit_time;
    public int location_id;
    public String inspector;
    public String inspection_type;
    public int inspection_status_id;
    public String incomplete_reason;

    public PastInspection_Table(){}

    public PastInspection_Table(@NonNull int id, OffsetDateTime inspection_submit_time,
                                int location_id, String inspector, String inspection_type,
                                int inspection_status_id, String incomplete_reason) {
        this.id = id;
        this.inspection_submit_time = inspection_submit_time;
        this.location_id = location_id;
        this.inspector = inspector;
        this.inspection_type = inspection_type;
        this.inspection_status_id = inspection_status_id;
        this.incomplete_reason = incomplete_reason;
    }
}

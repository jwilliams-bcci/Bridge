package data.Tables;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;
import java.util.Date;

import data.DateConverter;

@Entity(tableName = "inspection_table")
public class Inspection_Table {
    @PrimaryKey
    public int id;
    public int inspection_type_id;
    @TypeConverters({DateConverter.class})
    public Date inspection_date;
    public int location_id;
    public String builder_name;
    public int builder_id;
    public String super_name;
    public int inspector_id;
    public String inspector;
    public String community;
    public int community_id;
    public String city;
    public String inspection_type;
    public boolean reinspect;
    public String address;
    public int inspection_status_id;
    public String inspection_status;
    public String super_phone;
    public String super_email;
    public int super_present;
    public String incomplete_reason;
    public int incomplete_reason_id;
    public String notes;
    public boolean is_complete;
    public boolean is_uploaded;
    public int route_sheet_order;

    public Inspection_Table () {

    }

    public Inspection_Table(@NonNull int id, Date inspection_date, int location_id,
                            String builder_name, int builder_id, String super_name,
                            int inspector_id, String inspector, String community, int community_id,
                            String city, int inspection_type_id, String inspection_type,
                            boolean reinspect, String address, int inspection_status_id,
                            String inspection_status, String super_phone, String super_email,
                            int super_present, String incomplete_reason, int incomplete_reason_id,
                            String notes, boolean is_complete, boolean is_uploaded,
                            int route_sheet_order) {
        this.id = id;
        this.inspection_date = inspection_date;
        this.builder_name = builder_name;
        this.builder_id = builder_id;
        this.super_name = super_name;
        this.community = community;
        this.community_id = community_id;
        this.city = city;
        this.inspector_id = inspector_id;
        this.inspector = inspector;
        this.location_id = location_id;
        this.inspection_type_id = inspection_type_id;
        this.inspection_type = inspection_type;
        this.reinspect = reinspect;
        this.address = address;
        this.inspection_status_id = inspection_status_id;
        this.inspection_status = inspection_status;
        this.super_phone = super_phone;
        this.super_email = super_email;
        this.super_present = super_present;
        this.incomplete_reason = incomplete_reason;
        this.incomplete_reason_id = incomplete_reason_id;
        this.notes = notes;
        this.is_complete = is_complete;
        this.is_uploaded = is_uploaded;
        this.route_sheet_order = route_sheet_order;
    }
}

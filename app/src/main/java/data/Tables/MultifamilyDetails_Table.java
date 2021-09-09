package data.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "multifamily_details_table")
public class MultifamilyDetails_Table {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int inspection_id;
    public String builder_personnel;
    public String burgess_personnel;
    public String area_observed;
    public String temperature;
    public String weather_conditions;
}

package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class Location_Table {
    @PrimaryKey
    public int id;
    public String address;
    public int zip_code;
    public String city;
    public String state;
    public String community;
}

package data.Tables;

import androidx.annotation.NonNull;
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

    public Location_Table(@NonNull int id, String address, int zip_code, String city,
                          String state, String community) {
        this.id = id;
        this.address = address;
        this.zip_code = zip_code;
        this.city = city;
        this.state = state;
        this.community = community;
    }
}

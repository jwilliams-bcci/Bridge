package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "room_table")
public class Room_Table {
    @PrimaryKey
    public int id;
    public String room_name;

    @NonNull
    @Override
    public String toString() {
        return room_name;
    }
}

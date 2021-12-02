package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Room_Table;

@Dao
public interface Room_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Room_Table room);

    @Query("DELETE FROM room_table")
    void deleteAll();

    @Query("SELECT * FROM room_table ORDER BY room_name ASC")
    List<Room_Table> getRooms();
}

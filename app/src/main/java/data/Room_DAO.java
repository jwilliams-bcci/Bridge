package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Room_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Room_Table room);

    @Query("DELETE FROM room_table")
    void deleteAll();

    @Query("SELECT * FROM room_table ORDER BY id ASC")
    LiveData<List<Room_Table>> getRooms();
}

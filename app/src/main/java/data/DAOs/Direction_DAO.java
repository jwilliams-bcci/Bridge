package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Direction_Table;

@Dao
public interface Direction_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Direction_Table direction);

    @Query("DELETE FROM direction_table")
    void deleteAll();

    @Query("SELECT * FROM direction_table ORDER BY id ASC")
    LiveData<List<Direction_Table>> getDirections();
}

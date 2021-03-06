package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Location_Table;

@Dao
public interface Location_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Location_Table location);

    @Query("DELETE FROM location_table")
    void deleteAll();

    @Query("SELECT * FROM location_table ORDER BY id ASC")
    LiveData<List<Location_Table>> getLocations();

    @Query("SELECT * FROM location_table WHERE id = :location_id")
    public LiveData<Location_Table> getLocation(int location_id);
}
package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.PastInspection_Table;

@Dao
public interface PastInspection_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PastInspection_Table pastInspection);

    @Query("DELETE FROM past_inspection_table")
    void deleteAll();

    @Query("DELETE FROM past_inspection_table WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM past_inspection_table WHERE location_id = :locationId")
    LiveData<List<PastInspection_Table>> getPastInspections(int locationId);
}

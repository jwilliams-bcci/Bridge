package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.InspectionResolution_Table;

@Dao
public interface InspectionResolution_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(InspectionResolution_Table resolution);

    @Query("DELETE FROM inspection_resolution_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_resolution_table ORDER BY id ASC")
    LiveData<List<InspectionResolution_Table>> getInspectionResolutions();
}

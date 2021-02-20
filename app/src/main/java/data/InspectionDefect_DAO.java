package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InspectionDefect_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(InspectionDefect_Table inspectionDefect);

    @Query("DELETE FROM inspection_defect_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_defect_table ORDER BY id ASC")
    LiveData<List<InspectionDefect_Table>> getInspectionDefects();
}

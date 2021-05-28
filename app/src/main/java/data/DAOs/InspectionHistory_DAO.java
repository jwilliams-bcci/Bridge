package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.InspectionHistory_Table;

@Dao
public interface InspectionHistory_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(InspectionHistory_Table inspectionHistory);

    @Query("DELETE FROM inspection_history_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_history_table WHERE inspection_id = :inspection_id")
    LiveData<List<InspectionHistory_Table>> getInspectionHistory(int inspection_id);

    @Query("SELECT * FROM inspection_history_table WHERE inspection_id = :inspection_id")
    List<InspectionHistory_Table> getInspectionHistorySync(int inspection_id);
}

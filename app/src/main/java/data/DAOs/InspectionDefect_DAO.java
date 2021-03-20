package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.ReviewAndSubmit_View;
import data.Tables.InspectionDefect_Table;

@Dao
public interface InspectionDefect_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(InspectionDefect_Table inspectionDefect);

    @Query("DELETE FROM inspection_defect_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_defect_table WHERE inspection_id = :inspection_id ORDER BY id ASC")
    LiveData<List<InspectionDefect_Table>> getInspectionDefects(int inspection_id);

    @Query("SELECT * FROM inspection_defect_table WHERE id = :inspection_defect_id")
    LiveData<InspectionDefect_Table> getInspectionDefect(int inspection_defect_id);

    @Query("SELECT * FROM reviewandsubmit_view WHERE inspection_id = :inspection_id")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReview(int inspection_id);
}

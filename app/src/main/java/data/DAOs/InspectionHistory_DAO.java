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

    @Query("SELECT * " +
            "FROM inspection_history_table " +
            "WHERE inspection_id = :inspection_id " +
            "ORDER BY comment ASC")
    LiveData<List<InspectionHistory_Table>> getInspectionHistory(int inspection_id);

    @Query("SELECT comment FROM inspection_history_table WHERE id = :inspection_history_id")
    String getComment(int inspection_history_id);

    @Query("UPDATE inspection_history_table SET is_reviewed = 1 WHERE id = :inspection_history_id")
    void updateIsReviewed(int inspection_history_id);

    @Query("UPDATE inspection_history_table SET reviewed_status = :defect_status_id WHERE id = :inspection_history_id")
    void updateReviewedStatus(int defect_status_id, int inspection_history_id);

    @Query("UPDATE inspection_history_table SET inspection_defect_id = :inspection_defect_id WHERE id = :inspection_history_id")
    void updateInspectionDefectId(int inspection_defect_id, int inspection_history_id);

    @Query("SELECT COUNT(*) FROM inspection_history_table WHERE inspection_id = :inspection_id AND is_reviewed = 0")
    int getItemsToReview(int inspection_id);
}

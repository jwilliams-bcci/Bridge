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

    @Query("DELETE FROM inspection_history_table WHERE InspectionID = :inspection_id")
    void deleteForInspection(int inspection_id);

    @Query("DELETE FROM inspection_history_table")
    void deleteAll();

    @Query("UPDATE inspection_history_table SET InspectionID = :inspection_id, IsReviewed = 0, ReviewedStatus = null, InspectionDefectID = -1  WHERE InspectionDetailID = :id")
    void update(int id, int inspection_id);

    @Query("SELECT * " +
            "FROM inspection_history_table " +
            "WHERE InspectionID = :inspection_id " +
            "ORDER BY IsReviewed, Comment COLLATE NOCASE ASC")
    LiveData<List<InspectionHistory_Table>> getInspectionHistory(int inspection_id);

    @Query("SELECT * FROM inspection_history_table WHERE InspectionDetailID = :inspection_history_id")
    InspectionHistory_Table getInspectionHistorySync(int inspection_history_id);

    @Query("SELECT Comment FROM inspection_history_table WHERE InspectionDetailID = :inspection_history_id")
    String getComment(int inspection_history_id);

    @Query("UPDATE inspection_history_table SET IsReviewed = 1 WHERE InspectionDetailID = :inspection_history_id")
    void updateIsReviewed(int inspection_history_id);

    @Query("UPDATE inspection_history_table SET ReviewedStatus = :defect_status_id WHERE InspectionDetailID = :inspection_history_id")
    void updateReviewedStatus(int defect_status_id, int inspection_history_id);

    @Query("UPDATE inspection_history_table SET InspectionDefectID = :inspection_defect_id WHERE InspectionDetailID = :inspection_history_id")
    void updateInspectionDefectId(int inspection_defect_id, int inspection_history_id);

    @Query("SELECT COUNT(*) FROM inspection_history_table WHERE InspectionID = :inspection_id AND IsReviewed = 0")
    int getItemsToReview(int inspection_id);
}

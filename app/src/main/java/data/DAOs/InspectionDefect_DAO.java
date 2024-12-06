package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import data.Views.ReviewAndSubmit_View;
import data.Tables.InspectionDefect_Table;

@Dao
public interface InspectionDefect_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(InspectionDefect_Table inspectionDefect);

    @Update
    void update(InspectionDefect_Table inspectionDefect);

    @Query("DELETE FROM inspection_defect_table WHERE InspectionID = :inspection_id")
    void delete(int inspection_id);

    @Query("DELETE FROM inspection_defect_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_defect_table WHERE InspectionID = :inspection_id ORDER BY ID ASC")
    LiveData<List<InspectionDefect_Table>> getInspectionDefects(int inspection_id);

    @Query("SELECT * FROM inspection_defect_table WHERE InspectionID= :inspection_id ORDER BY ID ASC")
    List<InspectionDefect_Table> getInspectionDefectsSync(int inspection_id);

    @Query("SELECT ID FROM inspection_defect_table WHERE FirstDefectInspectionDetailID = :first_inspection_detail_id AND InspectionID = :inspection_id LIMIT 1")
    int getExistingMFCDefect(int first_inspection_detail_id, int inspection_id);

    @Query("UPDATE inspection_defect_table SET PriorInspectionDetailID = :prior_inspection_detail_id WHERE ID = :id")
    void updatePriorInspectionDetailId(int prior_inspection_detail_id, int id);

    @Query("UPDATE inspection_defect_table SET DefectStatusID = :defect_status_id, Comment = :comment WHERE ID = :id")
    void updateExistingMFCDefect(int defect_status_id, String comment, int id);

    @Query("SELECT * FROM inspection_defect_table WHERE ID = :inspection_defect_id")
    InspectionDefect_Table getInspectionDefect(int inspection_defect_id);

    @Query("SELECT * FROM ReviewAndSubmit_View WHERE InspectionID = :inspection_id ORDER BY ItemDescription collate nocase asc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDescriptionSortAsc(int inspection_id);

    @Query("SELECT * FROM ReviewAndSubmit_View WHERE InspectionID = :inspection_id ORDER BY ItemDescription collate nocase desc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDescriptionSortDesc(int inspection_id);

    @Query("SELECT * FROM ReviewAndSubmit_View WHERE InspectionID = :inspection_id ORDER BY ItemNumber asc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewItemNumberSortAsc(int inspection_id);

    @Query("SELECT * FROM ReviewAndSubmit_View WHERE InspectionID = :inspection_id ORDER BY ItemNumber desc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewItemNumberSortDesc(int inspection_id);

    @Query("SELECT * FROM ReviewAndSubmit_View WHERE InspectionID = :inspection_id ORDER BY InspectionDefectID asc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDefectIDSortAsc(int inspection_id);

    @Query("SELECT * FROM ReviewAndSubmit_View WHERE InspectionID = :inspection_id ORDER BY InspectionDefectID desc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDefectIDSortDesc(int inspection_id);

    @Query("SELECT * FROM ReviewAndSubmit_View WHERE InspectionID = :inspection_id")
    List<ReviewAndSubmit_View> getInspectionDefectsForReviewSync(int inspection_id);

    @Query("SELECT * FROM inspection_defect_table WHERE InspectionID = :inspection_id AND ReinspectionRequired = 1")
    List<InspectionDefect_Table> getReinspectionRequiredDefects(int inspection_id);

    @Query("UPDATE inspection_defect_table SET IsUploaded = 1 WHERE ID = :inspection_defect_id")
    void markDefectUploaded(int inspection_defect_id);

    @Query("SELECT COUNT(CASE WHEN IsUploaded = 0 THEN 1 END) AS total_uploaded FROM inspection_defect_table WHERE InspectionID = :inspection_id")
    int remainingToUpload(int inspection_id);

    @Query("DELETE FROM inspection_defect_table WHERE ID = :inspection_defect_id")
    void deleteInspectionDefect(int inspection_defect_id);

    @Query("SELECT COUNT(*) AS NumDefects FROM inspection_defect_table WHERE InspectionID = :inspection_id AND DefectStatusID = 2")
    int getInspectionDefectCount(int inspection_id);
}

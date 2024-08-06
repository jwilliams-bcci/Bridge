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

    @Query("DELETE FROM inspection_defect_table WHERE inspection_id = :inspection_id")
    void delete(int inspection_id);

    @Query("DELETE FROM inspection_defect_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_defect_table WHERE inspection_id = :inspection_id ORDER BY id ASC")
    LiveData<List<InspectionDefect_Table>> getInspectionDefects(int inspection_id);

    @Query("SELECT * FROM inspection_defect_table WHERE inspection_id= :inspection_id ORDER BY id ASC")
    List<InspectionDefect_Table> getInspectionDefectsSync(int inspection_id);

    @Query("SELECT id FROM inspection_defect_table WHERE first_inspection_detail_id = :first_inspection_detail_id LIMIT 1")
    int multifamilyDefectExists(int first_inspection_detail_id);

    @Query("UPDATE inspection_defect_table SET prior_inspection_detail_id = :prior_inspection_detail_id WHERE id = :id")
    void updatePriorInspectionDetailId(int prior_inspection_detail_id, int id);

    @Query("UPDATE inspection_defect_table SET defect_status_id = :defect_status_id, comment = :comment WHERE id = :id")
    void updateExistingMFCDefect(int defect_status_id, String comment, int id);

    @Query("SELECT * FROM inspection_defect_table WHERE id = :inspection_defect_id")
    InspectionDefect_Table getInspectionDefect(int inspection_defect_id);

    @Query("SELECT * FROM reviewandsubmit_view WHERE inspection_id = :inspection_id ORDER BY item_description asc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDescriptionSortAsc(int inspection_id);

    @Query("SELECT * FROM reviewandsubmit_view WHERE inspection_id = :inspection_id ORDER BY item_description desc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDescriptionSortDesc(int inspection_id);

    @Query("SELECT * FROM reviewandsubmit_view WHERE inspection_id = :inspection_id ORDER BY item_number asc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewItemNumberSortAsc(int inspection_id);

    @Query("SELECT * FROM reviewandsubmit_view WHERE inspection_id = :inspection_id ORDER BY item_number desc")
    LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewItemNumberSortDesc(int inspection_id);

    @Query("SELECT * FROM reviewandsubmit_view WHERE inspection_id = :inspection_id")
    List<ReviewAndSubmit_View> getInspectionDefectsForReviewSync(int inspection_id);

    @Query("SELECT * FROM inspection_defect_table WHERE inspection_id = :inspection_id AND reinspection_required = 1")
    List<InspectionDefect_Table> getReinspectionRequiredDefects(int inspection_id);

    @Query("UPDATE inspection_defect_table SET is_uploaded = 1 WHERE id = :inspection_defect_id")
    void markDefectUploaded(int inspection_defect_id);

    @Query("SELECT COUNT(CASE WHEN is_uploaded = 0 THEN 1 END) AS total_uploaded FROM inspection_defect_table WHERE inspection_id = :inspection_id")
    int remainingToUpload(int inspection_id);

    @Query("DELETE FROM inspection_defect_table WHERE id = :inspection_defect_id")
    void deleteInspectionDefect(int inspection_defect_id);

    @Query("SELECT COUNT(*) AS NumDefects FROM inspection_defect_table WHERE inspection_id = :inspection_id AND defect_status_id = 2")
    int getInspectionDefectCount(int inspection_id);
}

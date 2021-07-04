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
            "WHERE inspection_id = :inspection_id")
    LiveData<List<InspectionHistory_Table>> getInspectionHistory(int inspection_id);

    @Query("SELECT h.* " +
            "FROM inspection_history_table h " +
            "WHERE h.inspection_id = :inspection_id " +
            "ORDER BY h.defect_category_id, h.defect_item_number ASC")
    LiveData<List<InspectionHistory_Table>> getInspectionHistoryNumberSort(int inspection_id);

    @Query("SELECT h.* " +
            "FROM inspection_history_table h " +
            "WHERE h.inspection_id = :inspection_id " +
            "ORDER BY h.defect_category_id, h.defect_item_description ASC")
    LiveData<List<InspectionHistory_Table>> getInspectionHistoryDescriptionSort(int inspection_id);

    @Query("SELECT h.* " +
            "FROM inspection_history_table h " +
            "WHERE h.inspection_id = :inspection_id AND h.defect_category_name = :category_name " +
            "ORDER BY h.defect_category_id, h.defect_item_number ASC")
    LiveData<List<InspectionHistory_Table>> getInspectionHistoryFilteredNumberSort(String category_name, int inspection_id);

    @Query("SELECT h.* " +
            "FROM inspection_history_table h " +
            "WHERE h.inspection_id = :inspection_id AND h.defect_category_name = :category_name " +
            "ORDER BY h.defect_category_id, h.defect_item_description ASC")
    LiveData<List<InspectionHistory_Table>> getInspectionHistoryFilteredDescriptionSort(String category_name, int inspection_id);

    @Query("SELECT comment FROM inspection_history_table WHERE id = :inspection_history_id")
    String getComment(int inspection_history_id);
}

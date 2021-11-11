package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.DefectItem_Table;
import data.Views.ReinspectDefectList_View;

@Dao
public interface DefectItem_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DefectItem_Table defectItem);

    @Query("DELETE FROM defect_item_table")
    void deleteAll();

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN defect_item_x_inspection_type x ON x.defect_item_id = d.id " +
            "WHERE x.inspection_type_id = :inspection_type_id " +
            "ORDER BY d.defect_category_id, d.item_number ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsNumberSort(int inspection_type_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN inspection_history_table h ON h.defect_item_id = d.id " +
            "WHERE h.inspection_id = :inspection_id " +
            "ORDER BY d.defect_category_id, d.item_number ASC")
    LiveData<List<DefectItem_Table>> getReinspectionDefectItemsNumberSort(int inspection_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN defect_item_x_inspection_type x ON x.defect_item_id = d.id " +
            "WHERE x.inspection_type_id = :inspection_type_id " +
            "ORDER BY d.defect_category_id, d.item_description ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsDescriptionSort(int inspection_type_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN inspection_history_table h ON h.defect_item_id = d.id " +
            "WHERE h.inspection_id = :inspection_id " +
            "ORDER BY d.defect_category_id, d.item_description ASC")
    LiveData<List<DefectItem_Table>> getReinspectionDefectItemsDescriptionSort(int inspection_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN defect_item_x_inspection_type x ON x.defect_item_id = d.id " +
            "WHERE defect_category_name = :categoryName AND x.inspection_type_id = :inspection_type_id " +
            "ORDER BY d.item_number ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsFilteredNumberSort(String categoryName, int inspection_type_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN inspection_history_table h ON h.defect_item_id = d.id " +
            "WHERE d.defect_category_name = :categoryName AND h.inspection_id = :inspection_id " +
            "ORDER BY d.item_number ASC")
    LiveData<List<DefectItem_Table>> getReinspectionDefectItemsFilteredNumberSort(String categoryName, int inspection_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN defect_item_x_inspection_type x ON x.defect_item_id = d.id " +
            "WHERE d.defect_category_name = :categoryName AND x.inspection_type_id = :inspection_type_id " +
            "ORDER BY d.item_description ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsFilteredDescriptionSort(String categoryName, int inspection_type_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN inspection_history_table h ON h.defect_item_id = d.id " +
            "WHERE d.defect_category_name = :categoryName AND h.inspection_id = :inspection_id " +
            "ORDER BY d.item_description ASC")
    LiveData<List<DefectItem_Table>> getReinspectionDefectItemsFilteredDescriptionSort(String categoryName, int inspection_id);

    @Query("SELECT 'ALL' AS [category_name] UNION SELECT DISTINCT d.defect_category_name FROM defect_item_table d INNER JOIN defect_item_x_inspection_type x ON x.defect_item_id = d.id WHERE x.inspection_type_id = :inspection_type_id ORDER BY category_name ASC")
    LiveData<List<String>> getDefectCategories(int inspection_type_id);

    @Query("SELECT * FROM defect_item_table WHERE id = :defect_item_id")
    LiveData<DefectItem_Table> getDefectItem(int defect_item_id);

    @Query("SELECT * FROM defect_item_table WHERE id = :defect_item_id")
    DefectItem_Table getDefectItemSync(int defect_item_id);
}

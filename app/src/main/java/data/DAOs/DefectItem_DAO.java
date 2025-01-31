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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DefectItem_Table defectItem);

    @Query("DELETE FROM defect_item_table")
    void deleteAll();

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN defect_item_x_inspection_type x ON x.DefectItemID = d.DefectItemID " +
            "WHERE x.InspectionTypeID = :inspection_type_id AND d.CategoryName <> 'Re-Observation' " +
            "ORDER BY d.DefectCategoryID, d.ItemNumber ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsNumberSort(int inspection_type_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN defect_item_x_inspection_type x ON x.DefectItemID = d.DefectItemID " +
            "WHERE x.InspectionTypeID = :inspection_type_id AND d.CategoryName <> 'Re-Observation' " +
            "ORDER BY d.DefectCategoryID, d.ItemDescription ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsDescriptionSort(int inspection_type_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN defect_item_x_inspection_type x ON x.DefectItemID = d.DefectItemID " +
            "WHERE CategoryName = :categoryName AND x.InspectionTypeID = :inspection_type_id AND d.CategoryName <> 'Re-Observation' " +
            "ORDER BY d.ItemNumber ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsFilteredNumberSort(String categoryName, int inspection_type_id);

    @Query("SELECT d.* " +
            "FROM defect_item_table d " +
            "INNER JOIN defect_item_x_inspection_type x ON x.DefectItemID = d.DefectItemID " +
            "WHERE d.CategoryName = :categoryName AND x.InspectionTypeID = :inspection_type_id AND d.CategoryName <> 'Re-Observation' " +
            "ORDER BY d.ItemDescription ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsFilteredDescriptionSort(String categoryName, int inspection_type_id);

    @Query("SELECT 'ALL' AS [category_name] UNION SELECT DISTINCT d.CategoryName FROM defect_item_table d INNER JOIN defect_item_x_inspection_type x ON x.DefectItemID = d.DefectItemID WHERE x.InspectionTypeID = :inspection_type_id AND d.CategoryName <> 'Re-Observation' ORDER BY category_name ASC")
    LiveData<List<String>> getDefectCategories(int inspection_type_id);

    @Query("SELECT * FROM defect_item_table WHERE DefectItemID = :defect_item_id")
    LiveData<DefectItem_Table> getDefectItem(int defect_item_id);

    @Query("SELECT * FROM defect_item_table WHERE DefectItemID = :defect_item_id")
    DefectItem_Table getDefectItemSync(int defect_item_id);
}
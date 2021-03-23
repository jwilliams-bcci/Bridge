package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.DefectItem_Table;

@Dao
public interface DefectItem_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DefectItem_Table defectItem);

    @Query("DELETE FROM defect_item_table")
    void deleteAll();

    @Query("SELECT d.* FROM defect_item_table d INNER JOIN defect_category_x_inspection_type x ON x.defect_category_id = d.defect_category_id WHERE x.inspection_type_id = :inspection_type_id ORDER BY defect_category_id, item_number ASC")
    LiveData<List<DefectItem_Table>> getDefectItems(int inspection_type_id);

    @Query("SELECT * FROM defect_item_table WHERE category_name = :categoryName ORDER BY item_number ASC")
    LiveData<List<DefectItem_Table>> getDefectItemsFiltered(String categoryName);

    @Query("SELECT 'ALL' AS [category_name] UNION SELECT DISTINCT d.category_name FROM defect_item_table d INNER JOIN defect_category_x_inspection_type x ON x.defect_category_id = d.defect_category_id WHERE x.inspection_type_id = :inspection_type_id ORDER BY category_name ASC")
    LiveData<List<String>> getDefectCategories(int inspection_type_id);

    @Query("SELECT * FROM defect_item_table WHERE id = :defect_item_id")
    LiveData<DefectItem_Table> getDefectItem(int defect_item_id);
}

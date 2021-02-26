package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.DefectCategory_Table;

@Dao
public interface DefectCategory_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DefectCategory_Table defectCategory);

    @Query("DELETE FROM defect_category_table")
    void deleteAll();

    @Query("SELECT * FROM defect_category_table ORDER BY id ASC")
    LiveData<List<DefectCategory_Table>> getDefectCategories();
}

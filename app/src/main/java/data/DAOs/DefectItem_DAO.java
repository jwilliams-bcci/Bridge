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

    @Query("SELECT * FROM defect_item_table ORDER BY id ASC")
    LiveData<List<DefectItem_Table>> getDefectItems();
}

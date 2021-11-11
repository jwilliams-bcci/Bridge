package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Inspector_Table;

@Dao
public interface Inspector_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Inspector_Table inspector);

    @Query("DELETE FROM inspector_table")
    void deleteAll();

    @Query("SELECT * FROM inspector_table ORDER BY inspector_name ASC")
    List<Inspector_Table> getInspectors();

    @Query("SELECT * FROM inspector_table WHERE division_id = :divisionId ORDER BY inspector_name ASC")
    List<Inspector_Table> getInspectorsByDivision(int divisionId);
}

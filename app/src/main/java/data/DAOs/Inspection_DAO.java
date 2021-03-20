package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Inspection_Table;
import data.RouteSheet_View;

@Dao
public interface Inspection_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Inspection_Table inspection);

    @Query("DELETE FROM inspection_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_table WHERE is_complete = 0 ORDER BY id ASC")
    LiveData<List<Inspection_Table>> getInspections();

    @Query("SELECT * FROM inspection_table WHERE id = :inspection_id")
    LiveData<Inspection_Table> getInspection(int inspection_id);

    @Query("UPDATE inspection_table SET is_complete = 1 WHERE id = :inspection_id")
    void completeInspection(int inspection_id);
}

package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_ChangeLog_Table;

@Dao
public interface Ekotrope_ChangeLog_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_ChangeLog_Table changeLog);

    @Query("DELETE FROM ekotrope_change_log_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_change_log_table WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM ekotrope_change_log_table WHERE inspection_id = :inspection_id")
    List<Ekotrope_ChangeLog_Table> getEkotropeChanges(int inspection_id);
}
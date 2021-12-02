package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Fault_Table;

@Dao
public interface Fault_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Fault_Table fault);

    @Query("DELETE FROM fault_table")
    void deleteAll();

    @Query("SELECT * FROM fault_table ORDER BY text COLLATE NOCASE ASC")
    List<Fault_Table> getFaults();
}
package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Builder_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Builder_Table builder);

    @Query("DELETE FROM builder_table")
    void deleteAll();

    @Query("SELECT * FROM builder_table ORDER BY id ASC")
    LiveData<List<Builder_Table>> getBuilders();
}

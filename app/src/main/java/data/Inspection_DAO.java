package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bridge.OnButtonClickListener;

import java.util.List;

@Dao
public interface Inspection_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Inspection inspection);

    @Query("DELETE FROM inspection_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_table ORDER BY id ASC")
    LiveData<List<Inspection>> getInspections();
}

package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.SubmitRequest_Table;

@Dao
public interface SubmitRequest_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(SubmitRequest_Table submitRequest);

    @Query("DELETE FROM submit_request_table WHERE inspection_id = :inspection_id")
    void delete(int inspection_id);

    @Query("SELECT * FROM submit_request_table WHERE inspection_id = :inspection_id")
    LiveData<List<SubmitRequest_Table>> getSubmitRequests(int inspection_id);

    @Query("UPDATE submit_request_table SET complete = 1 WHERE id = :id")
    void markComplete(int id);

    @Query("UPDATE submit_request_table SET failed = 1 WHERE id = :id")
    void markFailed(int id);
}

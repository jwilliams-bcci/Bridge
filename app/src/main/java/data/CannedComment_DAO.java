package data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CannedComment_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CannedComment_Table cannedComment);

    @Query("DELETE FROM canned_comment_table")
    void deleteAll();

    @Query("SELECT * FROM canned_comment_table ORDER BY id ASC")
    LiveData<List<CannedComment_Table>> getCannedComments();
}

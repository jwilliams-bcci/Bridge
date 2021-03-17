package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.CannedComment_Table;

@Dao
public interface CannedComment_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CannedComment_Table cannedComment);

    @Query("DELETE FROM canned_comment_table")
    void deleteAll();

    @Query("SELECT '' AS [text] UNION SELECT text FROM canned_comment_table ORDER BY text ASC")
    LiveData<List<String>> getCannedComments();
}

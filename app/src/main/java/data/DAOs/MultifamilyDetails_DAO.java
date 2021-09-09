package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import data.Tables.MultifamilyDetails_Table;

@Dao
public interface MultifamilyDetails_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MultifamilyDetails_Table multifamilyDetails);

    @Query("DELETE FROM multifamily_details_table")
    void deleteAll();

    @Query("SELECT * FROM multifamily_details_table WHERE inspection_id = :inspection_id")
    MultifamilyDetails_Table getMultifamilyDetails(int inspection_id);
}

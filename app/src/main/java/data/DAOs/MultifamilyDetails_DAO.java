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

    @Query("UPDATE multifamily_details_table SET builder_personnel = :builder_personnel, burgess_personnel = :burgess_personnel, " +
            "area_observed = :area_observed, temperature = :temperature, weather_conditions = :weather_conditions WHERE id = :id")
    void updateMultifamilyDetails(int id, String builder_personnel, String burgess_personnel, String area_observed, String temperature, String weather_conditions);
}

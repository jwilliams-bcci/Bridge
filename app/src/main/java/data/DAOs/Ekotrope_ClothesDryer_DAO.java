package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import data.Tables.Ekotrope_ClothesDryer_Table;

@Dao
public interface Ekotrope_ClothesDryer_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_ClothesDryer_Table dryer);

    @Query("DELETE FROM ekotrope_clothes_dryer_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_clothes_dryer_table WHERE plan_id = :plan_id")
    void delete(String plan_id);

    @Query("SELECT * FROM ekotrope_clothes_dryer_table WHERE plan_id = :plan_id")
    Ekotrope_ClothesDryer_Table getClothesDryer(String plan_id);

    @Query("UPDATE ekotrope_clothes_dryer_table SET " +
            "available = :available, " +
            "defaults_type = :defaults_type, " +
            "combined_energy_factor = :combined_energy_factor, " +
            "utilization_factor = :utilization_factor, " +
            "is_changed = 1 " +
            "WHERE plan_id = :plan_id")
    void update(String plan_id, boolean available, String defaults_type,
                Double combined_energy_factor, String utilization_factor);
}

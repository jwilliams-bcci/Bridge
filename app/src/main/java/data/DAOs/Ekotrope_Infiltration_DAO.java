package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import data.Tables.Ekotrope_Infiltration_Table;

@Dao
public interface Ekotrope_Infiltration_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Infiltration_Table infiltration);

    @Query("DELETE FROM ekotrope_infiltration_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_infiltration_table WHERE plan_id = :plan_id")
    void delete(String plan_id);

    @Query("SELECT * FROM ekotrope_infiltration_table WHERE plan_id = :plan_id")
    Ekotrope_Infiltration_Table getInfiltration(String plan_id);

    @Query("UPDATE ekotrope_infiltration_table SET " +
            "cfm_50 = :cfm_50, ach_50 = :ach_50, " +
            "measurement_type = :measurement_type, is_changed = 1 " +
            "WHERE plan_id = :plan_id")
    void update(String plan_id, Double cfm_50, Double ach_50, String measurement_type);
}

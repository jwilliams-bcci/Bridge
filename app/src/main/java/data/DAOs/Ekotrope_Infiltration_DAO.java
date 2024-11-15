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
            "infiltration_unit = :infiltration_unit, infiltration_value = :infiltration_value, " +
            "measurement_type = :measurement_type, is_changed = 1 " +
            "WHERE plan_id = :plan_id")
    void update(String plan_id, String infiltration_unit, Double infiltration_value,
                String measurement_type);
}

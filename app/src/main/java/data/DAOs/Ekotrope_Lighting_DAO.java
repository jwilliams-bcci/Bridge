package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import data.Tables.Ekotrope_Lighting_Table;

@Dao
public interface Ekotrope_Lighting_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Lighting_Table lighting);

    @Query("DELETE FROM ekotrope_lighting_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_lighting_table WHERE plan_id = :plan_id")
    void delete(String plan_id);

    @Query("SELECT * FROM ekotrope_lighting_table WHERE plan_id = :plan_id")
    Ekotrope_Lighting_Table getLighting(String plan_id);

    @Query("UPDATE ekotrope_lighting_table SET " +
            "percent_interior_fluorescent = :percent_interior_fluorescent, " +
            "percent_interior_led = :percent_interior_led, " +
            "percent_exterior_fluorescent = :percent_exterior_fluorescent, " +
            "percent_exterior_led = :percent_exterior_led, " +
            "percent_garage_fluorescent = :percent_garage_fluorescent, " +
            "percent_garage_led = :percent_garage_led, is_changed = 1 " +
            "WHERE plan_id = :plan_id")
    void update(String plan_id, Double percent_interior_fluorescent, Double percent_interior_led,
                Double percent_exterior_fluorescent, Double percent_exterior_led,
                Double percent_garage_fluorescent, Double percent_garage_led);
}

package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import data.Tables.Ekotrope_RangeOven_Table;

@Dao
public interface Ekotrope_RangeOven_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_RangeOven_Table oven);

    @Query("DELETE FROM ekotrope_range_oven_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_range_oven_table WHERE plan_id = :plan_id")
    void delete(String plan_id);

    @Query("SELECT * FROM ekotrope_range_oven_table WHERE plan_id = :plan_id")
    Ekotrope_RangeOven_Table getRangeOven(String plan_id);

    @Query("UPDATE ekotrope_range_oven_table SET " +
            "fuel_type = :fuel_type, " +
            "is_induction_range = :is_induction_range, " +
            "is_convection_oven = :is_convection_oven, " +
            "is_changed = 1 " +
            "WHERE plan_id = :plan_id")
    void update(String plan_id, String fuel_type, boolean is_induction_range,
                boolean is_convection_oven);
}

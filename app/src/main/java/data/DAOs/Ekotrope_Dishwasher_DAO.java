package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import data.Tables.Ekotrope_Dishwasher_Table;

@Dao
public interface Ekotrope_Dishwasher_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Dishwasher_Table dishwasher);

    @Query("DELETE FROM ekotrope_dishwasher_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_dishwasher_table WHERE plan_id = :plan_id")
    void delete(String plan_id);

    @Query("SELECT * FROM ekotrope_dishwasher_table WHERE plan_id = :plan_id")
    Ekotrope_Dishwasher_Table getDishwasher(String plan_id);

    @Query("UPDATE ekotrope_dishwasher_table SET " +
            "dishwasher_available = :dishwasher_available, " +
            "dishwasher_defaults_type = :dishwasher_defaults_type, " +
            "dishwasher_size = :dishwasher_size, " +
            "dishwasher_efficiency_type = :dishwasher_efficiency_type, " +
            "dishwasher_efficiency = :dishwasher_efficiency, " +
            "dishwasher_annual_gas_cost = :dishwasher_annual_gas_cost, " +
            "dishwasher_gas_rate = :dishwasher_gas_rate, " +
            "dishwasher_electric_rate = :dishwasher_electric_rate, " +
            "is_changed = 1 " +
            "WHERE plan_id = :plan_id")
    void update(String plan_id, boolean dishwasher_available, String dishwasher_defaults_type,
                String dishwasher_size, String dishwasher_efficiency_type, Double dishwasher_efficiency,
                Double dishwasher_annual_gas_cost, Double dishwasher_gas_rate,
                Double dishwasher_electric_rate);
}

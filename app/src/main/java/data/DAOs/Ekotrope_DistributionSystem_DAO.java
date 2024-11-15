package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_DistributionSystem_Table;

@Dao
public interface Ekotrope_DistributionSystem_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_DistributionSystem_Table distributionSystem);

    @Query("DELETE FROM ekotrope_distribution_system_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_distribution_system_table WHERE plan_id = :plan_id and [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_distribution_system_table WHERE plan_id = :plan_id and [index] = :index")
    Ekotrope_DistributionSystem_Table getDistributionSystem(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_distribution_system_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_DistributionSystem_Table>> getDistributionSystems(String plan_id);

    @Query("SELECT * FROM ekotrope_distribution_system_table WHERE plan_id = :plan_id")
    List<Ekotrope_DistributionSystem_Table> getDistributionSystemsSync(String plan_id);

    @Query("SELECT * FROM ekotrope_distribution_system_table WHERE plan_id = :plan_id and is_changed = 1")
    List<Ekotrope_DistributionSystem_Table> getDistributionSystemsForUpdate(String plan_id);

    @Query("UPDATE ekotrope_distribution_system_table SET " +
            "is_leakage_to_outside_tested = :is_leakage_to_outside_tested, " +
            "leakage_to_outside = :leakage_to_outside, " +
            "total_leakage = :total_leakage, " +
            "total_duct_leakage_test_condition = :total_duct_leakage_test_condition, " +
            "number_of_returns = :number_of_returns, " +
            "sq_feet_served = :sq_feet_served, is_changed = 1 " +
            "WHERE plan_id = :plan_id and [index] = :index")
    void update(boolean is_leakage_to_outside_tested, Double leakage_to_outside,
                Double total_leakage, String total_duct_leakage_test_condition,
                Integer number_of_returns, Double sq_feet_served, String plan_id, int index);

}

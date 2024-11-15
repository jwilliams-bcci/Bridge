package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_MechanicalVentilation_Table;

@Dao
public interface Ekotrope_MechanicalVentilation_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_MechanicalVentilation_Table mechanicalVentilation);

    @Query("DELETE FROM ekotrope_mechanical_ventilation_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_mechanical_ventilation_table WHERE plan_id = :plan_id and [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_mechanical_ventilation_table WHERE plan_id = :plan_id and [index] = :index")
    Ekotrope_MechanicalVentilation_Table getMechanicalVentilation(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_mechanical_ventilation_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_MechanicalVentilation_Table>> getMechanicalVentilations(String plan_id);

    @Query("SELECT * FROM ekotrope_mechanical_ventilation_table WHERE plan_id = :plan_id")
    List<Ekotrope_MechanicalVentilation_Table> getMechanicalVentilationsSync(String plan_id);

    @Query("SELECT * FROM ekotrope_mechanical_ventilation_table WHERE plan_id = :plan_id and is_changed = 1")
    List<Ekotrope_MechanicalVentilation_Table> getMechanicalVentilationsForUpdate(String plan_id);

    @Query("UPDATE ekotrope_mechanical_ventilation_table SET " +
            "ventilation_type = :ventilation_type, measured_flow_rate = :measured_flow_rate, " +
            "fan_watts = :fan_watts, operational_hours_per_day = :operational_hours_per_day, " +
            "is_changed = 1 " +
            "WHERE plan_id = :plan_id and [index] = :index")
    void update(String plan_id, int index, String ventilation_type,
                Double measured_flow_rate, Double fan_watts, Double operational_hours_per_day);
}

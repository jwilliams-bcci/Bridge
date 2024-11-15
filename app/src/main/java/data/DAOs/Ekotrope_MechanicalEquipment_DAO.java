package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_MechanicalEquipment_Table;

@Dao
public interface Ekotrope_MechanicalEquipment_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_MechanicalEquipment_Table mechanicalEquipment);

    @Query("DELETE FROM ekotrope_mechanical_equipment_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_mechanical_equipment_table WHERE plan_id = :plan_id and [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_mechanical_equipment_table WHERE plan_id = :plan_id and [index] = :index")
    Ekotrope_MechanicalEquipment_Table getMechanicalEquipment(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_mechanical_equipment_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_MechanicalEquipment_Table>> getMechanicalEquipments(String plan_id);

    @Query("SELECT * FROM ekotrope_mechanical_equipment_table WHERE plan_id = :plan_id")
    List<Ekotrope_MechanicalEquipment_Table> getMechanicalEquipmentsSync(String plan_id);

    @Query("SELECT * FROM ekotrope_mechanical_equipment_table WHERE plan_id = :plan_id and is_changed = 1")
    List<Ekotrope_MechanicalEquipment_Table> getMechanicalEquipmentsForUpdate(String plan_id);

    @Query("UPDATE ekotrope_mechanical_equipment_table SET " +
            "location = :location, percent_heating_load = :percent_heating_load, percent_cooling_load = :percent_cooling_load, " +
            "percent_hot_water_load = :percent_hot_water_load, ahri_reference_number = :ahri_reference_number, " +
            "ahri_reference_fuel_type = :ahri_reference_fuel_type, rc_test_conducted = :rc_test_conducted, " +
            "rc_test_method = :rc_test_method, rc_metering_device = :rc_metering_device, " +
            "rc_difference_dtd = :rc_difference_dtd, rc_difference_ctoa = :rc_difference_ctoa, " +
            "rc_weight_deviation = :rc_weight_deviation, is_changed = 1 " +
            "WHERE plan_id = :plan_id and [index] = :index")
    void update(String location, Double percent_heating_load, Double percent_cooling_load,
                Double percent_hot_water_load, String ahri_reference_number,
                String ahri_reference_fuel_type, boolean rc_test_conducted,
                String rc_test_method, String rc_metering_device, Double rc_difference_dtd,
                Double rc_difference_ctoa, Double rc_weight_deviation, String plan_id, int index);
}
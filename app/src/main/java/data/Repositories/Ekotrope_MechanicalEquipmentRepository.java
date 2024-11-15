package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_MechanicalEquipment_DAO;
import data.Tables.Ekotrope_MechanicalEquipment_Table;

public class Ekotrope_MechanicalEquipmentRepository {
    private Ekotrope_MechanicalEquipment_DAO mMechanicalEquipmentDao;

    public Ekotrope_MechanicalEquipmentRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mMechanicalEquipmentDao = db.mEkotropeMechanicalEquipmentDao();
    }

    public void insert(Ekotrope_MechanicalEquipment_Table mechanicalEquipment) {
        mMechanicalEquipmentDao.insert(mechanicalEquipment);
    }

    public void update(Ekotrope_MechanicalEquipment_Table mechanicalEquipment) {
        mMechanicalEquipmentDao.update(mechanicalEquipment.location, mechanicalEquipment.percent_heating_load,
                mechanicalEquipment.percent_cooling_load, mechanicalEquipment.percent_hot_water_load,
                mechanicalEquipment.ahri_reference_number, mechanicalEquipment.ahri_reference_fuel_type,
                mechanicalEquipment.rc_test_conducted, mechanicalEquipment.rc_test_method,
                mechanicalEquipment.rc_metering_device, mechanicalEquipment.rc_difference_dtd,
                mechanicalEquipment.rc_difference_ctoa, mechanicalEquipment.rc_weight_deviation,
                mechanicalEquipment.plan_id, mechanicalEquipment.index);
    }

    public LiveData<List<Ekotrope_MechanicalEquipment_Table>> getMechanicalEquipments(String plan_id) {
        return mMechanicalEquipmentDao.getMechanicalEquipments(plan_id);
    }

    public List<Ekotrope_MechanicalEquipment_Table> getMechanicalEquipmentsSync(String plan_id) {
        return mMechanicalEquipmentDao.getMechanicalEquipmentsSync(plan_id);
    }

    public List<Ekotrope_MechanicalEquipment_Table> getMechanicalEquipmentsForUpdate(String plan_id) {
        return mMechanicalEquipmentDao.getMechanicalEquipmentsForUpdate(plan_id);
    }

    public Ekotrope_MechanicalEquipment_Table getMechanicalEquipment(String plan_id, int index) {
        return mMechanicalEquipmentDao.getMechanicalEquipment(plan_id, index);
    }
}

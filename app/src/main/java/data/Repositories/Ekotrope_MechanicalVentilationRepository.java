package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_MechanicalVentilation_DAO;
import data.Tables.Ekotrope_MechanicalVentilation_Table;

public class Ekotrope_MechanicalVentilationRepository {
    private Ekotrope_MechanicalVentilation_DAO mMechanicalVentilationDao;

    public Ekotrope_MechanicalVentilationRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mMechanicalVentilationDao = db.mEkotropeMechanicalVentilationDao();
    }

    public void insert(Ekotrope_MechanicalVentilation_Table mechanicalVentilation) {
        mMechanicalVentilationDao.insert(mechanicalVentilation);
    }

    public void update(Ekotrope_MechanicalVentilation_Table mechanicalVentilation) {
        mMechanicalVentilationDao.update(mechanicalVentilation.plan_id,
                mechanicalVentilation.index, mechanicalVentilation.ventilation_type,
                mechanicalVentilation.measured_flow_rate, mechanicalVentilation.fan_watts,
                mechanicalVentilation.operational_hours_per_day);
    }

    public LiveData<List<Ekotrope_MechanicalVentilation_Table>> getMechanicalVentilations(String plan_id) {
        return mMechanicalVentilationDao.getMechanicalVentilations(plan_id);
    }

    public List<Ekotrope_MechanicalVentilation_Table> getMechanicalVentilationsSync(String plan_id) {
        return mMechanicalVentilationDao.getMechanicalVentilationsSync(plan_id);
    }

    public List<Ekotrope_MechanicalVentilation_Table> getMechanicalVentilationsForUpdate(String plan_id) {
        return mMechanicalVentilationDao.getMechanicalVentilationsForUpdate(plan_id);
    }

    public Ekotrope_MechanicalVentilation_Table getMechanicalVentilation(String mPlanId, int mMechanicalVentilationIndex) {
        return mMechanicalVentilationDao.getMechanicalVentilation(mPlanId, mMechanicalVentilationIndex);
    }
}

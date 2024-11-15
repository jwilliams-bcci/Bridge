package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_DistributionSystem_DAO;
import data.Tables.Ekotrope_DistributionSystem_Table;

public class Ekotrope_DistributionSystemRepository {
    private Ekotrope_DistributionSystem_DAO mDistributionSystemDao;

    public Ekotrope_DistributionSystemRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mDistributionSystemDao = db.mEkotropeDistributionSystemDao();
    }

    public void insert(Ekotrope_DistributionSystem_Table distributionSystem) {
        mDistributionSystemDao.insert(distributionSystem);
    }

    public void update(Ekotrope_DistributionSystem_Table distributionSystem) {
        mDistributionSystemDao.update(distributionSystem.is_leakage_to_outside_tested,
                distributionSystem.leakage_to_outside, distributionSystem.total_leakage,
                distributionSystem.total_duct_leakage_test_condition,
                distributionSystem.number_of_returns, distributionSystem.sq_feet_served,
                distributionSystem.plan_id, distributionSystem.index);
    }

    public LiveData<List<Ekotrope_DistributionSystem_Table>> getDistributionSystems(String plan_id) {
        return mDistributionSystemDao.getDistributionSystems(plan_id);
    }

    public List<Ekotrope_DistributionSystem_Table> getDistributionSystemsSync(String plan_id) {
        return mDistributionSystemDao.getDistributionSystemsSync(plan_id);
    }

    public List<Ekotrope_DistributionSystem_Table> getDistributionSystemsForUpdate(String plan_id) {
        return mDistributionSystemDao.getDistributionSystemsForUpdate(plan_id);
    }

    public Ekotrope_DistributionSystem_Table getDistributionSystem(String plan_id, int index) {
        return mDistributionSystemDao.getDistributionSystem(plan_id, index);
    }
}

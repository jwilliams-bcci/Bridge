package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_ClothesDryer_DAO;
import data.Tables.Ekotrope_ClothesDryer_Table;

public class Ekotrope_ClothesDryerRepository {
    private Ekotrope_ClothesDryer_DAO mClothesDryersDao;

    public Ekotrope_ClothesDryerRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mClothesDryersDao = db.mEkotropeClothesDryerDao();
    }

    public void insert(Ekotrope_ClothesDryer_Table clothesDryer) {
        mClothesDryersDao.insert(clothesDryer);
    }

    public void update(Ekotrope_ClothesDryer_Table clothesDryer) {
        mClothesDryersDao.update(clothesDryer.plan_id, clothesDryer.available,
                clothesDryer.defaults_type, clothesDryer.combined_energy_factor,
                clothesDryer.utilization_factor);
    }

    public Ekotrope_ClothesDryer_Table getClothesDryer(String mPlanId) {
        return mClothesDryersDao.getClothesDryer(mPlanId);
    }
}

package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Refrigerator_DAO;
import data.Tables.Ekotrope_Refrigerator_Table;

public class Ekotrope_RefrigeratorRepository {
    private Ekotrope_Refrigerator_DAO mRefrigeratorDao;

    public Ekotrope_RefrigeratorRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mRefrigeratorDao = db.mEkotropeRefrigeratorDao();
    }

    public void insert(Ekotrope_Refrigerator_Table refrigerator) {
        mRefrigeratorDao.insert(refrigerator);
    }

    public void update(Ekotrope_Refrigerator_Table refrigerator) {
        mRefrigeratorDao.update(refrigerator.plan_id, refrigerator.refrigerator_consumption);
    }

    public Ekotrope_Refrigerator_Table getRefrigerator(String mPlanId) {
        return mRefrigeratorDao.getRefrigerator(mPlanId);
    }
}

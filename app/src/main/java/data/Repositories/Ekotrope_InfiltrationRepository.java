package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Infiltration_DAO;
import data.Tables.Ekotrope_Infiltration_Table;

public class Ekotrope_InfiltrationRepository {
    private Ekotrope_Infiltration_DAO mEkotropeInfiltrationDao;

    public Ekotrope_InfiltrationRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mEkotropeInfiltrationDao = db.mEkotropeInfiltrationDao();
    }

    public void insert(Ekotrope_Infiltration_Table infiltration) {
        mEkotropeInfiltrationDao.insert(infiltration);
    }

    public void update(Ekotrope_Infiltration_Table infiltration) {
        mEkotropeInfiltrationDao.update(infiltration.plan_id, infiltration.cfm_50,
                infiltration.ach_50, infiltration.measurement_type);
    }

    public Ekotrope_Infiltration_Table getInfiltration(String mPlanId) {
        return mEkotropeInfiltrationDao.getInfiltration(mPlanId);
    }
}

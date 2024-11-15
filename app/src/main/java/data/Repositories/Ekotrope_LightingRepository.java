package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Lighting_DAO;
import data.Tables.Ekotrope_Lighting_Table;

public class Ekotrope_LightingRepository {
    private Ekotrope_Lighting_DAO mLightingDao;

    public Ekotrope_LightingRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mLightingDao = db.mEkotropeLightingDao();
    }

    public void insert(Ekotrope_Lighting_Table lighting) {
        mLightingDao.insert(lighting);
    }

    public void update(Ekotrope_Lighting_Table lighting) {
        mLightingDao.update(lighting.plan_id, lighting.percent_interior_fluorescent,
                lighting.percent_interior_led, lighting.percent_exterior_fluorescent,
                lighting.percent_exterior_led, lighting.percent_garage_fluorescent,
                lighting.percent_garage_led);
    }

    public Ekotrope_Lighting_Table getLighting(String mPlanId) {
        return mLightingDao.getLighting(mPlanId);
    }
}

package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Ceiling_DAO;
import data.Tables.Ekotrope_Ceiling_Table;

public class Ekotrope_CeilingRepository {
    private Ekotrope_Ceiling_DAO mCeilingDao;

    public Ekotrope_CeilingRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mCeilingDao = db.mCeilingDao();
    }

    public void insert(Ekotrope_Ceiling_Table ceiling) {
        mCeilingDao.insert(ceiling);
    }

    public void update(Ekotrope_Ceiling_Table ceiling) {
        mCeilingDao.update(ceiling.plan_id, ceiling.index, ceiling.name, ceiling.typeName,
                ceiling.cavityInsulationGrade, ceiling.cavityInsulationR,
                ceiling.continuousInsulationR, ceiling.studSpacing,
                ceiling.studWidth, ceiling.studDepth, ceiling.studMaterial,
                ceiling.hasRadiantBarrier);
    }

    public LiveData<List<Ekotrope_Ceiling_Table>> getCeilings(String plan_id) {
        return mCeilingDao.getCeilings(plan_id);
    }

    public List<Ekotrope_Ceiling_Table> getCeilingsSync(String plan_id) {
        return mCeilingDao.getCeilingsSync(plan_id);
    }

    public Ekotrope_Ceiling_Table getCeiling(String mPlanId, int mCeilingIndex) {
        return mCeilingDao.getCeiling(mPlanId, mCeilingIndex);
    }
}

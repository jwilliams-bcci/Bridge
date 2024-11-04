package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Slab_DAO;
import data.Tables.Ekotrope_Slab_Table;

public class Ekotrope_SlabRepository {
    private Ekotrope_Slab_DAO mSlabDao;

    public Ekotrope_SlabRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mSlabDao = db.mSlabDao();
    }

    public void insert(Ekotrope_Slab_Table slab) {
        mSlabDao.insert(slab);
    }

    public void update(Ekotrope_Slab_Table slab) {
        mSlabDao.update(slab.plan_id, slab.index, slab.name, slab.typeName,
                slab.underslabInsulationR, slab.isFullyInsulated, slab.underslabInsulationWidth,
                slab.perimeterInsulationDepth, slab.perimeterInsulationR, slab.thermalBreak);
    }

    public LiveData<List<Ekotrope_Slab_Table>> getSlabs(String plan_id) {
        return mSlabDao.getSlabs(plan_id);
    }

    public List<Ekotrope_Slab_Table> getSlabsSync(String plan_id) {
        return mSlabDao.getSlabsSync(plan_id);
    }

    public List<Ekotrope_Slab_Table> getSlabsForUpdate(String plan_id) {
        return mSlabDao.getSlabsForUpdate(plan_id);
    }

    public Ekotrope_Slab_Table getSlab(String plan_id, int index) {
        return mSlabDao.getSlab(plan_id, index);
    }
}

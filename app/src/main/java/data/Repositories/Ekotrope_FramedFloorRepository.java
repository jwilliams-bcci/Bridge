package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_FramedFloor_DAO;
import data.Tables.Ekotrope_FramedFloor_Table;

public class Ekotrope_FramedFloorRepository {
    private Ekotrope_FramedFloor_DAO mFramedFloorsDao;

    public Ekotrope_FramedFloorRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mFramedFloorsDao = db.mEkotropeFramedFloorDao();
    }

    public void insert(Ekotrope_FramedFloor_Table framedFloor) {
        mFramedFloorsDao.insert(framedFloor);
    }

    public void update(Ekotrope_FramedFloor_Table framedFloor) {
        mFramedFloorsDao.update(framedFloor.plan_id, framedFloor.index, framedFloor.cavityInsulationGrade, framedFloor.cavityInsulationR,
                framedFloor.studSpacing, framedFloor.continuousInsulationR, framedFloor.studWidth, framedFloor.studDepth, framedFloor.studMaterial);
    }

    public LiveData<List<Ekotrope_FramedFloor_Table>> getFramedFloors(String plan_id) {
        return mFramedFloorsDao.getFramedFloors(plan_id);
    }

    public List<Ekotrope_FramedFloor_Table> getFramedFloorsSync(String plan_id) {
        return mFramedFloorsDao.getFramedFloorsSync(plan_id);
    }

    public List<Ekotrope_FramedFloor_Table> getFramedFloorsForUpdate(String plan_id) {
        return mFramedFloorsDao.getFramedFloorsForUpdate(plan_id);
    }

    public Ekotrope_FramedFloor_Table getFramedFloor(String mPlanId, int mFramedFloorIndex) {
        return mFramedFloorsDao.getFramedFloor(mPlanId, mFramedFloorIndex);
    }
}

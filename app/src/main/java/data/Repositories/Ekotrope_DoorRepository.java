package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Door_DAO;
import data.Tables.Ekotrope_Door_Table;

public class Ekotrope_DoorRepository {
    private Ekotrope_Door_DAO mDoorDao;

    public Ekotrope_DoorRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mDoorDao = db.mDoorDao();
    }

    public void insert(Ekotrope_Door_Table door) {
        mDoorDao.insert(door);
    }

    public void update(Ekotrope_Door_Table door) {
        mDoorDao.update(door.plan_id, door.index, door.name, door.typeName, door.remove,
                door.installedWallIndex, door.installedFoundationWallIndex, door.doorArea,
                door.uFactor);
    }

    public LiveData<List<Ekotrope_Door_Table>> getDoors(String plan_id) {
        return mDoorDao.getDoors(plan_id);
    }

    public List<Ekotrope_Door_Table> getDoorsSync(String plan_id) {
        return mDoorDao.getDoorsSync(plan_id);
    }

    public Ekotrope_Door_Table getDoor(String mPlanId, int mDoorIndex) {
        return mDoorDao.getDoor(mPlanId, mDoorIndex);
    }
}

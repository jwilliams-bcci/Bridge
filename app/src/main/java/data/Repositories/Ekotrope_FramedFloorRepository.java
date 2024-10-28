package data.Repositories;

import android.app.Application;

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

    public List<Ekotrope_FramedFloor_Table> getFramedFloors(String plan_id) {
        return mFramedFloorsDao.getFramedFloors(plan_id);
    }

    public void insert(Ekotrope_FramedFloor_Table framedFloor) {
        mFramedFloorsDao.insert(framedFloor);
    }
}

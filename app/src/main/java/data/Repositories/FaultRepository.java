package data.Repositories;

import android.app.Application;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Fault_DAO;
import data.Tables.Fault_Table;

public class FaultRepository {
    private Fault_DAO mFaultDao;

    public FaultRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mFaultDao = db.mFaultDao();
    }

    public List<Fault_Table> getFaults() {
        return mFaultDao.getFaults();
    }

    public void insert(Fault_Table fault) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mFaultDao.insert(fault);
        });
    }
}

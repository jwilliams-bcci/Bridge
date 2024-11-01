package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_RimJoist_DAO;
import data.Tables.Ekotrope_RimJoist_Table;

public class Ekotrope_RimJoistRepository {
    private Ekotrope_RimJoist_DAO mRimJoistDao;

    public Ekotrope_RimJoistRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mRimJoistDao = db.mRimJoistDao();
    }

    public void insert(Ekotrope_RimJoist_Table rimJoist) {
        mRimJoistDao.insert(rimJoist);
    }

    public LiveData<List<Ekotrope_RimJoist_Table>> getRimJoists(String plan_id) {
        return mRimJoistDao.getRimJoists(plan_id);
    }

    public List<Ekotrope_RimJoist_Table> getRimJoistsSync(String plan_id) {
        return mRimJoistDao.getRimJoistsSync(plan_id);
    }

    public Ekotrope_RimJoist_Table getRimJoist(String plan_id, int index) {
        return mRimJoistDao.getRimJoist(plan_id, index);
    }
}

package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Duct_DAO;
import data.Tables.Ekotrope_Duct_Table;

public class Ekotrope_DuctRepository {
    private Ekotrope_Duct_DAO mDuctDao;

    public Ekotrope_DuctRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mDuctDao = db.mEkotropeDuctDao();
    }

    public void insert(Ekotrope_Duct_Table duct) {
        mDuctDao.insert(duct);
    }

    public void update(Ekotrope_Duct_Table duct) {
        mDuctDao.update(duct.location, duct.percent_supply_area, duct.percent_return_area,
                duct.plan_id, duct.ds_id, duct.index);
    }

    public LiveData<List<Ekotrope_Duct_Table>> getDucts(String plan_id, int ds_id) {
        return mDuctDao.getDucts(plan_id, ds_id);
    }

    public List<Ekotrope_Duct_Table> getDuctsSync(String plan_id, int ds_id) {
        return mDuctDao.getDuctsSync(plan_id, ds_id);
    }

    public List<Ekotrope_Duct_Table> getDuctsForUpdate(String plan_id, int ds_id) {
        return mDuctDao.getDuctsForUpdate(plan_id, ds_id);
    }

    public Ekotrope_Duct_Table getDuct(String plan_id, int ds_id, int index) {
        return mDuctDao.getDuct(plan_id, ds_id, index);
    }
}

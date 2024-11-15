package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_RangeOven_DAO;
import data.Tables.Ekotrope_RangeOven_Table;

public class Ekotrope_RangeOvenRepository {
    private Ekotrope_RangeOven_DAO mRangeOvenDao;

    public Ekotrope_RangeOvenRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mRangeOvenDao = db.mEkotropeRangeOvenDao();
    }

    public void insert(Ekotrope_RangeOven_Table rangeOven) {
        mRangeOvenDao.insert(rangeOven);
    }

    public void update(Ekotrope_RangeOven_Table rangeOven) {
        mRangeOvenDao.update(rangeOven.plan_id, rangeOven.fuel_type, rangeOven.is_induction_range,
                rangeOven.is_convection_oven);
    }

    public Ekotrope_RangeOven_Table getRangeOven(String plan_id) {
        return mRangeOvenDao.getRangeOven(plan_id);
    }
}

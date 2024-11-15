package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Dishwasher_DAO;
import data.Tables.Ekotrope_Dishwasher_Table;

public class Ekotrope_DishwasherRepository {
    private Ekotrope_Dishwasher_DAO mDishwasherDao;

    public Ekotrope_DishwasherRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mDishwasherDao = db.mEkotropeDishwasherDao();
    }

    public void insert(Ekotrope_Dishwasher_Table dishwasher) {
        mDishwasherDao.insert(dishwasher);
    }

    public void update(Ekotrope_Dishwasher_Table dishwasher) {
        mDishwasherDao.update(dishwasher.plan_id, dishwasher.dishwasher_available,
                dishwasher.dishwasher_defaults_type, dishwasher.dishwasher_size,
                dishwasher.dishwasher_efficiency_type, dishwasher.dishwasher_efficiency,
                dishwasher.dishwasher_annual_gas_cost, dishwasher.dishwasher_gas_rate,
                dishwasher.dishwasher_electric_rate);
    }

    public Ekotrope_Dishwasher_Table getDishwasher(String mPlanId) {
        return mDishwasherDao.getDishwasher(mPlanId);
    }
}

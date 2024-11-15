package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_ClothesWasher_DAO;
import data.Tables.Ekotrope_ClothesWasher_Table;

public class Ekotrope_ClothesWasherRepository {
    private Ekotrope_ClothesWasher_DAO mClothesWasherDao;

    public Ekotrope_ClothesWasherRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mClothesWasherDao = db.mEkotropeClothesWasherDao();
    }

    public void insert(Ekotrope_ClothesWasher_Table clothesWasher) {
        mClothesWasherDao.insert(clothesWasher);
    }

    public void update(Ekotrope_ClothesWasher_Table clothesWasher) {
        mClothesWasherDao.update(clothesWasher.plan_id, clothesWasher.available,
                clothesWasher.defaults_type, clothesWasher.load_type,
                clothesWasher.labeled_energy_rating, clothesWasher.integrated_modified_energy_factor);
    }

    public Ekotrope_ClothesWasher_Table getClothesWasher(String plan_id) {
        return mClothesWasherDao.getClothesWasher(plan_id);
    }
}

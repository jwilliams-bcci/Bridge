package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.MultifamilyDetails_DAO;
import data.Tables.MultifamilyDetails_Table;

public class MultifamilyDetailsRepository {
    private MultifamilyDetails_DAO mMultifamilyDetailsDao;

    public MultifamilyDetailsRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mMultifamilyDetailsDao = db.mMultifamilyDetailsDao();
    }

    public MultifamilyDetails_Table getMultifamilyDetails(int inspection_id) {
        return mMultifamilyDetailsDao.getMultifamilyDetails(inspection_id);
    }

    public void insertMultifamilyDetails(MultifamilyDetails_Table multifamilyDetails) {
        mMultifamilyDetailsDao.insert(multifamilyDetails);
    }

    public void updateMultifamilyDetails(int id, String builder_personnel, String burgess_personnel, String area_observed, String temperature, String weather_conditions) {
        mMultifamilyDetailsDao.updateMultifamilyDetails(id, builder_personnel, burgess_personnel, area_observed, temperature, weather_conditions);
    }
}

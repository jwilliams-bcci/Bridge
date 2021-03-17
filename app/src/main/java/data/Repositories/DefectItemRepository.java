package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.DefectItem_DAO;
import data.Tables.DefectItem_Table;

public class DefectItemRepository {
    private DefectItem_DAO mDefectItemDao;

    public DefectItemRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mDefectItemDao = db.mDefectItemDao();
    }

    public LiveData<DefectItem_Table> getDefectItem(int defect_item_id) {
        return mDefectItemDao.getDefectItem(defect_item_id);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItems() {
        return mDefectItemDao.getDefectItems();
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFiltered(String categoryName) {
        return mDefectItemDao.getDefectItemsFiltered(categoryName);
    }

    public LiveData<List<String>> getDefectCategories() {
        return mDefectItemDao.getDefectCategories();
    }

    public void insert(DefectItem_Table defectItem) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDefectItemDao.insert(defectItem);
        });
    }
}

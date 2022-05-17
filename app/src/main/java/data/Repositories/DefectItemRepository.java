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

    public LiveData<List<DefectItem_Table>> getAllDefectItemsNumberSort(int inspection_type_id) {
        return mDefectItemDao.getDefectItemsNumberSort(inspection_type_id);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsDescriptionSort(int inspection_type_id) {
        return mDefectItemDao.getDefectItemsDescriptionSort(inspection_type_id);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFilteredNumberSort(String category_name, int inspection_type_id) {
        return mDefectItemDao.getDefectItemsFilteredNumberSort(category_name, inspection_type_id);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFilteredDescriptionSort(String category_name, int inspection_type_id) {
        return mDefectItemDao.getDefectItemsFilteredDescriptionSort(category_name, inspection_type_id);
    }

    public LiveData<List<String>> getDefectCategories(int inspection_type_id) {
        return mDefectItemDao.getDefectCategories(inspection_type_id);
    }

    public void insert(DefectItem_Table defectItem) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDefectItemDao.insert(defectItem);
        });
    }

    public DefectItem_Table getDefectItemSync(int defect_item_id) {
        return mDefectItemDao.getDefectItemSync(defect_item_id);
    }
}

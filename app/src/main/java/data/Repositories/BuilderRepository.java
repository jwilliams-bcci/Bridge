package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Builder_DAO;
import data.Tables.Builder_Table;

public class BuilderRepository {
    private Builder_DAO mBuilderDao;

    public BuilderRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mBuilderDao = db.mBuilderDao();
    }

    public LiveData<List<Builder_Table>> getBuilders() {
        return mBuilderDao.getBuilders();
    }

    public Builder_Table getBuilder(int builderId) {
        return mBuilderDao.getBuilder(builderId);
    }

    public void insert(Builder_Table builder) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mBuilderDao.insert(builder);
        });
    }
}

package data.Repositories;

import android.app.Application;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Direction_DAO;
import data.Tables.Direction_Table;

public class DirectionRepository {
    private Direction_DAO mDirectionDao;

    public DirectionRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mDirectionDao = db.mDirectionDao();
    }

    public List<Direction_Table> getDirections() {
        return mDirectionDao.getDirections();
    }

    public void insert(Direction_Table direction) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDirectionDao.insert(direction);
        });
    }
}

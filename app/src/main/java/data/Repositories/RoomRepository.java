package data.Repositories;

import android.app.Application;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Room_DAO;
import data.Tables.Room_Table;

public class RoomRepository {
    private Room_DAO mRoomDao;

    public RoomRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mRoomDao = db.mRoomDao();
    }

    public List<Room_Table> getRooms() {
        return mRoomDao.getRooms();
    }

    public void insert(Room_Table room) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRoomDao.insert(room);
        });
    }
}

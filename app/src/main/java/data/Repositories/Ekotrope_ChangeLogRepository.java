package data.Repositories;

import android.app.Application;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_ChangeLog_DAO;
import data.Tables.Ekotrope_ChangeLog_Table;

public class Ekotrope_ChangeLogRepository {
    private Ekotrope_ChangeLog_DAO mEkotropeChangeLogDao;

    public Ekotrope_ChangeLogRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mEkotropeChangeLogDao = db.mEkotropeChangeLogDao();
    }

    public void insert(Ekotrope_ChangeLog_Table changeLog) {
        mEkotropeChangeLogDao.insert(changeLog);
    }

    public List<Ekotrope_ChangeLog_Table> getEkotropeChanges(int inspection_id) {
        return mEkotropeChangeLogDao.getEkotropeChanges(inspection_id);
    }
}

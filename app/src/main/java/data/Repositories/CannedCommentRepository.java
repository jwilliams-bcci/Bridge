package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.CannedComment_DAO;
import data.Tables.CannedComment_Table;

public class CannedCommentRepository {
    private CannedComment_DAO mCannedCommentDao;

    public CannedCommentRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mCannedCommentDao = db.mCannedCommentDao();
    }

    public LiveData<List<String>> getCannedComments() {
        return mCannedCommentDao.getCannedComments();
    }

    public void insert(CannedComment_Table cannedComment) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCannedCommentDao.insert(cannedComment);
        });
    }
}

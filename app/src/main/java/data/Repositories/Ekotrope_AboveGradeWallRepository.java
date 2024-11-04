package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_AboveGradeWall_DAO;
import data.Tables.Ekotrope_AboveGradeWall_Table;

public class Ekotrope_AboveGradeWallRepository {
    private Ekotrope_AboveGradeWall_DAO mAboveGradeWallsDao;

    public Ekotrope_AboveGradeWallRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mAboveGradeWallsDao = db.mEkotropeAboveGradeWallsDao();
    }

    public void insert(Ekotrope_AboveGradeWall_Table aboveGradeWall) {
        mAboveGradeWallsDao.insert(aboveGradeWall);
    }

    public void update(Ekotrope_AboveGradeWall_Table aboveGradeWall) {
        mAboveGradeWallsDao.update(aboveGradeWall.plan_id, aboveGradeWall.index,
                aboveGradeWall.cavityInsulationGrade, aboveGradeWall.cavityInsulationR,
                aboveGradeWall.studSpacing, aboveGradeWall.continuousInsulationR, aboveGradeWall.studWidth,
                aboveGradeWall.studDepth, aboveGradeWall.studMaterial);
    }

    public LiveData<List<Ekotrope_AboveGradeWall_Table>> getAboveGradeWalls(String plan_id) {
        return mAboveGradeWallsDao.getAboveGradeWalls(plan_id);
    }

    public List<Ekotrope_AboveGradeWall_Table> getAboveGradeWallsSync(String plan_id) {
        return mAboveGradeWallsDao.getAboveGradeWallsSync(plan_id);
    }

    public List<Ekotrope_AboveGradeWall_Table> getAboveGradeWallsForUpdate(String plan_id) {
        return mAboveGradeWallsDao.getAboveGradeWallsForUpdate(plan_id);
    }

    public Ekotrope_AboveGradeWall_Table getAboveGradeWall(String mPlanId, int mAboveGradeWallIndex) {
        return mAboveGradeWallsDao.getAboveGradeWall(mPlanId, mAboveGradeWallIndex);
    }
}

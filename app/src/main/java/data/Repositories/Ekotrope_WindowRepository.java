package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Window_DAO;
import data.Tables.Ekotrope_Window_Table;

public class Ekotrope_WindowRepository {
    private Ekotrope_Window_DAO mWindowDao;

    public Ekotrope_WindowRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mWindowDao = db.mWindowDao();
    }

    public void insert(Ekotrope_Window_Table window) {
        mWindowDao.insert(window);
    }

    public void update(Ekotrope_Window_Table window) {
        mWindowDao.update(window.plan_id, window.index, window.name, window.remove, window.windowArea,
                window.orientation, window.installedWallIndex, window.installedFoundationWallIndex,
                window.overhangDepth, window.distanceOverhangToTop, window.distanceOverhangToBottom,
                window.SHGC, window.uFactor, window.adjacentSummerShading, window.adjacentWinterShading);
    }

    public LiveData<List<Ekotrope_Window_Table>> getWindows(String plan_id) {
        return mWindowDao.getWindows(plan_id);
    }

    public List<Ekotrope_Window_Table> getWindowsSync(String plan_id) {
        return mWindowDao.getWindowsSync(plan_id);
    }

    public List<Ekotrope_Window_Table> getWindowsForUpdate(String plan_id) {
        return mWindowDao.getWindowsForUpdate(plan_id);
    }

    public Ekotrope_Window_Table getWindow(String mPlanId, int mWindowIndex) {
        return mWindowDao.getWindow(mPlanId, mWindowIndex);
    }
}

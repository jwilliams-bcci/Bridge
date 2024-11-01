package com.burgess.bridge.ekotropedata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import data.Repositories.Ekotrope_AboveGradeWallRepository;
import data.Repositories.Ekotrope_CeilingRepository;
import data.Repositories.Ekotrope_DoorRepository;
import data.Repositories.Ekotrope_FramedFloorRepository;
import data.Repositories.Ekotrope_SlabRepository;
import data.Repositories.Ekotrope_WindowRepository;
import data.Repositories.InspectionRepository;
import data.Tables.Ekotrope_AboveGradeWall_Table;
import data.Tables.Ekotrope_Ceiling_Table;
import data.Tables.Ekotrope_Door_Table;
import data.Tables.Ekotrope_FramedFloor_Table;
import data.Tables.Ekotrope_Slab_Table;
import data.Tables.Ekotrope_Window_Table;
import data.Tables.Inspection_Table;

public class EkotropeDataViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private Ekotrope_AboveGradeWallRepository mAboveGradeWallsRepository;
    private Ekotrope_FramedFloorRepository mFramedFloorsRepository;
    private Ekotrope_DoorRepository mDoorsRepository;
    private Ekotrope_WindowRepository mWindowsRepository;
    private Ekotrope_CeilingRepository mCeilingsRepository;
    private Ekotrope_SlabRepository mSlabsRepository;

    public EkotropeDataViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mAboveGradeWallsRepository = new Ekotrope_AboveGradeWallRepository(application);
        mFramedFloorsRepository = new Ekotrope_FramedFloorRepository(application);
        mDoorsRepository = new Ekotrope_DoorRepository(application);
        mWindowsRepository = new Ekotrope_WindowRepository(application);
        mCeilingsRepository = new Ekotrope_CeilingRepository(application);
        mSlabsRepository = new Ekotrope_SlabRepository(application);
    }

    public Inspection_Table getInspectionSync(int inspection_id) {
        return mInspectionRepository.getInspectionSync(inspection_id);
    }

    public JSONObject getInspectionSyncJson(String plan_id, String project_id) {
        JSONObject topLevelRequest = new JSONObject();
        JSONObject inspectionSyncObj = new JSONObject();
        JSONArray aboveGradeWallsJson = new JSONArray();
        JSONArray framedFloorsJson = new JSONArray();
        JSONArray doorsJson = new JSONArray();
        JSONArray windowsJson = new JSONArray();
        JSONArray ceilingsJson = new JSONArray();
        JSONArray slabsJson = new JSONArray();

        List<Ekotrope_AboveGradeWall_Table> aboveGradeWalls = mAboveGradeWallsRepository.getAboveGradeWallsSync(plan_id);
        List<Ekotrope_FramedFloor_Table> framedFloors = mFramedFloorsRepository.getFramedFloorsSync(plan_id);
        List<Ekotrope_Door_Table> doors = mDoorsRepository.getDoorsSync(plan_id);
        List<Ekotrope_Window_Table> windows = mWindowsRepository.getWindowsSync(plan_id);
        List<Ekotrope_Ceiling_Table> ceilings = mCeilingsRepository.getCeilingsSync(plan_id);
        List<Ekotrope_Slab_Table> slabs = mSlabsRepository.getSlabsSync(plan_id);

        try {
            topLevelRequest.put("additionalEmail", "bwallace@burgess-inc.com");
            inspectionSyncObj.put("projectId", project_id.trim());
            for (Ekotrope_AboveGradeWall_Table aboveGradeWall : aboveGradeWalls) {
                aboveGradeWallsJson.put(aboveGradeWall.toJsonObj());
            }
            inspectionSyncObj.put("aboveGradeWalls", aboveGradeWallsJson);

            for (Ekotrope_FramedFloor_Table framedFloor : framedFloors) {
                framedFloorsJson.put(framedFloor.toJsonObj());
            }
            inspectionSyncObj.put("framedFloors", framedFloorsJson);

            for (Ekotrope_Door_Table door : doors) {
                doorsJson.put(door.toJsonObj());
            }
            inspectionSyncObj.put("doors", doorsJson);

            for (Ekotrope_Window_Table window : windows) {
                windowsJson.put(window.toJsonObj());
            }
            inspectionSyncObj.put("windows", windowsJson);

            for (Ekotrope_Ceiling_Table ceiling : ceilings) {
                ceilingsJson.put(ceiling.toJsonObj());
            }
            inspectionSyncObj.put("ceilings", ceilingsJson);

            for (Ekotrope_Slab_Table slab : slabs) {
                slabsJson.put(slab.toJsonObj());
            }
            inspectionSyncObj.put("slabs", slabsJson);

            topLevelRequest.put("inspectionSync", inspectionSyncObj);
        } catch (Exception e) {
        }
        return topLevelRequest;
    }
}
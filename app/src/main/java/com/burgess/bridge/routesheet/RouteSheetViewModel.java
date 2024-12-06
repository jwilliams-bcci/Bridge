package com.burgess.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.SharedPreferencesRepository;
import com.burgess.bridge.apiqueue.BridgeAPIQueue;
import com.burgess.bridge.apiqueue.RouteSheetAPI;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Repositories.DefectItem_InspectionType_XRefRepository;
import data.Repositories.Ekotrope_AboveGradeWallRepository;
import data.Repositories.Ekotrope_CeilingRepository;
import data.Repositories.Ekotrope_ClothesDryerRepository;
import data.Repositories.Ekotrope_ClothesWasherRepository;
import data.Repositories.Ekotrope_DishwasherRepository;
import data.Repositories.Ekotrope_DistributionSystemRepository;
import data.Repositories.Ekotrope_DoorRepository;
import data.Repositories.Ekotrope_DuctRepository;
import data.Repositories.Ekotrope_FramedFloorRepository;
import data.Repositories.Ekotrope_InfiltrationRepository;
import data.Repositories.Ekotrope_LightingRepository;
import data.Repositories.Ekotrope_MechanicalEquipmentRepository;
import data.Repositories.Ekotrope_MechanicalVentilationRepository;
import data.Repositories.Ekotrope_RangeOvenRepository;
import data.Repositories.Ekotrope_RefrigeratorRepository;
import data.Repositories.Ekotrope_RimJoistRepository;
import data.Repositories.Ekotrope_SlabRepository;
import data.Repositories.Ekotrope_WindowRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Repositories.PastInspectionRepository;
import data.Repositories.SubmitRequestRepository;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.Ekotrope_AboveGradeWall_Table;
import data.Tables.Ekotrope_Ceiling_Table;
import data.Tables.Ekotrope_ClothesDryer_Table;
import data.Tables.Ekotrope_ClothesWasher_Table;
import data.Tables.Ekotrope_Dishwasher_Table;
import data.Tables.Ekotrope_DistributionSystem_Table;
import data.Tables.Ekotrope_Door_Table;
import data.Tables.Ekotrope_Duct_Table;
import data.Tables.Ekotrope_FramedFloor_Table;
import data.Tables.Ekotrope_Infiltration_Table;
import data.Tables.Ekotrope_Lighting_Table;
import data.Tables.Ekotrope_MechanicalEquipment_Table;
import data.Tables.Ekotrope_MechanicalVentilation_Table;
import data.Tables.Ekotrope_RangeOven_Table;
import data.Tables.Ekotrope_Refrigerator_Table;
import data.Tables.Ekotrope_RimJoist_Table;
import data.Tables.Ekotrope_Slab_Table;
import data.Tables.Ekotrope_Window_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;
import data.Tables.PastInspection_Table;
import data.Views.RouteSheet_View;

public class RouteSheetViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;
    private InspectionDefectRepository mInspectionDefectRepository;
    private DefectItemRepository mDefectItemRepository;
    private DefectItem_InspectionType_XRefRepository mDIITRepository;
    private PastInspectionRepository mPastInspectionRepository;
    private Ekotrope_FramedFloorRepository mEkotropeFramedFloorsRepository;
    private Ekotrope_AboveGradeWallRepository mEkotropeAboveGradeWallsRepository;
    private Ekotrope_WindowRepository mEkotropeWindowRepository;
    private Ekotrope_DoorRepository mEkotropeDoorRepository;
    private Ekotrope_CeilingRepository mEkotropeCeilingRepository;
    private Ekotrope_SlabRepository mEkotropeSlabRepository;
    private Ekotrope_RimJoistRepository mEkotropeRimJoistRepository;
    private Ekotrope_MechanicalEquipmentRepository mEkotropeMechanicalEquipmentRepository;
    private Ekotrope_DistributionSystemRepository mEkotropeDistributionSystemRepository;
    private Ekotrope_DuctRepository mEkotropeDuctRepository;
    private Ekotrope_MechanicalVentilationRepository mEkotropeMechanicalVentilationRepository;
    private Ekotrope_LightingRepository mEkotropeLightingRepository;
    private Ekotrope_RefrigeratorRepository mEkotropeRefrigeratorRepository;
    private Ekotrope_DishwasherRepository mEkotropeDishwasherRepository;
    private Ekotrope_ClothesDryerRepository mEkotropeClothesDryerRepository;
    private Ekotrope_ClothesWasherRepository mEkotropeClothesWasherRepository;
    private Ekotrope_RangeOvenRepository mEkotropeRangeOvenRepository;
    private Ekotrope_InfiltrationRepository mEkotropeInfiltrationRepository;
    private SubmitRequestRepository submitRequestRepository;
    private SharedPreferencesRepository sharedPreferencesRepository;
    private String reportUrl;
    private MutableLiveData<String> snackbarMessage = new MutableLiveData<>();
    private MutableLiveData<String> statusMessage = new MutableLiveData<>();

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mDIITRepository = new DefectItem_InspectionType_XRefRepository(application);
        mPastInspectionRepository = new PastInspectionRepository(application);
        mEkotropeFramedFloorsRepository = new Ekotrope_FramedFloorRepository(application);
        mEkotropeAboveGradeWallsRepository = new Ekotrope_AboveGradeWallRepository(application);
        mEkotropeWindowRepository = new Ekotrope_WindowRepository(application);
        mEkotropeDoorRepository = new Ekotrope_DoorRepository(application);
        mEkotropeCeilingRepository = new Ekotrope_CeilingRepository(application);
        mEkotropeSlabRepository = new Ekotrope_SlabRepository(application);
        mEkotropeRimJoistRepository = new Ekotrope_RimJoistRepository(application);
        mEkotropeMechanicalEquipmentRepository = new Ekotrope_MechanicalEquipmentRepository(application);
        mEkotropeDistributionSystemRepository = new Ekotrope_DistributionSystemRepository(application);
        mEkotropeDuctRepository = new Ekotrope_DuctRepository(application);
        mEkotropeMechanicalVentilationRepository = new Ekotrope_MechanicalVentilationRepository(application);
        mEkotropeLightingRepository = new Ekotrope_LightingRepository(application);
        mEkotropeRefrigeratorRepository = new Ekotrope_RefrigeratorRepository(application);
        mEkotropeDishwasherRepository = new Ekotrope_DishwasherRepository(application);
        mEkotropeClothesDryerRepository = new Ekotrope_ClothesDryerRepository(application);
        mEkotropeClothesWasherRepository = new Ekotrope_ClothesWasherRepository(application);
        mEkotropeRangeOvenRepository = new Ekotrope_RangeOvenRepository(application);
        mEkotropeInfiltrationRepository = new Ekotrope_InfiltrationRepository(application);
        submitRequestRepository = new SubmitRequestRepository(application);
        sharedPreferencesRepository = new SharedPreferencesRepository(application);
        reportUrl = "";
    }

    public LiveData<String> getSnackbarMessage() {
        return snackbarMessage;
    }
    public void showSnackbarMessage(String message) {
        snackbarMessage.setValue(message);
    }
    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }
    public void showStatusMessage(String message) {
        statusMessage.setValue(message);
    }

    //region API Calls
    public void updateInspections(String authToken, String inspectorId) {
        JsonArrayRequest updateInspectionsRequest = RouteSheetAPI.updateInspections(this, authToken, inspectorId, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                showStatusMessage(message);
                updateDefectItems(authToken, inspectorId);
            }
            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(updateInspectionsRequest);
    }
    public void updateDefectItems(String authToken, String inspectorId) {
        JsonArrayRequest updateDefectItemsRequest = RouteSheetAPI.updateDefectItems(this, authToken, inspectorId, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                showStatusMessage(message);
                updateDIIT(authToken, inspectorId);
            }
            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(updateDefectItemsRequest);
    }
    public void updateDIIT(String authToken, String inspectorId) {
        JsonArrayRequest updateDIITRequest = RouteSheetAPI.updateDefectItem_InspectionTypeXRef(this, authToken, inspectorId, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                showStatusMessage(message);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String currentDate = formatter.format(OffsetDateTime.now());
                updateRouteSheetReportLink(authToken, Integer.parseInt(inspectorId), currentDate);
            }
            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(updateDIITRequest);
    }
    public void updateRouteSheetReportLink(String authToken, int inspectorId, String inspectionDate) {
        StringRequest updateRouteSheetReportLinkRequest = RouteSheetAPI.getReportData(this, authToken, inspectorId, inspectionDate, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String updateTime = currentTime.format(formatter);
                sharedPreferencesRepository.setRouteSheetLastUpdated(updateTime);
                showStatusMessage(String.format("Route sheet last updated at %s", updateTime));
            }
            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(updateRouteSheetReportLinkRequest);
    }
    public void checkExistingInspection(String authToken, int inspectionId, int inspectorId) {
        JsonObjectRequest checkExistingInspectionRequest = RouteSheetAPI.checkExistingInspection(this, authToken, inspectionId, inspectorId, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                //showSnackbarMessage(message);
            }
            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(checkExistingInspectionRequest);
    }
    //endregion

    //region Database Insert Calls
    public void insertInspection(Inspection_Table inspection) { mInspectionRepository.insert(inspection); }
    public void insertInspectionHistory(InspectionHistory_Table inspectionHistory) { mInspectionHistoryRepository.insert(inspectionHistory); }
    public void insertInspectionDefect(InspectionDefect_Table inspectionDefect) { mInspectionDefectRepository.insert(inspectionDefect); }
    public void insertPastInspection(PastInspection_Table pastInspection) { mPastInspectionRepository.insert(pastInspection); }
    public void insertDefectItem(DefectItem_Table defectItem) { mDefectItemRepository.insert(defectItem); }
    public void insertDIIT(DefectItem_InspectionType_XRef diit) { mDIITRepository.insert(diit); }
    //endregion

    //region Database Get Calls
    public LiveData<List<RouteSheet_View>> getAllInspectionsForRouteSheet(int inspectorId) { return mInspectionRepository.getAllInspectionsForRouteSheet(inspectorId); }
    public List<Integer> getAllInspectionIds(int inspectorId) { return mInspectionRepository.getAllInspectionIds(inspectorId); }
    public Inspection_Table getInspection(int inspection_id) { return mInspectionRepository.getInspectionSync(inspection_id); }
    //endregion

    //region Database Update Calls
    /**
     * Checks for an existing inspection defect. Since multiple multifamily defects can be assigned
     * on any given date and we need to "keep the chain", this prevents multiple copies of a defect
     * to be added. The only persistent field will be the firstInspectionDetailID, so we check
     * based on that
     *
     * @param firstInspectionDetailId The first recorded inspection detail id
     * @param inspectionId The inspection id to check for an existing defect
     * @return The ID of the existing defect, or 0 if none exists
     */
    public int getExistingMFCDefect(int firstInspectionDetailId, int inspectionId) { return mInspectionDefectRepository.getExistingMFCDefect(firstInspectionDetailId, inspectionId); }
    public void updateExistingMFCDefect(int defectStatusId, String comment, int id) { mInspectionDefectRepository.updateExistingMFCDefect(defectStatusId, comment, id); }

    public void updateRouteSheetIndexes(List<RouteSheet_View> routeSheetList) {
        if (routeSheetList != null) {
            for (int lcv = 0; lcv < routeSheetList.size(); lcv++) {
                mInspectionRepository.updateRouteSheetIndex(routeSheetList.get(lcv).InspectionID, lcv);
            }
        }
    }
    //endregion

    //region Database Delete Calls
    public void clearCompletedAndFutureDatedInspections(String authToken, int inspectorId) {
        List<Integer> allInspectionIds = getAllInspectionIds(inspectorId);
        for (int lcv = 0; lcv < allInspectionIds.size(); lcv++) {
            Inspection_Table inspection = getInspection(allInspectionIds.get(lcv));
            if (inspection.IsUploaded) {
                deleteInspectionDefects(inspection.InspectionID);
                deleteInspectionHistories(inspection.InspectionID);
                deleteInspection(inspection.InspectionID);
            } else {
                checkExistingInspection(authToken, inspection.InspectionID, inspectorId);
            }
        }
    }

    public void deleteInspectionDefects(int inspection_id) { mInspectionDefectRepository.delete(inspection_id); }
    public void deleteInspection(int id) { mInspectionRepository.delete(id); }
    public void deleteInspectionHistories(int inspection_id) { mInspectionHistoryRepository.deleteForInspection(inspection_id); }
    //endregion

    //region Database Ekotrope Updates and Inserts
    public void updateEkotropePlanId(String planId, int inspectionId) { mInspectionRepository.updateEkotropePlanId(planId, inspectionId); }
    public void insertFramedFloor(Ekotrope_FramedFloor_Table framedFloor) { mEkotropeFramedFloorsRepository.insert(framedFloor); }
    public void insertAboveGradeWall(Ekotrope_AboveGradeWall_Table aboveGradeWall) { mEkotropeAboveGradeWallsRepository.insert(aboveGradeWall); }
    public void insertWindow(Ekotrope_Window_Table window) { mEkotropeWindowRepository.insert(window); }
    public void insertDoor(Ekotrope_Door_Table door) { mEkotropeDoorRepository.insert(door); }
    public void insertCeiling(Ekotrope_Ceiling_Table ceiling) { mEkotropeCeilingRepository.insert(ceiling); }
    public void insertSlab(Ekotrope_Slab_Table slab) { mEkotropeSlabRepository.insert(slab); }
    public void insertRimJoist(Ekotrope_RimJoist_Table rimJoist) { mEkotropeRimJoistRepository.insert(rimJoist); }
    public void insertMechanicalEquipment(Ekotrope_MechanicalEquipment_Table mechanicalEquipment) { mEkotropeMechanicalEquipmentRepository.insert(mechanicalEquipment); }
    public void insertDistributionSystem(Ekotrope_DistributionSystem_Table distributionSystem) { mEkotropeDistributionSystemRepository.insert(distributionSystem); }
    public void insertDuct(Ekotrope_Duct_Table duct) { mEkotropeDuctRepository.insert(duct); }
    public void insertMechanicalVentilation(Ekotrope_MechanicalVentilation_Table mechanicalVentilation) { mEkotropeMechanicalVentilationRepository.insert(mechanicalVentilation); }
    public void insertLighting(Ekotrope_Lighting_Table lighting) { mEkotropeLightingRepository.insert(lighting); }
    public void insertRefrigerator(Ekotrope_Refrigerator_Table refrigerator) { mEkotropeRefrigeratorRepository.insert(refrigerator); }
    public void insertDishwasher(Ekotrope_Dishwasher_Table dishwasher) { mEkotropeDishwasherRepository.insert(dishwasher); }
    public void insertClothesDryer(Ekotrope_ClothesDryer_Table clothesDryer) { mEkotropeClothesDryerRepository.insert(clothesDryer); }
    public void insertClothesWasher(Ekotrope_ClothesWasher_Table clothesWasher) { mEkotropeClothesWasherRepository.insert(clothesWasher); }
    public void insertRangeOven(Ekotrope_RangeOven_Table rangeOven) { mEkotropeRangeOvenRepository.insert(rangeOven); }
    public void insertInfiltration(Ekotrope_Infiltration_Table infiltration) { mEkotropeInfiltrationRepository.insert(infiltration); }
    //endregion

    public String getReportUrl() { return reportUrl; }
    public void setReportUrl(String newUrl) { reportUrl = newUrl; }
}

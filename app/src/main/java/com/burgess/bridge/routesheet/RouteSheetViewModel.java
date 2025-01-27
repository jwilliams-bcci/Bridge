package com.burgess.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.ServerCallback;
import com.burgess.bridge.SharedPreferencesRepository;
import com.burgess.bridge.apiqueue.BridgeAPIQueue;
import com.burgess.bridge.apiqueue.RouteSheetAPI;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private final String TAG = "ROUTE_SHEET_VIEW_MODEL";

    private final InspectionRepository repoInspection;
    private final InspectionHistoryRepository repoInspectionHistory;
    private final InspectionDefectRepository repoInspectionDefect;
    private final DefectItemRepository repoDefectItem;
    private final DefectItem_InspectionType_XRefRepository repoDIIT;
    private final PastInspectionRepository repoPastInspection;
    private final Ekotrope_FramedFloorRepository repoEkoFramedFloor;
    private final Ekotrope_AboveGradeWallRepository repoEkoAboveGradeWall;
    private final Ekotrope_WindowRepository repoEkoWindow;
    private final Ekotrope_DoorRepository repoEkoDoor;
    private final Ekotrope_CeilingRepository repoEkoCeiling;
    private final Ekotrope_SlabRepository repoEkoSlab;
    private final Ekotrope_RimJoistRepository repoEkoRimJoist;
    private final Ekotrope_MechanicalEquipmentRepository repoEkoMechanicalEquipment;
    private final Ekotrope_DistributionSystemRepository repoEkoDistributionSystem;
    private final Ekotrope_DuctRepository repoEkoDuct;
    private final Ekotrope_MechanicalVentilationRepository repoEkoMechanicalVentilation;
    private final Ekotrope_LightingRepository repoEkoLighting;
    private final Ekotrope_RefrigeratorRepository repoEkoRefrigerator;
    private final Ekotrope_DishwasherRepository repoEkoDishwasher;
    private final Ekotrope_ClothesDryerRepository repoEkoClothesDryer;
    private final Ekotrope_ClothesWasherRepository repoEkoClothesWasher;
    private final Ekotrope_RangeOvenRepository repoEkoRangeOven;
    private final Ekotrope_InfiltrationRepository repoEkoInfiltration;
    private final SubmitRequestRepository repoSubmitRequest;
    private final SharedPreferencesRepository sharedPreferences;
    private String reportUrl;
    private List<RouteSheet_View> currentList;
    private final MutableLiveData<String> snackbarMessage = new MutableLiveData<>();
    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private final MutableLiveData<String> individualRemaining = new MutableLiveData<>();
    private final MutableLiveData<String> inspectionsRemaining = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showResetConfirmation = new MutableLiveData<>();
    private final MutableLiveData<String> reportLinkUrl = new MutableLiveData<>();

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        repoInspection = new InspectionRepository(application);
        repoInspectionHistory = new InspectionHistoryRepository(application);
        repoInspectionDefect = new InspectionDefectRepository(application);
        repoDefectItem = new DefectItemRepository(application);
        repoDIIT = new DefectItem_InspectionType_XRefRepository(application);
        repoPastInspection = new PastInspectionRepository(application);
        repoEkoFramedFloor = new Ekotrope_FramedFloorRepository(application);
        repoEkoAboveGradeWall = new Ekotrope_AboveGradeWallRepository(application);
        repoEkoWindow = new Ekotrope_WindowRepository(application);
        repoEkoDoor = new Ekotrope_DoorRepository(application);
        repoEkoCeiling = new Ekotrope_CeilingRepository(application);
        repoEkoSlab = new Ekotrope_SlabRepository(application);
        repoEkoRimJoist = new Ekotrope_RimJoistRepository(application);
        repoEkoMechanicalEquipment = new Ekotrope_MechanicalEquipmentRepository(application);
        repoEkoDistributionSystem = new Ekotrope_DistributionSystemRepository(application);
        repoEkoDuct = new Ekotrope_DuctRepository(application);
        repoEkoMechanicalVentilation = new Ekotrope_MechanicalVentilationRepository(application);
        repoEkoLighting = new Ekotrope_LightingRepository(application);
        repoEkoRefrigerator = new Ekotrope_RefrigeratorRepository(application);
        repoEkoDishwasher = new Ekotrope_DishwasherRepository(application);
        repoEkoClothesDryer = new Ekotrope_ClothesDryerRepository(application);
        repoEkoClothesWasher = new Ekotrope_ClothesWasherRepository(application);
        repoEkoRangeOven = new Ekotrope_RangeOvenRepository(application);
        repoEkoInfiltration = new Ekotrope_InfiltrationRepository(application);
        repoSubmitRequest = new SubmitRequestRepository(application);
        sharedPreferences = new SharedPreferencesRepository(application);
        reportUrl = "";

    }

    public List<RouteSheet_View> getCurrentList() { return currentList; }
    public void setCurrentList(List<RouteSheet_View> newList) { currentList = newList; }
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
    public LiveData<String> getIndividualRemainingMessage() { return individualRemaining; }
    public void setIndividualRemainingMessage(String message) { individualRemaining.setValue(message); }
    public LiveData<String> getTeamRemainingMessage() { return inspectionsRemaining; }
    public void setTeamRemainingMessage(String message) { inspectionsRemaining.setValue(message); }
    public LiveData<Boolean> getShowResetConfirmation() { return showResetConfirmation; }
    public void setShowResetConfirmation(Boolean show) { showResetConfirmation.setValue(show); }
    public LiveData<String> getReportLinkUrl() { return reportLinkUrl; }
    public void setReportLinkUrl(String url) { reportLinkUrl.setValue(url); }

    //region API Calls
    public void updateInspections(String authToken, String inspectorId) {
        JsonArrayRequest updateInspectionsRequest = RouteSheetAPI.updateInspections(this, authToken, inspectorId, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                //showStatusMessage(message);
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
                //showStatusMessage(message);
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
                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String updateTime = currentTime.format(formatter);
                showStatusMessage(String.format("Route sheet last updated at %s", updateTime));
                sharedPreferences.setRouteSheetLastUpdated(updateTime);
            }
            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(updateDIITRequest);
    }
    public void getRouteSheetReportlink(String authToken, int inspectorId, String inspectionDate) {
        StringRequest updateRouteSheetReportLinkRequest = RouteSheetAPI.getReportData(this, authToken, inspectorId, inspectionDate, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                setReportLinkUrl(message);
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
    public void updateInspectionsRemaining(String authToken, int inspectorId) {
        JsonObjectRequest updateInspectionsRemainingRequest = RouteSheetAPI.updateInspectionsRemaining(this, authToken, inspectorId, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
                sharedPreferences.setTeamRemaining(Integer.parseInt(message));
            }
            @Override
            public void onFailure(String message) {
                showSnackbarMessage(message);
            }
        });
        BridgeAPIQueue.getInstance().getRequestQueue().add(updateInspectionsRemainingRequest);
    }
    //endregion

    //region Database Insert Calls
    public void insertInspection(Inspection_Table inspection) { repoInspection.insert(inspection); }
    public void insertInspectionHistory(InspectionHistory_Table inspectionHistory) { repoInspectionHistory.insert(inspectionHistory); }
    public void insertInspectionDefect(InspectionDefect_Table inspectionDefect) {
        try {
            repoInspectionDefect.insert(inspectionDefect);
        } catch (ExecutionException | InterruptedException e) {
            BridgeLogger.log('E', TAG, "ERROR in insertInspectionDefect - " + e.getMessage());
        }
    }
    public void insertPastInspection(PastInspection_Table pastInspection) { repoPastInspection.insert(pastInspection); }
    public void insertDefectItem(DefectItem_Table defectItem) { repoDefectItem.insert(defectItem); }
    public void insertDIIT(DefectItem_InspectionType_XRef diit) { repoDIIT.insert(diit); }
    //endregion

    //region Database Get Calls
    public LiveData<List<RouteSheet_View>> getAllInspectionsForRouteSheet(int inspectorId)  { return repoInspection.getAllInspectionsForRouteSheet(inspectorId); }
    public List<Integer> getAllInspectionIds(int inspectorId) { return repoInspection.getAllInspectionIds(inspectorId); }
    public List<Integer> getAllInspectionIdsThread(int inspectorId) {
        try {
            return repoInspection.threadGetAllInspectionIds(inspectorId);
        } catch (ExecutionException | InterruptedException e) {
            BridgeLogger.log('E', TAG, "ERROR in getAllInspectionIdsThread - " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public Inspection_Table getInspection(int inspection_id) { return repoInspection.getInspectionSync(inspection_id); }
    public Inspection_Table getInspectionThread(int inspection_id) {
        try {
            return repoInspection.threadGetInspectionSync(inspection_id);
        } catch (ExecutionException | InterruptedException e) {
            BridgeLogger.log('E', TAG, "ERROR in getInspectionThread - " + e.getMessage());
            return null;
        }
    }
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
    public int getExistingMFCDefect(int firstInspectionDetailId, int inspectionId) { return repoInspectionDefect.getExistingMFCDefect(firstInspectionDetailId, inspectionId); }
    public int getExistingMFCDefectThread(int firstInspectionDetailId, int inspectionId) {
        try {
            return repoInspectionDefect.threadGetExistingMFCDefect(firstInspectionDetailId, inspectionId);
        } catch (ExecutionException | InterruptedException e) {
            BridgeLogger.log('E', TAG, "ERROR in getExistingMFCDefectThread - " + e.getMessage());
            return -1;
        }
    }
    public void updateExistingMFCDefect(int defectStatusId, String comment, int id) { repoInspectionDefect.updateExistingMFCDefect(defectStatusId, comment, id); }
    public void updateRouteSheetIndexes(List<RouteSheet_View> routeSheetList) {
        repoInspection.updateRouteSheetIndexes(routeSheetList);
    }
    public void clearStatusFlags(int inspectionId) {
        repoInspection.clearStatusFlags(inspectionId);
    }
    //endregion

    //region Database Delete Calls
    public void clearCompletedAndFutureDatedInspections(String authToken, int inspectorId) {
        List<Integer> allInspectionIds = getAllInspectionIdsThread(inspectorId);
        for (int lcv = 0; lcv < allInspectionIds.size(); lcv++) {
            Inspection_Table inspection = getInspectionThread(allInspectionIds.get(lcv));
            if (inspection.IsUploaded) {
                deleteInspectionDefects(inspection.InspectionID);
                deleteInspectionHistories(inspection.InspectionID);
                deleteInspection(inspection.InspectionID);
            } else {
                checkExistingInspection(authToken, inspection.InspectionID, inspectorId);
            }
        }
    }
    public void deleteInspectionDefects(int inspection_id) { repoInspectionDefect.delete(inspection_id); }
    public void deleteInspection(int id) { repoInspection.delete(id); }
    public void deleteInspectionHistories(int inspection_id) { repoInspectionHistory.deleteForInspection(inspection_id); }
    //endregion

    //region Database Ekotrope Updates and Inserts
    public void updateEkotropePlanId(String planId, int inspectionId) { repoInspection.updateEkotropePlanId(planId, inspectionId); }
    public void insertFramedFloor(Ekotrope_FramedFloor_Table framedFloor) { repoEkoFramedFloor.insert(framedFloor); }
    public void insertAboveGradeWall(Ekotrope_AboveGradeWall_Table aboveGradeWall) { repoEkoAboveGradeWall.insert(aboveGradeWall); }
    public void insertWindow(Ekotrope_Window_Table window) { repoEkoWindow.insert(window); }
    public void insertDoor(Ekotrope_Door_Table door) { repoEkoDoor.insert(door); }
    public void insertCeiling(Ekotrope_Ceiling_Table ceiling) { repoEkoCeiling.insert(ceiling); }
    public void insertSlab(Ekotrope_Slab_Table slab) { repoEkoSlab.insert(slab); }
    public void insertRimJoist(Ekotrope_RimJoist_Table rimJoist) { repoEkoRimJoist.insert(rimJoist); }
    public void insertMechanicalEquipment(Ekotrope_MechanicalEquipment_Table mechanicalEquipment) { repoEkoMechanicalEquipment.insert(mechanicalEquipment); }
    public void insertDistributionSystem(Ekotrope_DistributionSystem_Table distributionSystem) { repoEkoDistributionSystem.insert(distributionSystem); }
    public void insertDuct(Ekotrope_Duct_Table duct) { repoEkoDuct.insert(duct); }
    public void insertMechanicalVentilation(Ekotrope_MechanicalVentilation_Table mechanicalVentilation) { repoEkoMechanicalVentilation.insert(mechanicalVentilation); }
    public void insertLighting(Ekotrope_Lighting_Table lighting) { repoEkoLighting.insert(lighting); }
    public void insertRefrigerator(Ekotrope_Refrigerator_Table refrigerator) { repoEkoRefrigerator.insert(refrigerator); }
    public void insertDishwasher(Ekotrope_Dishwasher_Table dishwasher) { repoEkoDishwasher.insert(dishwasher); }
    public void insertClothesDryer(Ekotrope_ClothesDryer_Table clothesDryer) { repoEkoClothesDryer.insert(clothesDryer); }
    public void insertClothesWasher(Ekotrope_ClothesWasher_Table clothesWasher) { repoEkoClothesWasher.insert(clothesWasher); }
    public void insertRangeOven(Ekotrope_RangeOven_Table rangeOven) { repoEkoRangeOven.insert(rangeOven); }
    public void insertInfiltration(Ekotrope_Infiltration_Table infiltration) { repoEkoInfiltration.insert(infiltration); }
    //endregion

    public void updateIndividualRemaining(int remaining) {
        setIndividualRemainingMessage(Integer.toString(remaining));
        sharedPreferences.setIndividualRemaining(remaining);
    }

    public void resetInspection(int inspectionId, Boolean acknowledged) {
        Inspection_Table inspection = getInspectionThread(inspectionId);
        if (inspection.IsComplete) {
            showSnackbarMessage(String.format("Resetting inspection at %s, you can now attempt to Re-Upload.", inspection.Address));
            clearStatusFlags(inspection.InspectionID);
        } else {
            if (acknowledged == null) {
                setShowResetConfirmation(true);
            }
            else if (acknowledged) {
                setShowResetConfirmation(false);
                showSnackbarMessage("Resetting inspection, all items cleared");
                deleteInspection(inspection.InspectionID);
                deleteInspectionHistories(inspection.InspectionID);
                deleteInspectionDefects(inspection.InspectionID);
            } else {
                setShowResetConfirmation(false);
                showSnackbarMessage("Cancelled reset.");
            }
        }
    }
}

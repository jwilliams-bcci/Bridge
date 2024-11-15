package com.burgess.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
    private String reportUrl;

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
        reportUrl = "";
    }

    public LiveData<List<RouteSheet_View>> getAllInspectionsForRouteSheet(int inspectorId) {
        return mInspectionRepository.getAllInspectionsForRouteSheet(inspectorId);
    }

    public List<Integer> getAllInspectionIds(int inspectorId) {
        return mInspectionRepository.getAllInspectionIds(inspectorId);
    }

    public void insertInspection(Inspection_Table inspection) {
        mInspectionRepository.insert(inspection);
    }

    public void insertInspectionDefect(InspectionDefect_Table inspectionDefect) {
        mInspectionDefectRepository.insert(inspectionDefect);
    }

    public int multifamilyDefectExists(int firstInspectionDetailId) {
        return mInspectionDefectRepository.multifamilyDefectExists(firstInspectionDetailId);
    }

    public void updateExistingMFCDefect(int defectStatusId, String comment, int id) {
        mInspectionDefectRepository.updateExistingMFCDefect(defectStatusId, comment, id);
    }

    public void insertInspectionHistory(InspectionHistory_Table inspectionHistory) {
        mInspectionHistoryRepository.insert(inspectionHistory);
    }

    public void updateRouteSheetIndex(int inspection_id, int new_order) {
        mInspectionRepository.updateRouteSheetIndex(inspection_id, new_order);
    }

    public void insertDefectItem(DefectItem_Table defectItem) {
        mDefectItemRepository.insert(defectItem);
    }

    public void insertReference(DefectItem_InspectionType_XRef relation) {
        mDIITRepository.insert(relation);
    }

    public Inspection_Table getInspection(int inspection_id) {
        return mInspectionRepository.getInspectionSync(inspection_id);
    }

    public void deleteInspectionDefects(int inspection_id) {
        mInspectionDefectRepository.delete(inspection_id);
    }

    public void deleteInspection(int id) {
        mInspectionRepository.delete(id);
    }

    public void deleteInspectionHistories(int inspection_id) {
        mInspectionHistoryRepository.deleteForInspection(inspection_id);
    }

    public void insertPastInspection(PastInspection_Table pastInspection) {
        mPastInspectionRepository.insert(pastInspection);
    }

    public void updateUrl(String newUrl) {
        reportUrl = newUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void updateEkotropePlanId(String planId, int inspectionId) {
        mInspectionRepository.updateEkotropePlanId(planId, inspectionId);
    }

    public void insertFramedFloor(Ekotrope_FramedFloor_Table framedFloor) {
        mEkotropeFramedFloorsRepository.insert(framedFloor);
    }

    public void insertAboveGradeWall(Ekotrope_AboveGradeWall_Table aboveGradeWall) {
        mEkotropeAboveGradeWallsRepository.insert(aboveGradeWall);
    }

    public void insertWindow(Ekotrope_Window_Table window) {
        mEkotropeWindowRepository.insert(window);
    }

    public void insertDoor(Ekotrope_Door_Table door) {
        mEkotropeDoorRepository.insert(door);
    }

    public void insertCeiling(Ekotrope_Ceiling_Table ceiling) {
        mEkotropeCeilingRepository.insert(ceiling);
    }

    public void insertSlab(Ekotrope_Slab_Table slab) {
        mEkotropeSlabRepository.insert(slab);
    }

    public void insertRimJoist(Ekotrope_RimJoist_Table rimJoist) {
        mEkotropeRimJoistRepository.insert(rimJoist);
    }

    public void insertMechanicalEquipment(Ekotrope_MechanicalEquipment_Table mechanicalEquipment) {
        mEkotropeMechanicalEquipmentRepository.insert(mechanicalEquipment);
    }

    public void insertDistributionSystem(Ekotrope_DistributionSystem_Table distributionSystem) {
        mEkotropeDistributionSystemRepository.insert(distributionSystem);
    }

    public void insertDuct(Ekotrope_Duct_Table duct) {
        mEkotropeDuctRepository.insert(duct);
    }

    public void insertMechanicalVentilation(Ekotrope_MechanicalVentilation_Table mechanicalVentilation) {
        mEkotropeMechanicalVentilationRepository.insert(mechanicalVentilation);
    }

    public void insertLighting(Ekotrope_Lighting_Table lighting) {
        mEkotropeLightingRepository.insert(lighting);
    }

    public void insertRefrigerator(Ekotrope_Refrigerator_Table refrigerator) {
        mEkotropeRefrigeratorRepository.insert(refrigerator);
    }

    public void insertDishwasher(Ekotrope_Dishwasher_Table dishwasher) {
        mEkotropeDishwasherRepository.insert(dishwasher);
    }

    public void insertClothesDryer(Ekotrope_ClothesDryer_Table clothesDryer) {
        mEkotropeClothesDryerRepository.insert(clothesDryer);
    }

    public void insertClothesWasher(Ekotrope_ClothesWasher_Table clothesWasher) {
        mEkotropeClothesWasherRepository.insert(clothesWasher);
    }

    public void insertRangeOven(Ekotrope_RangeOven_Table rangeOven) {
        mEkotropeRangeOvenRepository.insert(rangeOven);
    }
}

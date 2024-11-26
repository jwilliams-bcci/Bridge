package com.burgess.bridge.ekotrope_data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import data.Repositories.Ekotrope_AboveGradeWallRepository;
import data.Repositories.Ekotrope_CeilingRepository;
import data.Repositories.Ekotrope_ClothesDryerRepository;
import data.Repositories.Ekotrope_ClothesWasherRepository;
import data.Repositories.Ekotrope_DishwasherRepository;
import data.Repositories.Ekotrope_DistributionSystemRepository;
import data.Repositories.Ekotrope_DoorRepository;
import data.Repositories.Ekotrope_FramedFloorRepository;
import data.Repositories.Ekotrope_InfiltrationRepository;
import data.Repositories.Ekotrope_LightingRepository;
import data.Repositories.Ekotrope_MechanicalEquipmentRepository;
import data.Repositories.Ekotrope_MechanicalVentilationRepository;
import data.Repositories.Ekotrope_RangeOvenRepository;
import data.Repositories.Ekotrope_RefrigeratorRepository;
import data.Repositories.Ekotrope_SlabRepository;
import data.Repositories.Ekotrope_WindowRepository;
import data.Repositories.InspectionRepository;
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
import data.Tables.Ekotrope_Slab_Table;
import data.Tables.Ekotrope_Window_Table;
import data.Tables.Inspection_Table;

public class Ekotrope_DataViewModel extends AndroidViewModel {
    private final InspectionRepository mInspectionRepository;
    private final Ekotrope_AboveGradeWallRepository mAboveGradeWallsRepository;
    private final Ekotrope_FramedFloorRepository mFramedFloorsRepository;
    private final Ekotrope_DoorRepository mDoorsRepository;
    private final Ekotrope_WindowRepository mWindowsRepository;
    private final Ekotrope_CeilingRepository mCeilingsRepository;
    private final Ekotrope_SlabRepository mSlabsRepository;
    private final Ekotrope_MechanicalEquipmentRepository mMechanicalEquipmentsRepository;
    private final Ekotrope_DistributionSystemRepository mDistributionSystemsRepository;
    private final Ekotrope_MechanicalVentilationRepository mMechanicalVentilationsRepository;
    private final Ekotrope_LightingRepository mLightingRepository;
    private final Ekotrope_RefrigeratorRepository mRefrigeratorRepository;
    private final Ekotrope_DishwasherRepository mDishwasherRepository;
    private final Ekotrope_ClothesDryerRepository mClothesDryerRepository;
    private final Ekotrope_ClothesWasherRepository mClothesWasherRepository;
    private final Ekotrope_RangeOvenRepository mRangeOvenRepository;
    private final Ekotrope_InfiltrationRepository mInfiltrationRepository;

    public Ekotrope_DataViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mAboveGradeWallsRepository = new Ekotrope_AboveGradeWallRepository(application);
        mFramedFloorsRepository = new Ekotrope_FramedFloorRepository(application);
        mDoorsRepository = new Ekotrope_DoorRepository(application);
        mWindowsRepository = new Ekotrope_WindowRepository(application);
        mCeilingsRepository = new Ekotrope_CeilingRepository(application);
        mSlabsRepository = new Ekotrope_SlabRepository(application);
        mMechanicalEquipmentsRepository = new Ekotrope_MechanicalEquipmentRepository(application);
        mDistributionSystemsRepository = new Ekotrope_DistributionSystemRepository(application);
        mMechanicalVentilationsRepository = new Ekotrope_MechanicalVentilationRepository(application);
        mLightingRepository = new Ekotrope_LightingRepository(application);
        mRefrigeratorRepository = new Ekotrope_RefrigeratorRepository(application);
        mDishwasherRepository = new Ekotrope_DishwasherRepository(application);
        mClothesDryerRepository = new Ekotrope_ClothesDryerRepository(application);
        mClothesWasherRepository = new Ekotrope_ClothesWasherRepository(application);
        mRangeOvenRepository = new Ekotrope_RangeOvenRepository(application);
        mInfiltrationRepository = new Ekotrope_InfiltrationRepository(application);
    }

    public Inspection_Table getInspectionSync(int inspection_id) {
        return mInspectionRepository.getInspectionSync(inspection_id);
    }

    public JSONObject getInspectionSyncJson_Rough(String plan_id, String project_id) {
        JSONObject topLevelRequest = new JSONObject();
        JSONObject inspectionSyncObj = new JSONObject();
        JSONArray aboveGradeWallsJson = new JSONArray();
        JSONArray framedFloorsJson = new JSONArray();
        JSONArray doorsJson = new JSONArray();
        JSONArray windowsJson = new JSONArray();
        JSONArray ceilingsJson = new JSONArray();
        JSONArray slabsJson = new JSONArray();
        JSONArray distributionSystemsJson = new JSONArray();

        List<Ekotrope_AboveGradeWall_Table> aboveGradeWalls = mAboveGradeWallsRepository.getAboveGradeWallsForUpdate(plan_id);
        List<Ekotrope_FramedFloor_Table> framedFloors = mFramedFloorsRepository.getFramedFloorsForUpdate(plan_id);
        List<Ekotrope_Door_Table> doors = mDoorsRepository.getDoorsForUpdate(plan_id);
        List<Ekotrope_Window_Table> windows = mWindowsRepository.getWindowsForUpdate(plan_id);
        List<Ekotrope_Ceiling_Table> ceilings = mCeilingsRepository.getCeilingsForUpdate(plan_id);
        List<Ekotrope_Slab_Table> slabs = mSlabsRepository.getSlabsForUpdate(plan_id);
        List<Ekotrope_DistributionSystem_Table> distributionSystems = mDistributionSystemsRepository.getDistributionSystemsForUpdate(plan_id);

        try {
            topLevelRequest.put("additionalEmail", "jwilliams@burgess-inc.com");
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
            inspectionSyncObj.put("ceilingsAndRoofs", ceilingsJson);

            for (Ekotrope_Slab_Table slab : slabs) {
                slabsJson.put(slab.toJsonObj());
            }
            inspectionSyncObj.put("slabs", slabsJson);

            for (Ekotrope_DistributionSystem_Table distributionSystem : distributionSystems) {
                distributionSystemsJson.put(distributionSystem.toJsonObj());
            }
            inspectionSyncObj.put("distributionSystems", distributionSystemsJson);

            topLevelRequest.put("inspectionSync", inspectionSyncObj);
        } catch (Exception e) {
        }
        return topLevelRequest;
    }

    public JSONObject getInspectionSyncJson_Final(String plan_id, String project_id) {
        JSONObject topLevelRequest = new JSONObject();
        JSONObject inspectionSyncObj = new JSONObject();
        JSONArray ceilingsJson = new JSONArray();
        JSONArray mechanicalEquipmentsJson = new JSONArray();
        JSONArray distributionSystemsJson = new JSONArray();
        JSONArray mechanicalVentilationsJson = new JSONArray();
        Ekotrope_Lighting_Table lighting = mLightingRepository.getLighting(plan_id);
        Ekotrope_Refrigerator_Table refrigerator = mRefrigeratorRepository.getRefrigerator(plan_id);
        Ekotrope_Dishwasher_Table dishwasher = mDishwasherRepository.getDishwasher(plan_id);
        Ekotrope_ClothesDryer_Table clothesDryer = mClothesDryerRepository.getClothesDryer(plan_id);
        Ekotrope_ClothesWasher_Table clothesWasher = mClothesWasherRepository.getClothesWasher(plan_id);
        Ekotrope_RangeOven_Table rangeOven = mRangeOvenRepository.getRangeOven(plan_id);
        Ekotrope_Infiltration_Table infiltration = mInfiltrationRepository.getInfiltration(plan_id);

        List<Ekotrope_Ceiling_Table> ceilings = mCeilingsRepository.getCeilingsForUpdate(plan_id);
        List<Ekotrope_MechanicalEquipment_Table> mechanicalEquipments = mMechanicalEquipmentsRepository.getMechanicalEquipmentsForUpdate(plan_id);
        List<Ekotrope_DistributionSystem_Table> distributionSystems = mDistributionSystemsRepository.getDistributionSystemsForUpdate(plan_id);
        List<Ekotrope_Duct_Table> ducts;
        List<Ekotrope_MechanicalVentilation_Table> mechanicalVentilations = mMechanicalVentilationsRepository.getMechanicalVentilationsForUpdate(plan_id);

        try {
            topLevelRequest.put("additionalEmail", "jwilliams@burgess-inc.com");
            inspectionSyncObj.put("projectId", project_id.trim());

            if (!ceilings.isEmpty()) {
                for (Ekotrope_Ceiling_Table ceiling : ceilings) {
                    ceilingsJson.put(ceiling.toJsonObj());
                }
                inspectionSyncObj.put("ceilingsAndRoofs", ceilingsJson);
            }

            if (!mechanicalEquipments.isEmpty()) {
                for (Ekotrope_MechanicalEquipment_Table mechanicalEquipment : mechanicalEquipments) {
                    mechanicalEquipmentsJson.put(mechanicalEquipment.toJsonObj());
                }
                inspectionSyncObj.put("mechanicalEquipments", mechanicalEquipmentsJson);
            }

            if (!distributionSystems.isEmpty()) {
                for (Ekotrope_DistributionSystem_Table distributionSystem : distributionSystems) {
                    distributionSystemsJson.put(distributionSystem.toJsonObj());
                }
                inspectionSyncObj.put("distributionSystems", distributionSystemsJson);
            }

            if (!mechanicalVentilations.isEmpty()) {
                for (Ekotrope_MechanicalVentilation_Table mechanicalVentilation : mechanicalVentilations) {
                    mechanicalVentilationsJson.put(mechanicalVentilation.toJsonObj());
                }
                inspectionSyncObj.put("mechanicalVentilations", mechanicalVentilationsJson);
            }

            if (lighting.is_changed) {
                inspectionSyncObj.put("lighting", lighting.toJsonObj());
            }

            if (refrigerator.is_changed) {
                inspectionSyncObj.put("refrigerator", refrigerator.toJsonObj());
            }

            if (dishwasher.is_changed) {
                inspectionSyncObj.put("dishwasherAvailable", dishwasher.dishwasher_available);
            }

            if (clothesDryer.is_changed) {
                inspectionSyncObj.put("clothesDryer", clothesDryer.toJsonObj());
            }

            if (clothesWasher.is_changed) {
                inspectionSyncObj.put("clothesWasher", clothesWasher.toJsonObj());
            }

            if (rangeOven.is_changed) {
                inspectionSyncObj.put("rangeOven", rangeOven.toJsonObj());
            }

            if (infiltration.is_changed) {
                inspectionSyncObj.put("infiltrationMeasurementType", infiltration.measurement_type);
                inspectionSyncObj.put("infiltrationUnit", "CFM_50");
                inspectionSyncObj.put("infiltrationValue", infiltration.cfm_50);
            }

            topLevelRequest.put("inspectionSync", inspectionSyncObj);
        } catch (Exception e) {
            return null;
        }

        return topLevelRequest;
    }
}
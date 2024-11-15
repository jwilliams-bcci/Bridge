package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_mechanical_equipment_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_MechanicalEquipment_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String name;
    public String model_number;
    public String location;
    public Double percent_heating_load;
    public Double percent_cooling_load;
    public Double percent_hot_water_load;
    public String ahri_reference_number;
    public String ahri_reference_fuel_type;
    public boolean rc_test_conducted;
    public String rc_test_method;
    public String rc_metering_device;
    public Double rc_difference_dtd;
    public Double rc_difference_ctoa;
    public Double rc_weight_deviation;
    public boolean is_changed;

    public Ekotrope_MechanicalEquipment_Table() {
        plan_id = "";
        index = -1;
        is_changed = false;
    }

    public Ekotrope_MechanicalEquipment_Table(@NonNull String plan_id, int index, String name,
                                              String model_number, String location,
                                              Double percent_heating_load, Double percent_cooling_load,
                                              Double percent_hot_water_load, String ahri_reference_number,
                                              String ahri_reference_fuel_type, boolean rc_test_conducted,
                                              String rc_test_method, String rc_metering_device,
                                              Double rc_difference_dtd, Double rc_difference_ctoa,
                                              Double rc_weight_deviation, boolean is_changed) {
        this.plan_id = plan_id;
        this.index = index;
        this.name = name;
        this.model_number = model_number;
        this.location = location;
        this.percent_heating_load = percent_heating_load;
        this.percent_cooling_load = percent_cooling_load;
        this.percent_hot_water_load = percent_hot_water_load;
        this.ahri_reference_number = ahri_reference_number;
        this.ahri_reference_fuel_type = ahri_reference_fuel_type;
        this.rc_test_conducted = rc_test_conducted;
        this.rc_test_method = rc_test_method;
        this.rc_metering_device = rc_metering_device;
        this.rc_difference_dtd = rc_difference_dtd;
        this.rc_difference_ctoa = rc_difference_ctoa;
        this.rc_weight_deviation = rc_weight_deviation;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        JSONObject rcObject = new JSONObject();
        try {
            jsonObject.put("index", index);
            jsonObject.put("location", location);
            jsonObject.put("percentHeatingLoad", percent_heating_load);
            jsonObject.put("percentCoolingLoad", percent_cooling_load);
            jsonObject.put("percentHotWaterLoad", percent_hot_water_load);
            jsonObject.put("ahriReferenceNumber", ahri_reference_number);
            jsonObject.put("ahriReferenceFuelType", ahri_reference_fuel_type);

            rcObject.put("rcTestConducted", rc_test_conducted);
            rcObject.put("rcTestMethod", rc_test_method);
            rcObject.put("rcMeteringDevice", rc_metering_device);
            rcObject.put("rcDifferenceDtd", rc_difference_dtd);
            rcObject.put("rcDifferenceCtoa", rc_difference_ctoa);
            rcObject.put("rcWeightDeviation", rc_weight_deviation);

            jsonObject.put("refrigerantCharge", rcObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

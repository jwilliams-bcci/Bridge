package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_mechanical_ventilation_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_MechanicalVentilation_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String motor_type;
    public String ventilation_type;
    public Double measured_flow_rate;
    public Double fan_watts;
    public Double operational_hours_per_day;
    public boolean is_changed;

    public Ekotrope_MechanicalVentilation_Table() {
        plan_id = "";
        index = -1;
        is_changed = false;
    }

    public Ekotrope_MechanicalVentilation_Table(@NonNull String plan_id, int index,
                                                String motor_type, String ventilation_type,
                                                Double measured_flow_rate, Double fan_watts,
                                                Double operational_hours_per_day, boolean is_changed) {
        this.plan_id = plan_id;
        this.index = index;
        this.motor_type = motor_type;
        this.ventilation_type = ventilation_type;
        this.measured_flow_rate = measured_flow_rate;
        this.fan_watts = fan_watts;
        this.operational_hours_per_day = operational_hours_per_day;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("index", index);
            jsonObject.put("ventilationType", ventilation_type);
            jsonObject.put("measuredFlowRate", measured_flow_rate);
            jsonObject.put("fanWatts", fan_watts);
            jsonObject.put("operationalHoursPerDay", operational_hours_per_day);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

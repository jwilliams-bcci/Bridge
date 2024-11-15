package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_dishwasher_table", primaryKeys = {"plan_id"})
public class Ekotrope_Dishwasher_Table {
    @NonNull
    public String plan_id;
    public boolean dishwasher_available;
    public String dishwasher_defaults_type;
    public String dishwasher_size;
    public String dishwasher_efficiency_type;
    public Double dishwasher_efficiency;
    public Double dishwasher_annual_gas_cost;
    public Double dishwasher_gas_rate;
    public Double dishwasher_electric_rate;
    public boolean is_changed;

    public Ekotrope_Dishwasher_Table() {
        plan_id = "";
        dishwasher_available = false;
        is_changed = false;
    }

    public Ekotrope_Dishwasher_Table(@NonNull String plan_id, boolean dishwasher_available,
                                      String dishwasher_defaults_type, String dishwasher_size,
                                      String dishwasher_efficiency_type, Double dishwasher_efficiency,
                                      Double dishwasher_annual_gas_cost, Double dishwasher_gas_rate,
                                      Double dishwasher_electric_rate, boolean is_changed) {
        this.plan_id = plan_id;
        this.dishwasher_available = dishwasher_available;
        this.dishwasher_defaults_type = dishwasher_defaults_type;
        this.dishwasher_size = dishwasher_size;
        this.dishwasher_efficiency_type = dishwasher_efficiency_type;
        this.dishwasher_efficiency = dishwasher_efficiency;
        this.dishwasher_annual_gas_cost = dishwasher_annual_gas_cost;
        this.dishwasher_gas_rate = dishwasher_gas_rate;
        this.dishwasher_electric_rate = dishwasher_electric_rate;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dishwasherAvailable", dishwasher_available);
            jsonObject.put("dishwasherDefaultsType", dishwasher_defaults_type);
            jsonObject.put("dishwasherSize", dishwasher_size);
            jsonObject.put("dishwasherEfficiencyType", dishwasher_efficiency_type);
            jsonObject.put("dishwasherEfficiency", dishwasher_efficiency);
            jsonObject.put("dishwasherAnnualGasCost", dishwasher_annual_gas_cost);
            jsonObject.put("dishwasherGasRate", dishwasher_gas_rate);
            jsonObject.put("dishwasherElectricRate", dishwasher_electric_rate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

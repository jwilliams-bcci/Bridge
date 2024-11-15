package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_distribution_system_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_DistributionSystem_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String system_type;
    public boolean is_leakage_to_outside_tested;
    public Double leakage_to_outside;
    public Double total_leakage;
    public String total_duct_leakage_test_condition;
    public Integer number_of_returns;
    public Double sq_feet_served;
    public boolean is_changed;

    public Ekotrope_DistributionSystem_Table() {
        plan_id = "";
        is_leakage_to_outside_tested = false;
        is_changed = false;
    }

    public Ekotrope_DistributionSystem_Table(@NonNull String plan_id, int index, String system_type,
                                             boolean is_leakage_to_outside_tested,
                                             Double leakage_to_outside,
                                             Double total_leakage,
                                             String total_duct_leak,
                                             Integer number_of_returns,
                                             Double sq_feet_served, boolean is_changed) {
        this.plan_id = plan_id;
        this.index = index;
        this.system_type = system_type;
        this.is_leakage_to_outside_tested = is_leakage_to_outside_tested;
        this.leakage_to_outside = leakage_to_outside;
        this.total_leakage = total_leakage;
        this.total_duct_leakage_test_condition = total_duct_leak;
        this.number_of_returns = number_of_returns;
        this.sq_feet_served = sq_feet_served;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("index", index);
            jsonObject.put("isLeakageToOutsideTested", is_leakage_to_outside_tested);
            jsonObject.put("leakageToOutside", leakage_to_outside);
            jsonObject.put("totalLeakage", total_leakage);
            jsonObject.put("totalDuctLeakageTestCondition", total_duct_leakage_test_condition);
            jsonObject.put("numberOfReturns", number_of_returns);
            jsonObject.put("sqFeetServed", sq_feet_served);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

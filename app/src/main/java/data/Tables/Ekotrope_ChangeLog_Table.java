package data.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_change_log_table")
public class Ekotrope_ChangeLog_Table {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int inspection_id;
    public String project_id;
    public String plan_id;
    public String component_type;
    public String component_name;
    public String field_name;
    public String old_value;
    public String new_value;

    public Ekotrope_ChangeLog_Table() {
    }

    public Ekotrope_ChangeLog_Table(int inspection_id, String project_id, String plan_id,
                                    String component_type, String component_name, String field_name,
                                    String old_value, String new_value) {
        this.inspection_id = inspection_id;
        this.project_id = project_id;
        this.plan_id = plan_id;
        this.component_type = component_type;
        this.component_name = component_name;
        this.field_name = field_name;
        this.old_value = old_value;
        this.new_value = new_value;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("inspection_id", inspection_id);
            jsonObj.put("project_id", project_id);
            jsonObj.put("plan_id", plan_id);
            jsonObj.put("component_type", component_type);
            jsonObj.put("component_name", component_name);
            jsonObj.put("field_name", field_name);
            jsonObj.put("old_value", old_value);
            jsonObj.put("new_value", new_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}

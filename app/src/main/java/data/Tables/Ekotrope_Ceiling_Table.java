package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_ceiling_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_Ceiling_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String name;
    public String typeName;
    public String cavityInsulationGrade;
    public double cavityInsulationR;
    public double continuousInsulationR;
    public double studSpacing;
    public double studWidth;
    public double studDepth;
    public String studMaterial;
    public boolean hasRadiantBarrier;
    public boolean isChanged;

    public Ekotrope_Ceiling_Table() {
    }

    public Ekotrope_Ceiling_Table(String plan_id, int index, String name, String typeName,
                                   String cavityInsulationGrade, double cavityInsulationR,
                                   double continuousInsulationR, double studSpacing,
                                   double studWidth, double studDepth, String studMaterial,
                                   boolean hasRadiantBarrier, boolean isChanged) {
        this.plan_id = plan_id;
        this.index = index;
        this.name = name;
        this.typeName = typeName;
        this.cavityInsulationGrade = cavityInsulationGrade;
        this.cavityInsulationR = cavityInsulationR;
        this.continuousInsulationR = continuousInsulationR;
        this.studSpacing = studSpacing;
        this.studWidth = studWidth;
        this.studDepth = studDepth;
        this.studMaterial = studMaterial;
        this.hasRadiantBarrier = hasRadiantBarrier;
        this.isChanged = isChanged;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("index", index);
            jsonObj.put("name", typeName);
            jsonObj.put("cavityInsulationGrade", cavityInsulationGrade);
            jsonObj.put("cavityInsulationR", cavityInsulationR);
            jsonObj.put("continuousInsulationR", continuousInsulationR);
            jsonObj.put("studSpacing", studSpacing);
            jsonObj.put("studWidth", studWidth);
            jsonObj.put("studDepth", studDepth);
            jsonObj.put("studMaterial", studMaterial);
            jsonObj.put("hasRadiantBarrier", hasRadiantBarrier);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}

package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_above_grade_wall_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_AboveGradeWall_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String name;
    public String typeName;
    public String cavityInsulationGrade; //gotten from api
    public double cavityInsulationR; //gotten from api
    public double continuousInsulationR;
    public double studSpacing; //gotten from api
    public double studWidth; //gotten from api
    public double studDepth; //gotten from api
    public String studMaterial; //gotten from api

    public Ekotrope_AboveGradeWall_Table() {
    }

    public Ekotrope_AboveGradeWall_Table(String plan_id, int index, String name, String typeName,
                                         String cavityInsulationGrade, double cavityInsulationR,
                                         double continuousInsulationR, double studSpacing,
                                         double studWidth, double studDepth, String studMaterial) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}

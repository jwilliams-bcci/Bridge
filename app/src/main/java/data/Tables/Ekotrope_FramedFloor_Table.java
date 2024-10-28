package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import data.Enums.Ekotrope_StudMaterial;

@Entity(tableName = "ekotrope_framed_floor_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_FramedFloor_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String name;
    public String cavityInsulationGrade; //gotten from api
    public double cavityInsulationR; //gotten from api
    public double continuousInsulationR;
    public double studSpacing; //gotten from api
    public double studWidth; //gotten from api
    public double studDepth; //gotten from api
    public String studMaterial; //gotten from api

    public Ekotrope_FramedFloor_Table() {
    }

    public Ekotrope_FramedFloor_Table(String plan_id, int index, String name,
                                      String cavityInsulationGrade, double cavityInsulationR,
                                      double continuousInsulationR, double studSpacing,
                                      double studWidth, double studDepth, String studMaterial) {
        this.plan_id = plan_id;
        this.index = index;
        this.name = name;
        this.cavityInsulationGrade = cavityInsulationGrade;
        this.cavityInsulationR = cavityInsulationR;
        this.continuousInsulationR = continuousInsulationR;
        this.studSpacing = studSpacing;
        this.studWidth = studWidth;
        this.studDepth = studDepth;
    }
}

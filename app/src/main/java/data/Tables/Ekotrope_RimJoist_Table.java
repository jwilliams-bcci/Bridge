package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "ekotrope_rimjoist_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_RimJoist_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String name;
    public String typeName;
    public String betweenInteriorAnd;
    public double surfaceArea;
    public double uFactor;
    public double rFactor;
    public boolean isChanged;

    public Ekotrope_RimJoist_Table() {
    }

    public Ekotrope_RimJoist_Table(String plan_id, int index, String name, String typeName,
                                    String betweenInteriorAnd, double surfaceArea, double uFactor,
                                    double rFactor, boolean isChanged) {
        this.plan_id = plan_id;
        this.index = index;
        this.name = name;
        this.typeName = typeName;
        this.betweenInteriorAnd = betweenInteriorAnd;
        this.surfaceArea = surfaceArea;
        this.uFactor = uFactor;
        this.rFactor = rFactor;
        this.isChanged = isChanged;
    }
}
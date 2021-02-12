package data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inspection_table")
public class Inspection_Table {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "InspectionId")
    private int mInspectionId;

    @NonNull
    @ColumnInfo(name = "BuilderId")
    private int mBuilderId;
}

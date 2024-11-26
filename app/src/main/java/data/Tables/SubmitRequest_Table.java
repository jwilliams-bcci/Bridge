package data.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "submit_request_table")
public class SubmitRequest_Table {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int inspection_id;
    public int inspection_defect_id;
    public boolean complete;
    public boolean failed;

    public SubmitRequest_Table(int inspection_id, int inspection_defect_id,
                               boolean complete, boolean failed) {
        this.inspection_id = inspection_id;
        this.inspection_defect_id = inspection_defect_id;
        this.complete = complete;
        this.failed = failed;
    }
}

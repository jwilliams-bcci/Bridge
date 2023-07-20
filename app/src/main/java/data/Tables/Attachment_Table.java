package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attachment_table")
public class Attachment_Table {
    @PrimaryKey
    public int id;
    public int inspection_id;
    public int location_id;
    public String file_name;
    public byte[] file_data;
    public String attachment_type;
    public String file_path;
    public boolean is_uploaded;

    public Attachment_Table(){}

    public Attachment_Table(@NonNull int id, int inspection_id, int location_id, String file_name,
                            byte[] file_data, String file_path, boolean is_uploaded) {
        this.id = id;
        this.inspection_id = inspection_id;
        this.location_id = location_id;
        this.file_name = file_name;
        this.file_data = file_data;
        this.file_path = file_path;
        this.is_uploaded = is_uploaded;
    }
}

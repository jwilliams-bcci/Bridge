package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attachment_table")
public class Attachment_Table {
    @PrimaryKey
    public int AttachmentID;
    public int InspectionID;
    public int LocationID;
    public String FileName;
    public byte[] FileData;
    public String AttachmentType;
    public String FilePath;
    public boolean IsUploaded;

    public Attachment_Table(){}

    public Attachment_Table(@NonNull int AttachmentID, int InspectionID, int LocationID, String FileName,
                            byte[] FileData, String FilePath, boolean IsUploaded) {
        this.AttachmentID = AttachmentID;
        this.InspectionID = InspectionID;
        this.LocationID = LocationID;
        this.FileName = FileName;
        this.FileData = FileData;
        this.FilePath = FilePath;
        this.IsUploaded = IsUploaded;
    }
}

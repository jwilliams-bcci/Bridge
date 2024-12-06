package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Attachment_Table;

@Dao
public interface Attachment_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Attachment_Table attachment);

    @Query("DELETE FROM Attachment_Table")
    void deleteAll();

    @Query("DELETE FROM Attachment_Table WHERE AttachmentID = :id")
    void delete(int id);

    @Query("DELETE FROM attachment_Table WHERE InspectionID = :inspection_id")
    void deleteForInspection(int inspection_id);

    @Query("SELECT * FROM attachment_Table WHERE InspectionID = :inspection_id")
    List<Attachment_Table> getAttachmentsForInspection(int inspection_id);

    @Query("SELECT * FROM attachment_table WHERE InspectionID = :inspectionId OR LocationID = :locationId")
    LiveData<List<Attachment_Table>> getAttachments(int inspectionId, int locationId);

    @Query("SELECT * FROM attachment_table WHERE InspectionID = :inspectionId AND IsUploaded = 0")
    List<Attachment_Table> getAttachmentsToUpload(int inspectionId);

    @Query("UPDATE attachment_table SET IsUploaded = 1 WHERE AttachmentID = :attachmentId")
    void updateIsUploaded(int attachmentId);
}

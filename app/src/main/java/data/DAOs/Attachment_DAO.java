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

    @Query("DELETE FROM Attachment_Table WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM attachment_Table WHERE inspection_id = :inspection_id")
    void deleteForInspection(int inspection_id);

    @Query("SELECT * FROM attachment_Table WHERE inspection_id = :inspection_id")
    List<Attachment_Table> getAttachmentsForInspection(int inspection_id);

    @Query("SELECT * FROM attachment_table WHERE inspection_id = :inspectionId OR location_id = :locationId")
    LiveData<List<Attachment_Table>> getAttachments(int inspectionId, int locationId);

    @Query("SELECT * FROM attachment_table WHERE inspection_id = :inspectionId AND is_uploaded = 0")
    List<Attachment_Table> getAttachmentsToUpload(int inspectionId);

    @Query("UPDATE attachment_table SET is_uploaded = 1 WHERE id = :attachmentId")
    void updateIsUploaded(int attachmentId);
}

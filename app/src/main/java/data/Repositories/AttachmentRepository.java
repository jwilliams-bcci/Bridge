package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Attachment_DAO;
import data.Tables.Attachment_Table;

public class AttachmentRepository {
    private Attachment_DAO mInspectionAttachmentDao;

    public AttachmentRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectionAttachmentDao = db.mInspectionAttachmentDao();
    }

    public List<Attachment_Table> getInspectionAttachments(int inspection_id) {
        return mInspectionAttachmentDao.getAttachmentsForInspection(inspection_id);
    }

    public LiveData<List<Attachment_Table>> getAttachments(int inspectionId, int locationId) {
        return mInspectionAttachmentDao.getAttachments(inspectionId, locationId);
    }

    public List<Attachment_Table> getAttachmentsToUpload(int inspectionId) {
        return mInspectionAttachmentDao.getAttachmentsToUpload(inspectionId);
    }

    public void updateIsUploaded(int attachmentId) {
        mInspectionAttachmentDao.updateIsUploaded(attachmentId);
    }

    public void insert(Attachment_Table attachment) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionAttachmentDao.insert(attachment);
        });
    }
}

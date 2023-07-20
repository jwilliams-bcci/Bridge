package com.burgess.bridge.attachments;

import android.app.Application;
import android.content.ContextWrapper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.AttachmentRepository;
import data.Repositories.InspectionRepository;
import data.Tables.Attachment_Table;
import data.Tables.Inspection_Table;

public class AttachmentsViewModel extends AndroidViewModel {
    private AttachmentRepository mAttachmentRepository;
    private InspectionRepository mInspectionRepository;

    public AttachmentsViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mAttachmentRepository = new AttachmentRepository(application);
    }

    public void insertAttachment(Attachment_Table attachment) {
        mAttachmentRepository.insert(attachment);
    }

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionRepository.getInspectionSync(inspectionId);
    }

    public LiveData<List<Attachment_Table>> getAttachments(int inspectionId, int locationId) {
        return mAttachmentRepository.getAttachments(inspectionId, locationId);
    }
}

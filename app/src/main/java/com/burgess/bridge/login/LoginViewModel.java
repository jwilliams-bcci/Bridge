package com.burgess.bridge.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.BuilderRepository;
import data.Repositories.CannedCommentRepository;
import data.Repositories.DefectItemRepository;
import data.Repositories.DefectItem_InspectionType_XRefRepository;
import data.Repositories.DirectionRepository;
import data.Repositories.FaultRepository;
import data.Repositories.InspectorRepository;
import data.Repositories.RoomRepository;
import data.Tables.Builder_Table;
import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.Direction_Table;
import data.Tables.Fault_Table;
import data.Tables.Inspector_Table;
import data.Tables.Room_Table;

public class LoginViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private CannedCommentRepository mCannedCommentRepository;
    private DefectItem_InspectionType_XRefRepository mRelationRepository;
    private BuilderRepository mBuilderRepository;
    private InspectorRepository mInspectorRepository;
    private RoomRepository mRoomRepository;
    private DirectionRepository mDirectionRepository;
    private FaultRepository mFaultRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mCannedCommentRepository = new CannedCommentRepository(application);
        mRelationRepository = new DefectItem_InspectionType_XRefRepository(application);
        mBuilderRepository = new BuilderRepository(application);
        mInspectorRepository = new InspectorRepository(application);
        mRoomRepository = new RoomRepository(application);
        mDirectionRepository = new DirectionRepository(application);
        mFaultRepository = new FaultRepository(application);
    }

    public void insertDefectItem(DefectItem_Table defectItem) {
        mDefectItemRepository.insert(defectItem);
    }

    public void insertCannedComment(CannedComment_Table cannedComment) {
        mCannedCommentRepository.insert(cannedComment);
    }

    public void insertReference(DefectItem_InspectionType_XRef relation) {
        mRelationRepository.insert(relation);
    }

    public void insertBuilder(Builder_Table builder) {
        mBuilderRepository.insert(builder);
    }

    public void insertInspector(Inspector_Table inspector) {
        mInspectorRepository.insert(inspector);
    }

    public void insertRoom(Room_Table room) {
        mRoomRepository.insert(room);
    }

    public void insertDirection(Direction_Table direction) {
        mDirectionRepository.insert(direction);
    }

    public void insertFault(Fault_Table fault) {
        mFaultRepository.insert(fault);
    }
}

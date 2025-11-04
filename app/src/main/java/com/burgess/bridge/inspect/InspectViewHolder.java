package com.burgess.bridge.inspect;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.defectitem.DefectItemActivity;
import com.burgess.bridge.R;

public class InspectViewHolder extends RecyclerView.ViewHolder {
    private final ConstraintLayout mConstraintLayout;
    private final TextView mTextDefectItemSection;
    private final TextView mTextDefectItemNumber;
    private final TextView mTextStatusIcon;
    private final TextView mTextDefectItemDescription;
    private int mInspectionHistoryId;
    private int mFirstDetailId;
    private int mDefectItemId;
    private int mInspectionDefectId;
    private String mComment;

    public InspectViewHolder(@NonNull View itemView) {
        super(itemView);
        mConstraintLayout = itemView.findViewById(R.id.item_defect_item_content_layout);
        mTextDefectItemSection = itemView.findViewById(R.id.item_defect_item_text_section);
        mTextDefectItemNumber = itemView.findViewById(R.id.item_defect_item_text_number);
        mTextStatusIcon = itemView.findViewById(R.id.item_defect_item_text_status_icon);
        mTextDefectItemDescription = itemView.findViewById(R.id.item_defect_item_text_description);
    }

    //region GETTERS
    public ConstraintLayout getConstraintLayout() {
        return mConstraintLayout;
    }

    public TextView getTextDefectItemSection() {
        return mTextDefectItemSection;
    }

    public TextView getTextDefectItemNumber() {
        return mTextDefectItemNumber;
    }

    public TextView getTextStatusIcon() {
        return mTextStatusIcon;
    }

    public TextView getTextDefectItemDescription() {
        return mTextDefectItemDescription;
    }

    public int getInspectionHistoryId() {
        return mInspectionHistoryId;
    }

    public int getFirstDetailId() {
        return mFirstDetailId;
    }

    public int getDefectItemId() {
        return mDefectItemId;
    }

    public int getInspectionDefectId() {
        return mInspectionDefectId;
    }

    public String getComment() {
        return mComment;
    }
    //endregion
    //region SETTERS
    public void setInspectionHistoryId(int inspectionHistoryId) {
        mInspectionHistoryId = inspectionHistoryId;
    }

    public void setFirstDetailId(int firstDetailId) {
        mFirstDetailId = firstDetailId;
    }

    public void setDefectItemId(int defectItemId) {
        mDefectItemId = defectItemId;
    }

    public void setInspectionDefectId(int inspectionDefectId) {
        mInspectionDefectId = inspectionDefectId;
    }

    public void setComment(String comment) {
        mComment = comment;
    }
    //endregion
}


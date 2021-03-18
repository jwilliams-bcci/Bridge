package com.example.bridge.reviewandsubmit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.R;

import data.DAOs.DefectItem_DAO;
import data.Repositories.DefectItemRepository;

public class ReviewAndSubmitViewHolder extends RecyclerView.ViewHolder {
    public int mInspectionDefectId;
    private final TextView mTextDefectItemNumber;
    private final TextView mTextDefectItemDescription;
    private final TextView mTextInspectionDefectComment;

    public ReviewAndSubmitViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextDefectItemNumber = itemView.findViewById(R.id.item_inspection_defect_text_defect_number);
        mTextDefectItemDescription = itemView.findViewById(R.id.item_inspection_defect_text_defect_description);
        mTextInspectionDefectComment = itemView.findViewById(R.id.item_inspection_defect_text_comment);
    }

    public void bind(int inspectionDefectId, String comment) {
        mTextInspectionDefectComment.setText(comment);
    }

    public static ReviewAndSubmitViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspection_defect, parent, false);
        return new ReviewAndSubmitViewHolder(view);
    }
}

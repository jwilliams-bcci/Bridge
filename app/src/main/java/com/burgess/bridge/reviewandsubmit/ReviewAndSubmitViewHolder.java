package com.burgess.bridge.reviewandsubmit;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;
import com.google.android.material.snackbar.Snackbar;

public class ReviewAndSubmitViewHolder extends RecyclerView.ViewHolder {
    public int mInspectionId;
    public int mInspectionDefectId;
    public int mDefectItemId;
    private final TextView mTextDefectItemNumber;
    private final TextView mTextDefectItemDescription;
    private final TextView mTextInspectionDefectComment;
    private final ImageView mImageViewShowThumbnail;

    public ReviewAndSubmitViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextDefectItemNumber = itemView.findViewById(R.id.item_inspection_defect_text_defect_number);
        mTextDefectItemDescription = itemView.findViewById(R.id.item_inspection_defect_text_defect_description);
        mTextInspectionDefectComment = itemView.findViewById(R.id.item_inspection_defect_text_comment);
        mImageViewShowThumbnail = itemView.findViewById(R.id.item_inspection_defect_imageview);
    }

    public void bind(int inspectionId, int inspectionDefectId, int defectItemId, int defectItemNumber, String defectItemDescription, String comment, boolean showThumbnail) {
        mInspectionId = inspectionId;
        mInspectionDefectId = inspectionDefectId;
        mDefectItemId = defectItemId;
        mTextDefectItemNumber.setText(String.valueOf(defectItemNumber));
        mTextDefectItemDescription.setText(defectItemDescription);
        mTextInspectionDefectComment.setText(comment);
        if (showThumbnail) {
            mImageViewShowThumbnail.setVisibility(View.VISIBLE);
        } else {
            mImageViewShowThumbnail.setVisibility(View.INVISIBLE);
        }
    }

    public static ReviewAndSubmitViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspection_defect, parent, false);
        return new ReviewAndSubmitViewHolder(view);
    }
}

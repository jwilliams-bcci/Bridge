package com.burgess.bridge.reviewandsubmit;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;

import data.Views.ReviewAndSubmit_View;

public class ReviewAndSubmitListAdapter extends ListAdapter<ReviewAndSubmit_View, ReviewAndSubmitViewHolder> {

    protected ReviewAndSubmitListAdapter(@NonNull DiffUtil.ItemCallback<ReviewAndSubmit_View> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ReviewAndSubmitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ReviewAndSubmitViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAndSubmitViewHolder holder, int position) {
        ReviewAndSubmit_View current = getItem(position);
        boolean showThumbnail = false;
        if (current.picture_path != null) {
            showThumbnail = true;
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DefectItemActivity.class);
            intent.putExtra(DefectItemActivity.INSPECTION_ID, holder.mInspectionId);
            intent.putExtra(DefectItemActivity.DEFECT_ID, holder.mDefectItemId);
            intent.putExtra(DefectItemActivity.INSPECTION_DEFECT_ID, holder.mInspectionDefectId);
            v.getContext().startActivity(intent);
        });

        String status = "";
        switch(current.defect_status_id) {
            case 7:
                status = "NOTE";
                break;
            case 2:
                status = "NC";
                break;
            case 3:
                status = "C";
                break;
            case 6:
                status = "R";
                break;
            case 4:
                status = "NA";
                break;
        }

        holder.bind(current.inspection_id, current.inspection_defect_id, current.defect_item_id, current.item_number, current.item_description, current.comment, status, showThumbnail);
    }

    public static class ReviewAndSubmitDiff extends DiffUtil.ItemCallback<ReviewAndSubmit_View> {
        @Override
        public boolean areItemsTheSame(@NonNull ReviewAndSubmit_View oldItem, @NonNull ReviewAndSubmit_View newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReviewAndSubmit_View oldItem, @NonNull ReviewAndSubmit_View newItem) {
            return oldItem.inspection_defect_id == newItem.inspection_defect_id;
        }
    }
}

package com.burgess.bridge.reviewandsubmit;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.defectitem.DefectItemActivity;

import java.util.List;

import data.Views.ReviewAndSubmit_View;

public class ReviewAndSubmitListAdapter extends ListAdapter<ReviewAndSubmit_View, ReviewAndSubmitViewHolder> {
    private List<ReviewAndSubmit_View> currentList;

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
        if (current.PicturePath != null) {
            showThumbnail = true;
        }
        holder.itemView.setOnClickListener(v -> {
            if (holder.mIsEditable) {
                Intent intent = new Intent(v.getContext(), DefectItemActivity.class);
                intent.putExtra(DefectItemActivity.INSPECTION_ID, holder.mInspectionId);
                intent.putExtra(DefectItemActivity.DEFECT_ID, holder.mDefectItemId);
                intent.putExtra(DefectItemActivity.INSPECTION_DEFECT_ID, holder.mInspectionDefectId);
                v.getContext().startActivity(intent);
            }
        });

        String status = "";
        switch(current.DefectStatusID) {
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

        holder.bind(current.InspectionID, current.InspectionDefectID, current.DefectItemID, current.ItemNumber, current.ItemDescription, current.Comment, status, showThumbnail, current.IsEditable);
    }

    public List<ReviewAndSubmit_View> getCurrentList() { return currentList; }
    public void setCurrentList(List<ReviewAndSubmit_View> list) {
        currentList = list;
        submitList(list);
    }

    public static class ReviewAndSubmitDiff extends DiffUtil.ItemCallback<ReviewAndSubmit_View> {
        @Override
        public boolean areItemsTheSame(@NonNull ReviewAndSubmit_View oldItem, @NonNull ReviewAndSubmit_View newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReviewAndSubmit_View oldItem, @NonNull ReviewAndSubmit_View newItem) {
            return oldItem.InspectionDefectID == newItem.InspectionDefectID;
        }
    }
}

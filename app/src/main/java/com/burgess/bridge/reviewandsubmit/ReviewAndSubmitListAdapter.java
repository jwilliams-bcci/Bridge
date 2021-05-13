package com.burgess.bridge.reviewandsubmit;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import data.ReviewAndSubmit_View;

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
        holder.bind(current.item_number, current.item_description, current.comment, showThumbnail);
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

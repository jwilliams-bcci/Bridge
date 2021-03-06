package com.example.bridge.inspect;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import data.Tables.DefectItem_Table;

public class InspectListAdapter extends ListAdapter<DefectItem_Table, InspectViewHolder> {

    protected InspectListAdapter(@NonNull DiffUtil.ItemCallback<DefectItem_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public InspectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return InspectViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectViewHolder holder, int position) {
        DefectItem_Table current = getItem(position);
        boolean showSection = true;
        if (position > 0) {
            DefectItem_Table previous = getItem(position-1);
            if (previous.category_name.equals(current.category_name)) {
                showSection = false;
            } else {
                showSection = true;
            }
        }
        holder.mDefectItemId = current.id;
        holder.bind(String.valueOf(current.item_number), String.valueOf(current.item_description), String.valueOf(current.category_name), showSection);
    }

    public static class InspectDiff extends DiffUtil.ItemCallback<DefectItem_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull DefectItem_Table oldItem, @NonNull DefectItem_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DefectItem_Table oldItem, @NonNull DefectItem_Table newItem) {
            return oldItem.id == newItem.id;
        }
    }
}

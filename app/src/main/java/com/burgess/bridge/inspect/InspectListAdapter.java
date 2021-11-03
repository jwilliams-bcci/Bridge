package com.burgess.bridge.inspect;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;

import data.Tables.DefectItem_Table;

public class InspectListAdapter extends ListAdapter<DefectItem_Table, InspectViewHolder> {
    private int mInspectionId;
    private int mInspectionTypeId;
    private String mFilter;

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
            if (previous.defect_category_name.equals(current.defect_category_name)) {
                showSection = false;
            } else {
                showSection = true;
            }
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DefectItemActivity.class);
            intent.putExtra(DefectItemActivity.INSPECTION_ID, holder.mInspectionId);
            intent.putExtra(DefectItemActivity.DEFECT_ID, holder.mDefectItemId);
            intent.putExtra(DefectItemActivity.INSPECTION_HISTORY_ID, holder.mInspectionHistoryId);
            intent.putExtra(DefectItemActivity.FILTER_OPTION, holder.mFilter);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.mDefectItemId = current.id;
        holder.bind(String.valueOf(current.item_number), String.valueOf(current.item_description), String.valueOf(current.defect_category_name), showSection, mInspectionId, mInspectionTypeId, -1, -1, "", mFilter);
    }

    public void setInspectionId (int inspectionId) {
        mInspectionId = inspectionId;
    }

    public void setInspectionTypeId (int inspectionTypeId) {
        mInspectionTypeId = inspectionTypeId;
    }

    public void setFilter (String filter) {
        mFilter = filter;
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

package com.burgess.bridge.inspect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;

import org.jetbrains.annotations.NotNull;

import data.Tables.InspectionHistory_Table;

public class ReinspectListAdapter extends ListAdapter<InspectionHistory_Table, InspectViewHolder> {
    private int mInspectionId;
    private int mInspectionHistoryId;
    private int mInspectionTypeId;

    protected ReinspectListAdapter(@NonNull @NotNull DiffUtil.ItemCallback<InspectionHistory_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @NotNull
    @Override
    public InspectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return InspectViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InspectViewHolder holder, int position) {
        InspectionHistory_Table current = getItem(position);
        Group group = holder.itemView.findViewById(R.id.item_defect_item_group);
        TextView textDescription = holder.itemView.findViewById(R.id.item_defect_item_text_description);
        TextView textNumber = holder.itemView.findViewById(R.id.item_defect_item_text_number);
        boolean showSection = false;
        if (!current.is_reviewed) {
            textNumber.setTextColor(Color.WHITE);
            textDescription.setTextColor(Color.WHITE);
            group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_not_reviewed));

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), DefectItemActivity.class);
                intent.putExtra(DefectItemActivity.INSPECTION_ID, holder.mInspectionId);
                intent.putExtra(DefectItemActivity.INSPECTION_TYPE_ID, holder.mInspectionTypeId);
                intent.putExtra(DefectItemActivity.DEFECT_ID, holder.mDefectItemId);
                intent.putExtra(DefectItemActivity.INSPECTION_HISTORY_ID, holder.mInspectionHistoryId);
                holder.itemView.getContext().startActivity(intent);
            });
        } else {
            switch (current.reviewed_status) {
                case 2:
                    group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_reviewed_failed));
                    break;
                default:
                    group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_reviewed_complete));
                    break;
            }
            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Please edit in Review & Submit screen, id: " + current.id, Toast.LENGTH_LONG).show();
            });
        }
        holder.mDefectItemId = current.defect_item_id;
        holder.bind(String.valueOf(current.defect_item_number), current.defect_item_description + "\n" + current.comment, String.valueOf(current.defect_category_name), showSection, mInspectionId, mInspectionTypeId, current.id);
    }

    public void setInspectionId(int inspectionId) {
        mInspectionId = inspectionId;
    }

    public void setInspectionHistoryId(int inspectionHistoryId) {
        mInspectionHistoryId = inspectionHistoryId;
    }

    public void setInspectionTypeId(int inspectionTypeId) {
        mInspectionTypeId = inspectionTypeId;
    }

    public static class InspectDiff extends DiffUtil.ItemCallback<InspectionHistory_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            return oldItem.id == newItem.id;
        }
    }
}

package com.burgess.bridge.inspect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import data.Tables.InspectionHistory_Table;

public class ReinspectListAdapter extends ListAdapter<InspectionHistory_Table, InspectViewHolder> {
    public int mInspectionId;
    public int mInspectionHistoryId;
    public int mInspectionTypeId;

    protected ReinspectListAdapter(@NonNull @NotNull DiffUtil.ItemCallback<InspectionHistory_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @NotNull
    @Override
    public InspectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_defect_item, parent, false);
        return new InspectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InspectViewHolder holder, int position) {
        InspectionHistory_Table inspectionHistory = getCurrentList().get(position);
        Group group = holder.itemView.findViewById(R.id.item_defect_item_group);

        TextView textSection = holder.getTextDefectItemSection();
        TextView textNumber = holder.getTextDefectItemNumber();
        TextView textDescription = holder.getTextDefectItemDescription();

        // Set text fields
        textSection.setVisibility(View.GONE);
        textNumber.setText(Integer.toString(inspectionHistory.defect_item_number));
        textDescription.setText(inspectionHistory.defect_item_description);

        // Set the background according to whether it's reviewed or not
        if (!inspectionHistory.is_reviewed) {
            textNumber.setTextColor(Color.WHITE);
            textDescription.setTextColor(Color.WHITE);
            group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_not_reviewed));
        } else {
            switch (inspectionHistory.reviewed_status) {
                case 2:
                    group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_reviewed_failed));
                    textDescription.setTextColor(Color.WHITE);
                    textNumber.setTextColor(Color.WHITE);
                    break;
                default:
                    group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_reviewed_complete));
                    textDescription.setTextColor(Color.BLACK);
                    textNumber.setTextColor(Color.BLACK);
                    break;
            }
        }

        // Add click listener for the RecyclerView items
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DefectItemActivity.class);
            intent.putExtra(DefectItemActivity.INSPECTION_ID, inspectionHistory.inspection_id);
            intent.putExtra(DefectItemActivity.DEFECT_ID, inspectionHistory.defect_item_id);
            intent.putExtra(DefectItemActivity.INSPECTION_HISTORY_ID, inspectionHistory.id);
            intent.putExtra(DefectItemActivity.INSPECTION_DEFECT_ID, inspectionHistory.inspection_defect_id);
            holder.itemView.getContext().startActivity(intent);
        });

        // Set the variables for swipe functionality in Activity
        holder.setInspectionHistoryId(inspectionHistory.id);
        holder.setInspectionDefectId(inspectionHistory.inspection_defect_id);
        holder.setDefectItemId(inspectionHistory.defect_item_id);
        holder.setComment(inspectionHistory.comment);
    }

    public static class ReinspectDiff extends DiffUtil.ItemCallback<InspectionHistory_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            return Objects.equals(oldItem.reviewed_status, newItem.reviewed_status);
        }
    }
}
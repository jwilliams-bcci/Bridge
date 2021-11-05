package com.burgess.bridge.inspect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
        return InspectViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InspectViewHolder holder, int position) {
        InspectionHistory_Table current = getItem(position);
        Group group = holder.itemView.findViewById(R.id.item_defect_item_group);
        TextView textDescription = holder.itemView.findViewById(R.id.item_defect_item_text_description);
        TextView textNumber = holder.itemView.findViewById(R.id.item_defect_item_text_number);
        if (!current.is_reviewed) {
            textNumber.setTextColor(Color.WHITE);
            textDescription.setTextColor(Color.WHITE);
            group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_not_reviewed));
        } else {
            switch (current.reviewed_status) {
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
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DefectItemActivity.class);
            intent.putExtra(DefectItemActivity.INSPECTION_ID, holder.mInspectionId);
            intent.putExtra(DefectItemActivity.DEFECT_ID, holder.mDefectItemId);
            intent.putExtra(DefectItemActivity.INSPECTION_HISTORY_ID, holder.mInspectionHistoryId);
            intent.putExtra(DefectItemActivity.INSPECTION_DEFECT_ID, holder.mInspectionDefectId);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.mDefectItemId = current.defect_item_id;
        holder.bind(String.valueOf(current.defect_item_number), current.defect_item_description + "\n" + current.comment, String.valueOf(current.defect_category_name), false, mInspectionId, mInspectionTypeId, current.id, current.inspection_defect_id, current.comment, null);
    }

    public static class ReinspectDiff extends DiffUtil.ItemCallback<InspectionHistory_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            System.out.println("areItemsTheSame == oldItem: " + oldItem.id + " -- newItem: " + newItem.id);
            Log.i("DIFFUTIL", "areItemsTheSame == oldItem: " + oldItem.id + " -- newItem: " + newItem.id);
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            System.out.println("areContentsTheSame == oldItem: " + oldItem.reviewed_status + " -- newItem: " + newItem.reviewed_status);
            Log.i("DIFFUTIL", "areContentsTheSame == oldItem: " + oldItem.reviewed_status + " -- newItem: " + newItem.reviewed_status);
            return oldItem.is_reviewed == newItem.is_reviewed && oldItem.reviewed_status == newItem.reviewed_status;
        }

//        @Nullable
//        @Override
//        public Object getChangePayload(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
//            System.out.println("getChangePayload == oldItem: " + oldItem.id + " -- newItem: " + newItem.id);
//            Log.i("DIFFUTIL", "getChangePayload == oldItem: " + oldItem.id + " -- newItem: " + newItem.id);
//            return super.getChangePayload(oldItem, newItem);
//        }
    }
}
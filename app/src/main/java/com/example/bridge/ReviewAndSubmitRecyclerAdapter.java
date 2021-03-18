package com.example.bridge;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import data.DataManager;
import data.DefectItem;
import data.InspectionDefect;

public class ReviewAndSubmitRecyclerAdapter extends RecyclerView.Adapter<ReviewAndSubmitRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<InspectionDefect> mInspectionDefects;
    private final LayoutInflater mLayoutInflater;

    public ReviewAndSubmitRecyclerAdapter(Context context, List<InspectionDefect> inspectionDefectList) {
        mContext = context;
        mInspectionDefects = inspectionDefectList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_inspection_defect, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InspectionDefect inspectionDefect = mInspectionDefects.get(position);
        DefectItem defectItem = DataManager.getInstance().getDefectItem(inspectionDefect.getDefectItemId());
        holder.mTextDefectNumber.setText(Integer.toString(defectItem.getItemNumber()));
        holder.mTextDefectDescription.setText(defectItem.getItemDescription());
        holder.mTextStatus.setText(Integer.toString(inspectionDefect.getDefectStatusId()));
        holder.mTextLocation.setText(inspectionDefect.getLocation());
        holder.mTextRoom.setText(inspectionDefect.getRoom());
        holder.mTextDirection.setText(inspectionDefect.getDirection());
        holder.mTextNote.setText(inspectionDefect.getNote());

        boolean isExpanded = inspectionDefect.isVisible();
        holder.mExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mInspectionDefects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextDefectNumber;
        public TextView mTextDefectDescription;
        public TextView mTextStatus;
        public TextView mTextLocation;
        public TextView mTextRoom;
        public TextView mTextDirection;
        public TextView mTextNote;
        public ConstraintLayout mExpandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextDefectNumber = (TextView) itemView.findViewById(R.id.item_inspection_defect_text_defect_number);
            mTextDefectDescription = (TextView) itemView.findViewById(R.id.item_inspection_defect_text_defect_description);
            mTextStatus = (TextView) itemView.findViewById(R.id.item_inspection_defect_text_comment);
            mExpandableLayout = (ConstraintLayout) itemView.findViewById(R.id.item_inspection_defect_constraint_layout_expandable_layout);

            mTextDefectNumber.setOnClickListener(v -> {
                Log.d("R&S", "Inspection Number Clicked, expand");
                InspectionDefect inspectionDefect = mInspectionDefects.get(getAdapterPosition());
                inspectionDefect.setVisible(!inspectionDefect.isVisible());
                notifyItemChanged(getAdapterPosition());
            });

            mTextDefectDescription.setOnClickListener(v -> {
                Log.d("R&S", "Inspection Description Clicked, expand");
                InspectionDefect inspectionDefect = mInspectionDefects.get(getAdapterPosition());
                inspectionDefect.setVisible(!inspectionDefect.isVisible());
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}

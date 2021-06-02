package com.burgess.bridge.inspect;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.defectitem.DefectItemActivity;
import com.burgess.bridge.R;

public class InspectViewHolder extends RecyclerView.ViewHolder {
    public int mDefectItemId;
    public int mInspectionHistoryId;
    public int mInspectionTypeId;
    public int mInspectionId;
    private final TextView mTextDefectItemNumber;
    private final TextView mTextDefectItemDescription;
    private final TextView mTextDefectItemSection;

    public InspectViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextDefectItemNumber = itemView.findViewById(R.id.item_defect_item_text_number);
        mTextDefectItemDescription = itemView.findViewById(R.id.item_defect_item_text_description);
        mTextDefectItemSection = itemView.findViewById(R.id.item_defect_item_text_section);

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DefectItemActivity.class);
            intent.putExtra(DefectItemActivity.INSPECTION_ID, mInspectionId);
            intent.putExtra(DefectItemActivity.INSPECTION_TYPE_ID, mInspectionTypeId);
            intent.putExtra(DefectItemActivity.DEFECT_ID, mDefectItemId);
            intent.putExtra(DefectItemActivity.INSPECTION_HISTORY_ID, mInspectionHistoryId);
            Toast.makeText(v.getContext(), mTextDefectItemDescription.getText() + " selected", Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        });
    }

    public void bind(String itemNumber, String itemDescription, String sectionName, boolean showSection, int inspectionId, int inspectionTypeId) {
        mTextDefectItemSection.setText(sectionName);
        mTextDefectItemNumber.setText(itemNumber);
        mTextDefectItemDescription.setText(itemDescription);
        mInspectionId = inspectionId;
        mInspectionTypeId = inspectionTypeId;

        if (showSection) {
            mTextDefectItemSection.setVisibility(View.VISIBLE);
        } else {
            mTextDefectItemSection.setVisibility(View.GONE);
        }
    }

    public static InspectViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_defect_item, parent, false);
        return new InspectViewHolder(view);
    }
}

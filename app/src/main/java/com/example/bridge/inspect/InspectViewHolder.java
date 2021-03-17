package com.example.bridge.inspect;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.defectitem.DefectItemActivity;
import com.example.bridge.R;

public class InspectViewHolder extends RecyclerView.ViewHolder {
    public int mDefectItemId;
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
            intent.putExtra(DefectItemActivity.DEFECT_ID, mDefectItemId);
            Toast.makeText(v.getContext(), mTextDefectItemDescription.getText() + " selected", Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        });
    }

    public void bind(String itemNumber, String itemDescription, String sectionName, boolean showSection) {
        mTextDefectItemSection.setText(sectionName);
        mTextDefectItemNumber.setText(itemNumber);
        mTextDefectItemDescription.setText(itemDescription);

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

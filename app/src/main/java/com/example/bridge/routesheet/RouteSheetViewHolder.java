package com.example.bridge.routesheet;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bridge.InspectionDetailsActivity;
import com.example.bridge.R;

public class RouteSheetViewHolder extends RecyclerView.ViewHolder {
    public int mInspectionId;
    public int mBuilderName;
    public String mInspectionAddress;

    private final TextView mTextInspectionCommunity;
    private final TextView mTextInspectionAddress;
    private final TextView mTextInspectionType;
    private final TextView mTextInspectionNotes;

    public RouteSheetViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextInspectionCommunity = itemView.findViewById(R.id.text_recycler_community);
        mTextInspectionAddress = itemView.findViewById(R.id.text_recycler_address);
        mTextInspectionType = itemView.findViewById(R.id.text_recycler_inspection_type);
        mTextInspectionNotes = itemView.findViewById(R.id.text_recycler_inspection_notes);

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InspectionDetailsActivity.class);
            intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, mInspectionId);
            Toast.makeText(v.getContext(), "Inspection " + mInspectionId + " selected", Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        });
    }

    public void bind(String community, String address, String inspectionType, String notes) {
        mTextInspectionCommunity.setText(community);
        mTextInspectionAddress.setText(address);
        mTextInspectionType.setText(inspectionType);
        mTextInspectionNotes.setText(notes);
    }

    public static RouteSheetViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspection_list, parent, false);
        return new RouteSheetViewHolder(view);
    }
}

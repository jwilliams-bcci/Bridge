package com.burgess.bridge.routesheet;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.InspectionDetailsActivity;
import com.burgess.bridge.R;

public class RouteSheetViewHolder extends RecyclerView.ViewHolder {
    public int mInspectionId;
    public int mInspectionTypeId;
    public int mBuilderName;
    public String mInspectionAddress;

    private final TextView mTextInspectionCommunity;
    private final TextView mTextInspectionAddress;
    private final TextView mTextInspectionType;
    private final TextView mTextInspectionNotes;
    public final ImageView mReorderHandle;

    public RouteSheetViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextInspectionCommunity = itemView.findViewById(R.id.text_recycler_community);
        mTextInspectionAddress = itemView.findViewById(R.id.text_recycler_address);
        mTextInspectionType = itemView.findViewById(R.id.text_recycler_inspection_type);
        mTextInspectionNotes = itemView.findViewById(R.id.text_recycler_inspection_notes);
        mReorderHandle = itemView.findViewById(R.id.item_inspection_list_imageview_reorder_handle);

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InspectionDetailsActivity.class);
            intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, mInspectionId);
            intent.putExtra(InspectionDetailsActivity.INSPECTION_TYPE_ID, mInspectionTypeId);
            v.getContext().startActivity(intent);
        });

        mReorderHandle.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d("DRAG", "Action is ACTION_DOWN");
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("DRAG", "Action is ACTION_UP");
            }
            return true;
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
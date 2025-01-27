package com.burgess.bridge.routesheet;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

import java.time.OffsetDateTime;
import java.util.Locale;

import data.Views.RouteSheet_View;

public class RouteSheetViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = "ROUTE_SHEET";

    public final CardView cardView;
    public final TextView tCommunity;
    public final TextView tAddress;
    public final TextView tInspectionType;
    public final ImageView ivShowMenu;
    public final TextView tCallbackNotes;
    public final TextView tUploaded;
    public final ImageView ivReupload;
    public final ImageView ivNoteAttached;
    public final ConstraintLayout constraintLayoutMenu;
    public final View verticalDivider;
    public final Button bReupload;
    public final Button bResetInspection;
    public final Button bViewQueue;
    private final OnItemClickListener itemClickListener;
    private final OnItemButtonClickListener buttonClickListener;
    private final RouteSheetListAdapter adapter;
    private boolean showMenu = false;

    public RouteSheetViewHolder(@NonNull View itemView, OnItemClickListener itemClickListener,
                                OnItemButtonClickListener buttonClickListener,
                                RouteSheetListAdapter adapter) {
        super(itemView);
        cardView = itemView.findViewById(R.id.item_inspection_list_card_view);
        tCommunity = itemView.findViewById(R.id.text_recycler_community);
        tAddress = itemView.findViewById(R.id.text_recycler_address);
        tInspectionType = itemView.findViewById(R.id.text_recycler_inspection_type);
        ivShowMenu = itemView.findViewById(R.id.item_inspection_list_imageview_show_menu);
        tCallbackNotes = itemView.findViewById(R.id.text_recycler_callback_notes);
        tUploaded = itemView.findViewById(R.id.text_recycler_uploaded);
        ivReupload = itemView.findViewById(R.id.item_inspection_list_imageview_reupload);
        ivNoteAttached = itemView.findViewById(R.id.item_inspection_list_imageview_note_attached);
        constraintLayoutMenu = itemView.findViewById(R.id.item_inspection_constraint_layout_menu);
        verticalDivider = itemView.findViewById(R.id.item_inspection_list_divider);
        bReupload = itemView.findViewById(R.id.item_inspection_list_button_reupload);
        bResetInspection = itemView.findViewById(R.id.item_inspection_list_button_reset_inspection);
        bViewQueue = itemView.findViewById(R.id.item_inspection_list_button_view_queue);
        this.itemClickListener = itemClickListener;
        this.buttonClickListener = buttonClickListener;
        this.adapter = adapter;
    }

    public void bind(RouteSheet_View inspection) {
        String addressDisplay;
        if (!TextUtils.isEmpty(inspection.JobNumber) && inspection.JobNumber.toLowerCase().startsWith("lot")) {
            addressDisplay = inspection.Address + " - " + inspection.JobNumber;
        } else {
            addressDisplay = inspection.Address;
        }
        tAddress.setText(addressDisplay);
        tCommunity.setText(inspection.Community);
        tInspectionType.setText(inspection.InspectionType);
        cardView.setBackgroundColor(Color.WHITE);
        tUploaded.setVisibility(View.GONE);
        ivReupload.setVisibility(View.GONE);
        ivNoteAttached.setVisibility(View.GONE);
        tCallbackNotes.setVisibility(View.GONE);
        constraintLayoutMenu.setVisibility(View.GONE);
        verticalDivider.setVisibility(View.GONE);
        bReupload.setVisibility(View.GONE);
        bResetInspection.setVisibility(View.VISIBLE);
        bViewQueue.setVisibility(View.GONE);

        // If the inspection is complete, set the color to yellow and show other view components
        if (inspection.IsComplete) {
            cardView.setBackgroundColor(Color.YELLOW);
            tUploaded.setVisibility(View.VISIBLE);
            tUploaded.setText(String.format(Locale.US, "Uploaded %d of %d items.", inspection.NumUploaded, inspection.NumTotal));
            //bReupload.setVisibility(View.VISIBLE);
            //verticalDivider.setVisibility(View.VISIBLE);
            //bViewQueue.setVisibility(View.VISIBLE);
        }

        // If the inspection submit time is over two minutes old, assume failed, set IsFailed
        if (inspection.SubmitTime != null && !inspection.RequireRiskAssessment) {
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime twoMinutesAgo = now.minusMinutes(2);
            if (inspection.SubmitTime.isBefore(twoMinutesAgo)) {
                System.out.println("Submit time: " + inspection.SubmitTime.minusMinutes(2).toString() + " -- Now: " + now);
                inspection.IsFailed = true;
            }
        }

        // If the inspection is failed, set the color to red
        if (inspection.IsFailed) {
            cardView.setBackgroundColor(Color.RED);
            bReupload.setVisibility(View.VISIBLE);
        }

        // If the inspection is uploaded, set the color to green, disable buttons
        if (inspection.IsUploaded) {
            cardView.setBackgroundColor(Color.GREEN);
            constraintLayoutMenu.setVisibility(View.GONE);
            ivReupload.setVisibility(View.GONE);
        }

        // If there's a note, show the note icon
        if (!TextUtils.isEmpty(inspection.Notes) && !inspection.Notes.equals("null")) {
            ivNoteAttached.setVisibility(View.VISIBLE);
            tCallbackNotes.setVisibility(View.VISIBLE);
            tCallbackNotes.setText(inspection.Notes);
            tCallbackNotes.setSelected(true);
        }

        BindClickListeners();
    }

    private void BindClickListeners() {
        itemView.setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                RouteSheet_View inspection = adapter.getItem(position);
                itemClickListener.onItemClick(inspection);
            }
        });

        ivShowMenu.setOnClickListener(v -> {
            showMenu = !showMenu;
            if (showMenu) {
                constraintLayoutMenu.setVisibility(View.VISIBLE);
                ivShowMenu.setBackgroundResource(android.R.drawable.arrow_up_float);
            } else {
                constraintLayoutMenu.setVisibility(View.GONE);
                ivShowMenu.setBackgroundResource(android.R.drawable.arrow_down_float);
            }
        });

        bReupload.setOnClickListener(v -> {
            buttonClickListener.onButtonClick(adapter.getItem(getBindingAdapterPosition()), R.id.item_inspection_list_button_reupload);
        });
        bResetInspection.setOnClickListener(v -> {
            buttonClickListener.onButtonClick(adapter.getItem(getBindingAdapterPosition()), R.id.item_inspection_list_button_reset_inspection);
        });
        bViewQueue.setOnClickListener(v -> {
            buttonClickListener.onButtonClick(adapter.getItem(getBindingAdapterPosition()), R.id.item_inspection_list_button_view_queue);
        });
    }

    public interface OnItemClickListener {
        void onItemClick(RouteSheet_View inspection);
    }
    public interface OnItemButtonClickListener {
        void onButtonClick(RouteSheet_View inspection, int buttonId);
    }
}

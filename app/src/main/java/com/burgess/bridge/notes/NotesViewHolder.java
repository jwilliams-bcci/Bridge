package com.burgess.bridge.notes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

import data.Tables.InspectionHistory_Table;

public class NotesViewHolder extends RecyclerView.ViewHolder {
    public final CardView cardView;
    public final ConstraintLayout constraintLayout;
    public final TextView tComment;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.item_note_card_view);
        constraintLayout = itemView.findViewById(R.id.item_note_constraint_layout);
        tComment = itemView.findViewById(R.id.item_note_text_comment);
    }

    public void bind(InspectionHistory_Table inspectionHistory) {
        tComment.setText(inspectionHistory.Comment);
    }
}

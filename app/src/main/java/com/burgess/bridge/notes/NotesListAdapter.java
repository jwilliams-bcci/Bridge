package com.burgess.bridge.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;

import java.util.List;

import data.Tables.InspectionHistory_Table;

public class NotesListAdapter extends ListAdapter<InspectionHistory_Table, NotesViewHolder> {
    private List<InspectionHistory_Table> currentList;

    protected NotesListAdapter(@NonNull DiffUtil.ItemCallback<InspectionHistory_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        InspectionHistory_Table note = getCurrentList().get(position);
        holder.bind(note);
    }

    public static class NoteDiff extends DiffUtil.ItemCallback<InspectionHistory_Table> {

        @Override
        public boolean areItemsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            return oldItem.InspectionDetailID == newItem.InspectionDetailID;
        }

        @Override
        public boolean areContentsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            return oldItem.Comment.equals(newItem.Comment);
        }
    }
}

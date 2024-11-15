package com.burgess.bridge.ekotrope_windowslist;

import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_windows.Ekotrope_WindowsActivity;

import java.util.List;

import data.Tables.Ekotrope_Window_Table;

public class Ekotrope_WindowsListAdapter extends ListAdapter<Ekotrope_Window_Table, Ekotrope_WindowsListViewHolder> {
    public static final String TAG = "WINDOWS_LIST";

    private int mInspectionId;
    private String mEkotropeProjectId;
    private List<Ekotrope_Window_Table> currentList;

    protected Ekotrope_WindowsListAdapter(@NonNull DiffUtil.ItemCallback<Ekotrope_Window_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public Ekotrope_WindowsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_window, parent, false);
        return new Ekotrope_WindowsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ekotrope_WindowsListViewHolder holder, int position) {
        Ekotrope_Window_Table window = currentList.get(position);

        // Get text fields
        TextView textIndex = holder.getTextIndex();
        TextView textName = holder.getTextName();

        // Set text fields
        holder.setTextIndex(Integer.toString(window.index));
        holder.setTextName(window.name);

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            Intent windowIntent = new Intent(holder.itemView.getContext(), Ekotrope_WindowsActivity.class);
            windowIntent.putExtra(INSPECTION_ID, mInspectionId);
            windowIntent.putExtra(EKOTROPE_PROJECT_ID, mEkotropeProjectId);
            windowIntent.putExtra(Ekotrope_WindowsActivity.PLAN_ID, window.plan_id);
            windowIntent.putExtra(Ekotrope_WindowsActivity.WINDOW_INDEX, window.index);
            holder.itemView.getContext().startActivity(windowIntent);
        });
    }
    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }
    public void setInspectionId(int inspectionId) {
        mInspectionId = inspectionId;
    }
    public void setEkotropeProjectId(String ekotropeProjectId) {
        mEkotropeProjectId = ekotropeProjectId;
    }
    public List<Ekotrope_Window_Table> getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List<Ekotrope_Window_Table> list) {
        currentList = list;
        submitList(list);
    }

    public static class Ekotrope_WindowsDiff extends DiffUtil.ItemCallback<Ekotrope_Window_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Ekotrope_Window_Table oldItem, @NonNull Ekotrope_Window_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ekotrope_Window_Table oldItem, @NonNull Ekotrope_Window_Table newItem) {
            return oldItem.index == newItem.index;
        }
    }
}
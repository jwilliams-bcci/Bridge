package com.burgess.bridge.ekotrope_rimjoistslist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_rimjoists.Ekotrope_RimJoistsActivity;

import java.util.List;

import data.Tables.Ekotrope_RimJoist_Table;

public class Ekotrope_RimJoistsListAdapter extends ListAdapter<Ekotrope_RimJoist_Table, Ekotrope_RimJoistsListViewHolder> {
    public static final String TAG = "RIMJOISTS_LIST";

    private List<Ekotrope_RimJoist_Table> currentList;

    protected Ekotrope_RimJoistsListAdapter(@NonNull DiffUtil.ItemCallback<Ekotrope_RimJoist_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public Ekotrope_RimJoistsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rim_joist, parent, false);
        return new Ekotrope_RimJoistsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ekotrope_RimJoistsListViewHolder holder, int position) {
        Ekotrope_RimJoist_Table rimJoist = currentList.get(position);

        // Get text fields
        TextView textIndex = holder.getTextIndex();
        TextView textName = holder.getTextName();

        // Set text fields
        holder.setTextIndex(Integer.toString(rimJoist.index));
        holder.setTextName(rimJoist.name);

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            Intent rimJoistIntent = new Intent(holder.itemView.getContext(), Ekotrope_RimJoistsActivity.class);
            rimJoistIntent.putExtra(Ekotrope_RimJoistsActivity.PLAN_ID, rimJoist.plan_id);
            rimJoistIntent.putExtra(Ekotrope_RimJoistsActivity.RIM_JOIST_INDEX, rimJoist.index);
            holder.itemView.getContext().startActivity(rimJoistIntent);
        });
    }
    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }
    public List<Ekotrope_RimJoist_Table> getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List<Ekotrope_RimJoist_Table> list) {
        currentList = list;
        submitList(list);
    }

    public static class Ekotrope_RimJoistsDiff extends DiffUtil.ItemCallback<Ekotrope_RimJoist_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Ekotrope_RimJoist_Table oldItem, @NonNull Ekotrope_RimJoist_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ekotrope_RimJoist_Table oldItem, @NonNull Ekotrope_RimJoist_Table newItem) {
            return oldItem.index == newItem.index;
        }
    }
}

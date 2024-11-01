package com.burgess.bridge.ekotrope_ceilingslist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_ceilings.Ekotrope_CeilingsActivity;

import java.util.List;

import data.Tables.Ekotrope_Ceiling_Table;

public class Ekotrope_CeilingsListAdapter extends ListAdapter<Ekotrope_Ceiling_Table, Ekotrope_CeilingsListViewHolder> {
    public static final String TAG = "CEILINGS_LIST";

    private List<Ekotrope_Ceiling_Table> currentList;

    protected Ekotrope_CeilingsListAdapter(@NonNull DiffUtil.ItemCallback<Ekotrope_Ceiling_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public Ekotrope_CeilingsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ceiling, parent, false);
        return new Ekotrope_CeilingsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ekotrope_CeilingsListViewHolder holder, int position) {
        Ekotrope_Ceiling_Table ceiling = currentList.get(position);

        // Get text fields
        TextView textIndex = holder.getTextIndex();
        TextView textName = holder.getTextName();

        // Set text fields
        holder.setTextIndex(Integer.toString(ceiling.index));
        holder.setTextName(ceiling.name);

        // Set on click listeners
        holder.itemView.setOnClickListener(v -> {
            Intent ceilingIntent = new Intent(holder.itemView.getContext(), Ekotrope_CeilingsActivity.class);
            ceilingIntent.putExtra(Ekotrope_CeilingsActivity.PLAN_ID, ceiling.plan_id);
            ceilingIntent.putExtra(Ekotrope_CeilingsActivity.CEILING_INDEX, ceiling.index);
            holder.itemView.getContext().startActivity(ceilingIntent);
        });
    }
    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }
    public List<Ekotrope_Ceiling_Table> getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List<Ekotrope_Ceiling_Table> list) {
        currentList = list;
        submitList(list);
    }

    public static class Ekotrope_CeilingsDiff extends DiffUtil.ItemCallback<Ekotrope_Ceiling_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Ekotrope_Ceiling_Table oldItem, @NonNull Ekotrope_Ceiling_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ekotrope_Ceiling_Table oldItem, @NonNull Ekotrope_Ceiling_Table newItem) {
            return oldItem.index == newItem.index;
        }
    }

}

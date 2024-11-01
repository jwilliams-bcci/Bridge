package com.burgess.bridge.ekotrope_slabslist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_slabs.Ekotrope_SlabsActivity;

import java.util.List;

import data.Tables.Ekotrope_Slab_Table;

public class Ekotrope_SlabsListAdapter extends ListAdapter<Ekotrope_Slab_Table, Ekotrope_SlabsListViewHolder> {
    public static final String TAG = "SLABS_LIST";

    private List<Ekotrope_Slab_Table> currentList;

    protected Ekotrope_SlabsListAdapter(@NonNull DiffUtil.ItemCallback<Ekotrope_Slab_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public Ekotrope_SlabsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slab, parent, false);
        return new Ekotrope_SlabsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ekotrope_SlabsListViewHolder holder, int position) {
        Ekotrope_Slab_Table slab = currentList.get(position);

        // Get text fields
        TextView textIndex = holder.getTextIndex();
        TextView textName = holder.getTextName();

        // Set text fields
        holder.setTextIndex(Integer.toString(slab.index));
        holder.setTextName(slab.name);

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            Intent slabIntent = new Intent(holder.itemView.getContext(), Ekotrope_SlabsActivity.class);
            slabIntent.putExtra(Ekotrope_SlabsActivity.PLAN_ID, slab.plan_id);
            slabIntent.putExtra(Ekotrope_SlabsActivity.SLAB_INDEX, slab.index);
            holder.itemView.getContext().startActivity(slabIntent);
        });
    }
    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }
    public List<Ekotrope_Slab_Table> getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List<Ekotrope_Slab_Table> list) {
        currentList = list;
        submitList(list);
    }

    public static class Ekotrope_SlabsDiff extends DiffUtil.ItemCallback<Ekotrope_Slab_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Ekotrope_Slab_Table oldItem, @NonNull Ekotrope_Slab_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ekotrope_Slab_Table oldItem, @NonNull Ekotrope_Slab_Table newItem) {
            return oldItem.index == newItem.index;
        }
    }
}

package com.burgess.bridge.ekotrope_abovegradewallslist;

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
import com.burgess.bridge.ekotrope_abovegradewalls.Ekotrope_AboveGradeWallsActivity;

import java.util.List;

import data.Tables.Ekotrope_AboveGradeWall_Table;

public class Ekotrope_AboveGradeWallsListAdapter extends ListAdapter<Ekotrope_AboveGradeWall_Table, Ekotrope_AboveGradeWallsListViewHolder> {
    public static final String TAG = "ABOVE_GRADE_WALLS_LIST";

    private int mInspectionId;
    private String mEkotropeProjectId;
    private List<Ekotrope_AboveGradeWall_Table> currentList;

    protected Ekotrope_AboveGradeWallsListAdapter(@NonNull DiffUtil.ItemCallback<Ekotrope_AboveGradeWall_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public Ekotrope_AboveGradeWallsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_above_grade_wall, parent, false);
        return new Ekotrope_AboveGradeWallsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ekotrope_AboveGradeWallsListViewHolder holder, int position) {
        Ekotrope_AboveGradeWall_Table aboveGradeWall = currentList.get(position);

        // Set text fields
        holder.setTextIndex(Integer.toString(aboveGradeWall.index));
        holder.setTextName(aboveGradeWall.name);

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), Ekotrope_AboveGradeWallsActivity.class);
            intent.putExtra(INSPECTION_ID, mInspectionId);
            intent.putExtra(EKOTROPE_PROJECT_ID, mEkotropeProjectId);
            intent.putExtra(Ekotrope_AboveGradeWallsActivity.PLAN_ID, aboveGradeWall.plan_id);
            intent.putExtra(Ekotrope_AboveGradeWallsActivity.ABOVE_GRADE_WALLS_INDEX, aboveGradeWall.index);
            holder.itemView.getContext().startActivity(intent);
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
    public List<Ekotrope_AboveGradeWall_Table> getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List<Ekotrope_AboveGradeWall_Table> list) {
        currentList = list;
        submitList(list);
    }

    public static class Ekotrope_AboveGradeWallsDiff extends DiffUtil.ItemCallback<Ekotrope_AboveGradeWall_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Ekotrope_AboveGradeWall_Table oldItem, @NonNull Ekotrope_AboveGradeWall_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ekotrope_AboveGradeWall_Table oldItem, @NonNull Ekotrope_AboveGradeWall_Table newItem) {
            return oldItem.index == newItem.index;
        }
    }
}

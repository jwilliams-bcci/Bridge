package com.example.bridge;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import data.DefectCategory;
import data.DefectItem;

public class DefectItemRecyclerAdapter extends RecyclerView.Adapter<DefectItemRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List mInspectDefectList;
    private final LayoutInflater mLayoutInflater;

    public DefectItemRecyclerAdapter(Context context, List inspectDefectList) {
        mContext = context;
        mInspectDefectList = inspectDefectList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_defect_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object listItem = mInspectDefectList.get(position);
        if (listItem instanceof DefectCategory) {
            DefectCategory item = (DefectCategory) listItem;

            holder.mTextSection.setVisibility(View.VISIBLE);
            holder.mTextItemNumber.setVisibility(View.GONE);
            holder.mTextItemDescription.setVisibility(View.GONE);
            holder.mTextSection.setText(item.getCategoryName());
        } else {
            DefectItem item = (DefectItem) listItem;

            holder.mTextSection.setVisibility(View.GONE);
            holder.mTextItemNumber.setVisibility(View.VISIBLE);
            holder.mTextItemDescription.setVisibility(View.VISIBLE);
            holder.mTextItemNumber.setText(Integer.toString(item.getItemNumber()));
            holder.mTextItemDescription.setText(item.getItemDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DefectItemActivity.class);
                    intent.putExtra(DefectItemActivity.DEFECT_ID, item.getDefectItemId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mInspectDefectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mDefectItemId;
        public int mDefectCategoryId;
        public int mItemNumber;
        public String mItemDescription;
        public final TextView mTextSection;
        public final TextView mTextItemNumber;
        public final TextView mTextItemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextItemNumber = (TextView) itemView.findViewById(R.id.item_defect_item_text_number);
            mTextItemDescription = (TextView) itemView.findViewById(R.id.item_defect_item_text_description);
            mTextSection = (TextView) itemView.findViewById(R.id.item_defect_item_text_section);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
        }
    }
}

package com.example.bridge;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bridge.data.Inspection;

import java.util.List;

public class InspectionRecyclerAdapter extends RecyclerView.Adapter<InspectionRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Inspection> mInspectionList;
    private final LayoutInflater mLayoutInflater;

    public InspectionRecyclerAdapter(Context context, List<Inspection> inspectionList) {
        mContext = context;
        mInspectionList = inspectionList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_inspection_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inspection inspection = mInspectionList.get(position);
        holder.mTextCommunity.setText(inspection.getCommunity());
        holder.mTextAddress.setText(inspection.getAddress());
        holder.mTextInspectionType.setText(inspection.getInspectionType());
        holder.mTextInspectionNotes.setText(inspection.getNotes());
    }

    @Override
    public int getItemCount() {
        return mInspectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextCommunity;
        public final TextView mTextAddress;
        public final TextView mTextInspectionType;
        public final TextView mTextInspectionNotes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCommunity = (TextView) itemView.findViewById(R.id.text_recycler_community);
            mTextAddress = (TextView) itemView.findViewById(R.id.text_recycler_address);
            mTextInspectionType = (TextView) itemView.findViewById(R.id.text_recycler_inspection_type);
            mTextInspectionNotes = (TextView) itemView.findViewById(R.id.text_recycler_inspection_notes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}

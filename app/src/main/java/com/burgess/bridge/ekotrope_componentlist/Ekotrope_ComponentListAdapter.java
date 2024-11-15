package com.burgess.bridge.ekotrope_componentlist;

import static com.burgess.bridge.Constants.COMPONENT_DISTRIBUTION_SYSTEM;
import static com.burgess.bridge.Constants.COMPONENT_DUCT;
import static com.burgess.bridge.Constants.COMPONENT_INDEX;
import static com.burgess.bridge.Constants.COMPONENT_MECHANICAL_EQUIPMENT;
import static com.burgess.bridge.Constants.COMPONENT_MECHANICAL_VENTILATION;
import static com.burgess.bridge.Constants.DISTRIBUTION_SYSTEM_INDEX;
import static com.burgess.bridge.Constants.DUCT_INDEX;
import static com.burgess.bridge.Constants.EKOTROPE_PLAN_ID;
import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.MECHANICAL_EQUIPMENT_INDEX;
import static com.burgess.bridge.Constants.MECHANICAL_VENTILATION_INDEX;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_distributionsystem.Ekotrope_DistributionSystemActivity;
import com.burgess.bridge.ekotrope_duct.Ekotrope_DuctActivity;
import com.burgess.bridge.ekotrope_mechanicalequipment.Ekotrope_MechanicalEquipmentActivity;
import com.burgess.bridge.ekotrope_mechanicalventilation.Ekotrope_MechanicalVentilationActivity;

import java.util.List;

import data.Tables.Ekotrope_DistributionSystem_Table;
import data.Tables.Ekotrope_Duct_Table;
import data.Tables.Ekotrope_MechanicalEquipment_Table;
import data.Tables.Ekotrope_MechanicalVentilation_Table;

public class Ekotrope_ComponentListAdapter extends ListAdapter<Object, Ekotrope_ComponentListViewHolder> {
    public static final String TAG = "COMPONENT_LIST";

    private int mInspectionId;
    private String mEkotropeProjectId;
    private String mEkotropePlanId;
    private int mDsId;
    private String mComponentType;
    private List currentList;

    protected Ekotrope_ComponentListAdapter(@NonNull DiffUtil.ItemCallback<Object> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public Ekotrope_ComponentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ekotrope_component, parent, false);
        return new Ekotrope_ComponentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ekotrope_ComponentListViewHolder holder, int position) {
        Object component = currentList.get(position);
        Ekotrope_MechanicalEquipment_Table mechanicalEquipment;
        Ekotrope_DistributionSystem_Table distributionSystem;
        Ekotrope_Duct_Table duct;
        Ekotrope_MechanicalVentilation_Table mechanicalVentilation;
        if (component != null) {
            switch (mComponentType) {
                case COMPONENT_MECHANICAL_EQUIPMENT:
                    assert component instanceof Ekotrope_MechanicalEquipment_Table;
                    mechanicalEquipment = (Ekotrope_MechanicalEquipment_Table) component;
                    holder.setTextIndex(Integer.toString(mechanicalEquipment.index));
                    holder.setTextName(mechanicalEquipment.name);
                    holder.itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(holder.itemView.getContext(), Ekotrope_MechanicalEquipmentActivity.class);
                        intent.putExtra(MECHANICAL_EQUIPMENT_INDEX, mechanicalEquipment.index);
                        intent.putExtra(INSPECTION_ID, mInspectionId);
                        intent.putExtra(EKOTROPE_PROJECT_ID, mEkotropeProjectId);
                        intent.putExtra(EKOTROPE_PLAN_ID, mEkotropePlanId);
                        holder.itemView.getContext().startActivity(intent);
                    });
                    break;
                case COMPONENT_DISTRIBUTION_SYSTEM:
                    assert component instanceof Ekotrope_DistributionSystem_Table;
                    distributionSystem = (Ekotrope_DistributionSystem_Table) component;
                    holder.setTextIndex(Integer.toString(distributionSystem.index));
                    holder.setTextName(distributionSystem.system_type);
                    holder.itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(holder.itemView.getContext(), Ekotrope_DistributionSystemActivity.class);
                        intent.putExtra(DISTRIBUTION_SYSTEM_INDEX, distributionSystem.index);
                        intent.putExtra(INSPECTION_ID, mInspectionId);
                        intent.putExtra(EKOTROPE_PROJECT_ID, mEkotropeProjectId);
                        intent.putExtra(EKOTROPE_PLAN_ID, mEkotropePlanId);
                        holder.itemView.getContext().startActivity(intent);
                    });
                    break;
                case COMPONENT_DUCT:
                    assert component instanceof Ekotrope_Duct_Table;
                    duct = (Ekotrope_Duct_Table) component;
                    holder.setTextIndex(Integer.toString(duct.index));
                    holder.setTextName(duct.location);
                    holder.itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(holder.itemView.getContext(), Ekotrope_DuctActivity.class);
                        intent.putExtra(INSPECTION_ID, mInspectionId);
                        intent.putExtra(EKOTROPE_PROJECT_ID, mEkotropeProjectId);
                        intent.putExtra(EKOTROPE_PLAN_ID, mEkotropePlanId);
                        intent.putExtra(DISTRIBUTION_SYSTEM_INDEX, mDsId);
                        intent.putExtra(DUCT_INDEX, duct.index);
                        holder.itemView.getContext().startActivity(intent);
                    });
                    break;
                case COMPONENT_MECHANICAL_VENTILATION:
                    assert component instanceof Ekotrope_MechanicalVentilation_Table;
                    mechanicalVentilation = (Ekotrope_MechanicalVentilation_Table) component;
                    holder.setTextIndex(Integer.toString(mechanicalVentilation.index));
                    holder.setTextName(mechanicalVentilation.motor_type);
                    holder.itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(holder.itemView.getContext(), Ekotrope_MechanicalVentilationActivity.class);
                        intent.putExtra(INSPECTION_ID, mInspectionId);
                        intent.putExtra(EKOTROPE_PROJECT_ID, mEkotropeProjectId);
                        intent.putExtra(EKOTROPE_PLAN_ID, mEkotropePlanId);
                        intent.putExtra(MECHANICAL_VENTILATION_INDEX, mechanicalVentilation.index);
                        holder.itemView.getContext().startActivity(intent);
                    });
                    break;
            }
        }
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
    public void setEkotropePlanId(String ekotropePlanId) {
        mEkotropePlanId = ekotropePlanId;
    }
    public void setDsId(int dsId) {
        mDsId = dsId;
    }
    public void setComponentType(String componentType) {
        mComponentType = componentType;
    }
    public List getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List list) {
        currentList = list;
        submitList(list);
    }

    public static class Ekotrope_ComponentListDiff extends DiffUtil.ItemCallback<Object> {

        @Override
        public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
            return true;
        }
    }
}

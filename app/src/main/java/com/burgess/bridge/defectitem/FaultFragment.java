package com.burgess.bridge.defectitem;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.burgess.bridge.OnButtonClickListener;
import com.burgess.bridge.R;

import java.util.List;

import data.Tables.Fault_Table;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaultFragment extends DialogFragment implements OnButtonClickListener {
    private static FaultFragment mFragment;
    private View mView;
    private EditText mFaultResultsDialog;
    private List<Fault_Table> faultList;

    public FaultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FaultFragment.
     */
    public static FaultFragment newInstance() {
        mFragment = new FaultFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fault, container, false);
        mFaultResultsDialog = mView.findViewById(R.id.fault_text_result);
        TextView faultResultsDefectItem = getActivity().findViewById(R.id.defect_item_text_fault);

        RecyclerView recyclerFaults = mView.findViewById(R.id.fault_recycler_buttons);
        recyclerFaults.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerFaults.setAdapter(new FaultsRecyclerAdapter(getActivity(), faultList, this));

        Button saveAndExit = mView.findViewById(R.id.fault_button_save_and_exit);
        saveAndExit.setOnClickListener(v -> {
            faultResultsDefectItem.setText(mFaultResultsDialog.getText());
            mFragment.dismiss();
        });

        return mView;
    }

    @Override
    public void onButtonClick(String buttonName) {
        mFaultResultsDialog.append(buttonName + " ");
    }

    public void setFaultList(List<Fault_Table> faultList) {
        this.faultList = faultList;
    }
}
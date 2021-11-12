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

import data.Tables.Direction_Table;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionFragment extends DialogFragment implements OnButtonClickListener {
    private static DirectionFragment mFragment;
    private View mView;
    private EditText mDirectionResultsDialog;
    private List<Direction_Table> directionList;

    public DirectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DirectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectionFragment newInstance() {
        mFragment = new DirectionFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_direction, container, false);
        mDirectionResultsDialog = mView.findViewById(R.id.direction_text_result);
        TextView directionResultsDefectItem = getActivity().findViewById(R.id.defect_item_text_direction);

        RecyclerView recyclerDirections = (RecyclerView) mView.findViewById(R.id.direction_recycler_buttons);
        recyclerDirections.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerDirections.setAdapter(new DirectionsRecyclerAdapter(getActivity(), directionList, this));

        Button saveAndExit = mView.findViewById(R.id.direction_button_save_and_exit);
        saveAndExit.setOnClickListener(v -> {
            directionResultsDefectItem.setText(mDirectionResultsDialog.getText());
            mFragment.dismiss();
        });

        return mView;
    }

    @Override
    public void onButtonClick(String buttonName) {
        mDirectionResultsDialog.append(buttonName + " ");
    }

    public void setDirectionList(List<Direction_Table> directionList) {
        this.directionList = directionList;
    }
}
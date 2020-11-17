package com.example.bridge;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends DialogFragment {
    private static LocationFragment mFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance() {
        mFragment = new LocationFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        setClickListeners(view);

        return view;
    }

    public void setClickListeners(View view) {
        EditText locationResultsDialog = view.findViewById(R.id.location_text_result);
        TextView locationResultsDefectItem = getActivity().findViewById(R.id.defect_item_text_location);

        //region First Row
        Button rearLeftCorner = view.findViewById(R.id.location_button_rear_left_corner);
        rearLeftCorner.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button rear = view.findViewById(R.id.location_button_rear);
        rear.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button rearRightCorner = view.findViewById(R.id.location_button_rear_right_corner);
        rearRightCorner.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        //endregion
        //region Second Row
        Button leftRear = view.findViewById(R.id.location_button_left_rear);
        leftRear.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button rearLeft = view.findViewById(R.id.location_button_rear_left);
        rearLeft.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button rearMiddle = view.findViewById(R.id.location_button_rear_middle);
        rearMiddle.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button rearRight = view.findViewById(R.id.location_button_rear_right);
        rearRight.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button rightRear = view.findViewById(R.id.location_button_right_rear);
        rightRear.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        //endregion
        //region Third Row
        Button leftMiddle = view.findViewById(R.id.location_button_left_middle);
        leftMiddle.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button left = view.findViewById(R.id.location_button_left);
        left.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button middle = view.findViewById(R.id.location_button_middle);
        middle.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button right = view.findViewById(R.id.location_button_right);
        right.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button rightMiddle = view.findViewById(R.id.location_button_right_middle);
        rightMiddle.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        //endregion
        //region Fourth Row
        Button leftFront = view.findViewById(R.id.location_button_left_front);
        leftFront.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button frontLeft = view.findViewById(R.id.location_button_front_left);
        frontLeft.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button frontMiddle = view.findViewById(R.id.location_button_front_middle);
        frontMiddle.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button frontRight = view.findViewById(R.id.location_button_front_right);
        frontRight.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button rightFront = view.findViewById(R.id.location_button_right_front);
        rightFront.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        //endregion
        //region Fifth Row
        Button frontLeftCorner = view.findViewById(R.id.location_button_front_left_corner);
        frontLeftCorner.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button front = view.findViewById(R.id.location_button_front);
        front.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button frontRightCorner = view.findViewById(R.id.location_button_front_right_corner);
        frontRightCorner.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        //endregion
        //region Sixth Row
        Button exterior = view.findViewById(R.id.location_button_exterior);
        exterior.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button basement = view.findViewById(R.id.location_button_basement);
        basement.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button main = view.findViewById(R.id.location_button_main);
        main.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button west = view.findViewById(R.id.location_button_west);
        west.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button north = view.findViewById(R.id.location_button_north);
        north.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        //endregion
        //region Seventh Row
        Button up = view.findViewById(R.id.location_button_up);
        up.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button third = view.findViewById(R.id.location_button_third);
        third.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button fourth = view.findViewById(R.id.location_button_fourth);
        fourth.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button south = view.findViewById(R.id.location_button_south);
        south.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        Button east = view.findViewById(R.id.location_button_east);
        east.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            locationResultsDialog.append(buttonClicked.getText() + " ");
        });
        //endregion

        Button saveAndExit = view.findViewById(R.id.location_button_save_and_exit);
        saveAndExit.setOnClickListener(v -> {
            locationResultsDefectItem.setText(locationResultsDialog.getText());
            mFragment.dismiss();
        });
    }
}
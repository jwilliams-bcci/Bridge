package com.example.bridge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import data.DataManager;
import data.Room;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends DialogFragment {
    private static RoomFragment mFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public RoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance() {
        mFragment = new RoomFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_room, container, false);
        View view = inflater.inflate(R.layout.fragment_room, null);
        GridView gridView = (GridView) view.findViewById(R.id.room_gridview_buttons);

        //RoomGridViewAdapter roomAdapter = new RoomGridViewAdapter(getActivity(), (ArrayList<Room>) DataManager.getInstance().getRooms());
        gridView.setAdapter(new RoomGridViewAdapter(mFragment.getContext(), (List<Room>) DataManager.getInstance().getRooms()));
        //gridView.setAdapter(new ArrayAdapter<Room>(mFragment.getContext(), android.R.layout.simple_list_item_1, (List<Room>) DataManager.getInstance().getRooms()));

        return view;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_room, null);
//        GridView gridView = (GridView) view.findViewById(R.id.room_gridview_buttons);
//        RoomGridViewAdapter roomAdapter = new RoomGridViewAdapter(mFragment.getContext(), (ArrayList<Room>) DataManager.getInstance().getRooms());
//        gridView.setAdapter(roomAdapter);
//
//        builder.setView(view);
//        Dialog dialog = builder.create();
//        return dialog;
//    }
}
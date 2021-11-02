package com.burgess.bridge.defectitem;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.burgess.bridge.CannedCommentRecyclerAdapter;
import com.burgess.bridge.OnButtonClickListener;
import com.burgess.bridge.R;

import java.util.List;

import data.CannedComment;
import data.DataManager;
import data.Tables.CannedComment_Table;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CannedCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CannedCommentFragment extends DialogFragment implements OnButtonClickListener {
    private static CannedCommentFragment mFragment;
    private static List<String> mCannedComments;
    private View mView;
    private EditText mCannedCommentResultsDialog;

    public CannedCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CannedCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CannedCommentFragment newInstance(List<String> cannedCommentList) {
        mFragment = new CannedCommentFragment();
        mCannedComments = cannedCommentList;
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_canned_comment, container, false);
        List<CannedComment> cannedComments = DataManager.getInstance().getCannedComments();
        mCannedCommentResultsDialog = mView.findViewById(R.id.canned_comment_text_result);
        TextView cannedCommentResultsCannedComment = getActivity().findViewById(R.id.defect_item_text_canned_comment);

        RecyclerView recyclerCannedComments = (RecyclerView) mView.findViewById(R.id.canned_comment_recycler_buttons);
        recyclerCannedComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCannedComments.setAdapter(new CannedCommentRecyclerAdapter(getActivity(), mCannedComments, this));

        Button saveAndExit = mView.findViewById(R.id.canned_comment_button_save_and_exit);
        saveAndExit.setOnClickListener(v -> {
            cannedCommentResultsCannedComment.setText(mCannedCommentResultsDialog.getText());
            mFragment.dismiss();
        });

        return mView;
    }

    @Override
    public void onButtonClick(String buttonName) {
        mCannedCommentResultsDialog.append(buttonName + " ");
    }
}
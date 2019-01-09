package com.code_dream.almanach.middle_drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.models.GenericListItem;
import com.code_dream.almanach.models.GenericListTitleHeader;
import com.code_dream.almanach.notes.note_list.NoteListActivity;
import com.code_dream.almanach.subject_list.SubjectListActivity;
import com.code_dream.almanach.timetable.TimetableActivity;
import com.code_dream.almanach.utility.Registry;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMiddleDrawer extends Fragment implements MiddleDrawerContract.View,
															  RecyclerViewAdapter.ItemClickListener {

	@BindView(R.id.subject_fragment_recycler_view) RecyclerView recyclerView;

	RecyclerViewAdapter adapter;

	ArrayList<Object> recyclerViewContentList = new ArrayList<>();

	private MiddleDrawerPresenter presenter;

	public FragmentMiddleDrawer() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_middle_drawer, container, false);
		ButterKnife.bind(this, view);

		presenter = new MiddleDrawerPresenter(this);

		return view;
	}

    @Override
    public void onResume() {
        if (presenter == null)
            presenter = new MiddleDrawerPresenter(this);
	    super.onResume();
    }

    @Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		presenter.populateItemsList();

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		adapter = new RecyclerViewAdapter(recyclerViewContentList);
		adapter.setItemClickListener(this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void addCategoryToList(GenericListTitleHeader genericListTitleHeader, GenericListItem[] genericListItems){
		recyclerViewContentList.add(genericListTitleHeader);
		recyclerViewContentList.addAll(Arrays.asList(genericListItems));
	}

	@Override
	public void onRecyclerViewItemClick(View view, Class<?> viewHolder, int position) {
		if (position == Registry.MIDDLE_DRAWER_MY_TIMETABLE_POSITION)
			startActivity(new Intent(getActivity(), TimetableActivity.class));
		else if (position == Registry.MIDDLE_DRAWER_MY_SUBJECTS_POSITION)
			startActivity(new Intent(getActivity(), SubjectListActivity.class));
		else if (position == Registry.MIDDLE_DRAWER_NOTES_POSITION)
			startActivity(new Intent(getActivity(), NoteListActivity.class));
	}
}

package com.code_dream.almanach.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.models.PersonSearchResult;
import com.code_dream.almanach.models.School;
import com.code_dream.almanach.person_profile.PersonProfileActivity;
import com.code_dream.almanach.school_profile.SchoolProfileActivity;
import com.code_dream.almanach.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchableActivity extends BaseActivity implements SearchableContract.View,
                                                                SearchView.OnQueryTextListener,
                                                                RecyclerViewAdapter.ItemClickListener {

    @BindView(R.id.search_activity_toolbar) Toolbar toolbar;
    @BindView(R.id.search_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.activity_searchable_searchView) SearchView searchView;
    @BindView(R.id.progress_bar_search) ProgressBar progressBar;

    RecyclerViewAdapter adapter;

    ArrayList<Object> searchResultArrayList = new ArrayList<>();

    SearchablePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchable);
        setupView();
    }

    @Override
    public void setupPresenter() {
        presenter = new SearchablePresenter(this);
    }

    @Override
    public void setupView(){
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new RecyclerViewAdapter(searchResultArrayList);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void updateRecyclerView(final ArrayList<Object> searchResults, final String searchText) {
        searchResultArrayList.clear();
        searchResultArrayList.addAll(searchResults);

        adapter.notifyDataSetChanged();

        applySearchTextBolding(searchText);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText != null)
            presenter.onRefreshItems(newText.trim());

        return true;
    }

    private void applySearchTextBolding(String textToBold){
        adapter.boldSearchText(Utility.deAccentString(textToBold).toLowerCase());
    }

    @Override
    public void onRecyclerViewItemClick(View view, Class<?> viewHolder, int position) {
        presenter.onRecyclerViewItemClick(position, viewHolder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.requestFocus();
    }

    @Override
    public void setRecyclerViewVisibility(final int visibility) {
        recyclerView.setVisibility(visibility);
    }

    @Override
    public void setProgressBarVisibility(final int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void clearSearchView() {
        searchView.clearFocus();
        searchView.setQuery("", false);
    }

    @Override
    public void startSchoolProfileActivity(int position) {
        Intent schoolProfile = new Intent(this, SchoolProfileActivity.class);
        schoolProfile.putExtra("school", ((School) searchResultArrayList.get(position)).getName());
        startActivity(schoolProfile);
    }

    @Override
    public void startPersonProfileActivity(int position) {
        Intent personProfile = new Intent(this, PersonProfileActivity.class);
        personProfile.putExtra("personSearchResult", (PersonSearchResult) searchResultArrayList.get(position));
        startActivity(personProfile);
    }
}

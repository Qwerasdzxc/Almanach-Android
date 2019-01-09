package com.code_dream.almanach.search;

import android.util.Log;
import android.view.View;

import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.models.SearchResult;
import com.code_dream.almanach.search.network.OnSearchListener;
import com.code_dream.almanach.search.network.SearchableInteractor;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public class SearchablePresenter implements SearchableContract.UserActionsListener, OnSearchListener {

    private SearchableContract.View view;
    private SearchableInteractor interactor;

    private String searchText;

    public SearchablePresenter(SearchableContract.View view){
        this.view = view;
        interactor = new SearchableInteractor();
    }

    @Override
    public void onRefreshItems(String searchText) {
        view.setRecyclerViewVisibility(View.GONE);
        view.setProgressBarVisibility(View.VISIBLE);

        this.searchText = searchText;

        interactor.refreshSchools(this, searchText);
    }

    @Override
    public void onRecyclerViewItemClick(int position, Class<?> viewHolder) {
        // Clear the Action bar search field
        view.clearSearchView();

        Log.e("onClick", "viewHolder: " + viewHolder.getName());

        if (viewHolder == RecyclerViewAdapter.SchoolViewHolder.class)
            view.startSchoolProfileActivity(position);
        else
            view.startPersonProfileActivity(position);
    }

    @Override
    public void onSearchResultsSuccessfullyLoaded(SearchResult searchResults) {
        view.setRecyclerViewVisibility(View.VISIBLE);
        view.setProgressBarVisibility(View.GONE);

        // We got our new search results so we display them in the RecyclerView.
        view.updateRecyclerView(searchResults.getAllSearchResults(), searchText);
    }

    @Override
    public void onNoSearchResults() {
        view.setRecyclerViewVisibility(View.VISIBLE);
        view.setProgressBarVisibility(View.GONE);
    }
}

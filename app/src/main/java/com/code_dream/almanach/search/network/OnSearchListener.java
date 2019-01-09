package com.code_dream.almanach.search.network;

import com.code_dream.almanach.models.SearchResult;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface OnSearchListener {

    void onSearchResultsSuccessfullyLoaded(SearchResult searchResults);

    void onNoSearchResults();
}

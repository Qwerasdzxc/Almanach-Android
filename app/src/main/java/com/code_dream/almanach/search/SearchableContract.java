package com.code_dream.almanach.search;

import android.support.annotation.UiThread;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface SearchableContract {

    @UiThread
    interface View {

        void updateRecyclerView(ArrayList<Object> searchResults, String searchText);

        void setRecyclerViewVisibility(int visibility);

        void setProgressBarVisibility(int visibility);

        void clearSearchView();

        void startSchoolProfileActivity(int position);

        void startPersonProfileActivity(int position);
    }

    interface UserActionsListener {

        void onRefreshItems(String searchText);

        void onRecyclerViewItemClick(int position, Class<?> viewHolder);
    }
}

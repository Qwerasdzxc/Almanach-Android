package com.code_dream.almanach.search.network;

/**
 * Created by Qwerasdzxc on 2/1/17.
 */

public interface ISearchableInteractor {
    void refreshSchools(OnSearchListener listener, String searchText);

    //Single<List<School>> refreshSchoolsReactively(String searchText);
}

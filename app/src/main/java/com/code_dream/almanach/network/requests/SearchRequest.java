package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public class SearchRequest {

    private String searchText;

    public SearchRequest(String searchText){
        this.searchText = searchText;
    }

    public String getSearchText(){
        return searchText;
    }
}

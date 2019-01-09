package com.code_dream.almanach.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 9/19/17.
 */

public class SearchResult {

    @SerializedName("person_list")
    @Expose
    private ArrayList<PersonSearchResult> people;

    @SerializedName("school_list")
    @Expose
    private ArrayList<School> schools;

    public SearchResult(ArrayList<PersonSearchResult> people, ArrayList<School> schools) {
        this.people = people;
        this.schools = schools;
    }

    public ArrayList<PersonSearchResult> getPeople() {
        return people;
    }

    public ArrayList<School> getSchools() {
        return schools;
    }

    public ArrayList<Object> getAllSearchResults() {
        ArrayList<Object> allSearchResults = new ArrayList<>();
        allSearchResults.addAll(people);
        allSearchResults.addAll(schools);

        return allSearchResults;
    }
}

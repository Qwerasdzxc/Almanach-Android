package com.code_dream.almanach.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 6/7/17.
 */

public class Subject {

    @SerializedName("subject_name")
    @Expose
    private String name;

    @SerializedName("subject_grades")
    @Expose
    private ArrayList<Integer> grades = new ArrayList<>();

    public Subject() { }

    public Subject(String name, ArrayList<Integer> grades) {
        this.name = name;
        this.grades = grades;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Integer> grades) {
        this.grades = grades;
    }
}

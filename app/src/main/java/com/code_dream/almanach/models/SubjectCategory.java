package com.code_dream.almanach.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Qwerasdzxc on 6/21/17.
 */

public class SubjectCategory extends ExpandableGroup<SubjectCategoryItem> {

    public SubjectCategory(String title, List<SubjectCategoryItem> items){
        super(title, items);
    }
}

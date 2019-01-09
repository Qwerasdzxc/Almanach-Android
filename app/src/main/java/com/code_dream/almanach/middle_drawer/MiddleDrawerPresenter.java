package com.code_dream.almanach.middle_drawer;

import com.code_dream.almanach.models.GenericListItem;
import com.code_dream.almanach.models.GenericListTitleHeader;
import com.code_dream.almanach.utility.Registry;

/**
 * Created by Qwerasdzxc on 2/5/17.
 */

public class MiddleDrawerPresenter implements MiddleDrawerContract.UserActionsListener {

    private MiddleDrawerContract.View view;

    public MiddleDrawerPresenter(MiddleDrawerContract.View view){
        this.view = view;
    }

    @Override
    public void populateItemsList(){
        for (ListCategory listCategory : ListCategory.values())
            addCategoryToList(listCategory);
    }

    private void addCategoryToList(ListCategory listCategory) {
        String categoryTitle;
        String[] categoryItems;
        int[] categoryImages;
        GenericListItem[] genericListItems;

        switch (listCategory){
            case Timetable:
                categoryTitle = Registry.MIDDLE_DRAWER_TIMETABLE_HEADER_TITLE;
                categoryItems = Registry.MIDDLE_DRAWER_TIMETABLE_ITEMS;
                categoryImages = Registry.MIDDLE_DRAWER_TIMETABLE_IMAGES;
                break;
            case Subjects:
                categoryTitle = Registry.MIDDLE_DRAWER_SUBJECTS_HEADER_TITLE;
                categoryItems = Registry.MIDDLE_DRAWER_SUBJECTS_ITEMS;
                categoryImages = Registry.MIDDLE_DRAWER_SUBJECTS_IMAGES;
                break;
            default:
                return;
        }

        genericListItems = new GenericListItem[categoryItems.length];

        for (int i = 0; i < categoryItems.length; i++)
            genericListItems[i] = new GenericListItem(categoryItems[i], categoryImages[i]);

        view.addCategoryToList(new GenericListTitleHeader(categoryTitle), genericListItems);
    }
}

package com.code_dream.almanach.middle_drawer;

import android.support.annotation.UiThread;

import com.code_dream.almanach.models.GenericListItem;
import com.code_dream.almanach.models.GenericListTitleHeader;

/**
 * Created by Qwerasdzxc on 2/5/17.
 */

public interface MiddleDrawerContract {

    @UiThread
    interface View {

        void addCategoryToList(GenericListTitleHeader genericListTitleHeader, GenericListItem[] genericListItems);
    }

    interface UserActionsListener {

        void populateItemsList();
    }
}

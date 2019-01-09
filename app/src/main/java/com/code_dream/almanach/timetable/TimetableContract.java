package com.code_dream.almanach.timetable;

import android.support.annotation.UiThread;

import com.code_dream.almanach.models.GenericListItem;
import com.code_dream.almanach.models.TimetableItem;
import com.code_dream.almanach.timetable.models.TimetableListItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 5/22/17.
 */

public interface TimetableContract {

    @UiThread
    interface View {

        void updateAllItems(GenericListItem[] subjectListItems);

        void onItemsLoaded();

        void displayItemsToAdd(ArrayList<String> itemsToAdd);

        void addItem(TimetableListItem subjectListItem);

        void displayProgressBar(boolean display);

        void displayEditButton(boolean display);

        void displaySnackbarFailure();

        void switchToEditMode();

        void switchToViewMode();

        void callBackButton();

        void addAddNewItemItemToRecyclerView();

        void removeAddNewItemItemFromRecyclerView();

        TimetableItem.Day getSelectedTimetableDay();

        ArrayList<TimetableListItem> getRecyclerViewItems();

        void showLoadingToast();

        void dismissLoadingToastSuccessfully();

        void dismissLoadingToastWithError();

        void setDraggingEnabled(boolean enabled);

        void showTimetableCancelConfirmationDialog();

        void showIntro();

        boolean didShowIntro();
    }

    interface UserActionsListener {

        void loadTimetable();

        void onSyncClick();

        void onDayChanged();

        void showItems();

        void getItemsToAdd();

        void addItemToTimetable(String name, TimetableItem.Day day, int order);

        void onItemRemoved(TimetableListItem removedItem);

        boolean isInEditMode();

        void onCancelTimetableChangesClick();

        void onBackPressed();

        void cancelTimetableChanges();

        void changeTimetableMode();
    }
}

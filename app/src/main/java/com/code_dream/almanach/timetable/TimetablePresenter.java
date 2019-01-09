package com.code_dream.almanach.timetable;

import com.code_dream.almanach.R;
import com.code_dream.almanach.models.GenericListItem;
import com.code_dream.almanach.models.TimetableItem;
import com.code_dream.almanach.timetable.models.TimetableListItem;
import com.code_dream.almanach.timetable.network.OnTimetableRequestFinished;
import com.code_dream.almanach.timetable.network.TimetableInteractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by Qwerasdzxc on 5/22/17.
 */

public class TimetablePresenter implements TimetableContract.UserActionsListener, OnTimetableRequestFinished {

    public enum TimetableMode { VIEW, EDIT }

    private TimetableContract.View view;
    private TimetableInteractor interactor;

    // Map which stores all Timetable items for appropriate day.
    private HashMap<TimetableItem.Day, ArrayList<TimetableItem>> timetableItemMap = new HashMap<>();

    // List for storing individual subjects.
    private ArrayList<TimetableItem> allSubjectItems = new ArrayList<>();

    // Lists that are used for backup when the user cancels changes made to the Timetable.
    private ArrayList<TimetableItem> addedItems = new ArrayList<>();
    private ArrayList<TimetableItem> removedItems = new ArrayList<>();

    // There are 2 working modes: EDIT and VIEW, which can be changed by the user.
    private TimetableMode timetableMode = TimetableMode.VIEW;

    private boolean timetableLoading = true;

    public TimetablePresenter(TimetableContract.View view) {
        this.view = view;
        interactor = new TimetableInteractor();
    }

    @Override
    public void loadTimetable() {
        view.displayProgressBar(true);
        interactor.loadItems(this);
    }

    @Override
    public void changeTimetableMode() {
        // Prevent switching the Timetable mode if it hasn't finished loading yet
        if (timetableLoading)
            return;

        // Changing mode to VIEW.
        if (timetableMode == TimetableMode.EDIT) {
            // Updating new Item positions.
            this.updateNewTimetableItemsPositionsFromRecyclerView(view.getRecyclerViewItems());
            // Display a loading toast.
            view.showLoadingToast();
            // Upload the whole Timetable to the server.
            interactor.uploadItems(appendAllTimetableItems(), this);
        }
        // Changing mode to EDIT.
        else {
            timetableMode = TimetableMode.EDIT;

            // Save the original Item positions for backup purposes.
            this.saveOriginalTimetableItemsPositions();

            // Enable Item dragging and re-arranging. Add the 'AddNewItem' item as the last element of the RecyclerView.
            view.setDraggingEnabled(true);
            view.addAddNewItemItemToRecyclerView();
            view.switchToEditMode();
            this.showItems();
        }
    }

    @Override
    public void onSyncClick() {
        this.loadTimetable();
    }

    @Override
    public boolean isInEditMode() {
        return timetableMode == TimetableMode.EDIT;
    }

    @Override
    public void onCancelTimetableChangesClick() {
        view.showTimetableCancelConfirmationDialog();
    }

    @Override
    public void onBackPressed() {
        if (isInEditMode())
            view.showTimetableCancelConfirmationDialog();
        else
            view.callBackButton();
    }

    @Override
    public void cancelTimetableChanges() {
        // Cancel all changes made to the Timetable and switch to VIEW mode.
        if (timetableMode == TimetableMode.EDIT) {
            timetableMode = TimetableMode.VIEW;

            view.removeAddNewItemItemFromRecyclerView();
            this.revertToOriginalTimetableItemsPositions();

            view.switchToViewMode();
            this.showItems();
        }
        // We are already in VIEW mode so we call the original BackButton request.
        else
            view.callBackButton();
    }

    @Override
    public void onDayChanged() {
        // Prevent the Timetable population since it hasn't been loaded yet.
        if (timetableLoading)
            return;

        if (isInEditMode()) {
            // Update new Item positions for the previous day.
            this.updateNewTimetableItemsPositionsFromRecyclerView(view.getRecyclerViewItems());
        }

        this.showItems();
    }

    @Override
    public void showItems() {
        TimetableItem.Day dayToShow = view.getSelectedTimetableDay();
        GenericListItem[] timetableItems = new GenericListItem[timetableItemMap.get(dayToShow).size()];

        // Populate the Timetable items by the order which they have been saved.
        for (int i = 0; i < timetableItems.length; i++) {
            for (TimetableItem item : timetableItemMap.get(dayToShow)) {
                if (item.getOrder() == i) {
                    if (timetableMode == TimetableMode.EDIT)
                        timetableItems[i] = new TimetableListItem(item.getName(), new Random().nextInt(2) == 0 ? R.drawable.ic_subject_1 : R.drawable.ic_subject_2, item);
                    else if (timetableMode == TimetableMode.VIEW) {
                        timetableItems[i] = new GenericListItem(item.getName(), new Random().nextInt(2) == 0 ? R.drawable.ic_subject_1 : R.drawable.ic_subject_2, true);
                    }
                }
            }
        }

        // Pass the newly created Array to the RecyclerView.
        view.updateAllItems(timetableItems);

        // In case we're in EDIT mode, we need to add the 'AddNewItem' item as the last element of the RecyclerView.
        if (timetableMode == TimetableMode.EDIT)
            view.addAddNewItemItemToRecyclerView();
    }

    @Override
    public void getItemsToAdd() {
        ArrayList<String> subjectNames = new ArrayList<>();

        for (TimetableItem timetableItem : allSubjectItems)
                subjectNames.add(timetableItem.getName());

        view.displayItemsToAdd(subjectNames);
    }

    @Override
    public void addItemToTimetable(String name, TimetableItem.Day day, int order) {
        TimetableItem newTimetableItem = new TimetableItem(name, day, order);
        ArrayList<TimetableItem> timetableItems = timetableItemMap.get(newTimetableItem.getDay());

        timetableItems.add(newTimetableItem);
        timetableItemMap.put(newTimetableItem.getDay(), timetableItems);

        // Storing the added item in List so that we can remove it
        // if the user chooses to cancel the Timetable modification.
        addedItems.add(newTimetableItem);

        view.addItem(new TimetableListItem(name, new Random().nextInt(2) == 0 ? R.drawable.ic_subject_1 : R.drawable.ic_subject_2, newTimetableItem));
    }

    @Override
    public void onItemsSuccessfullyLoaded(ArrayList<TimetableItem> loadedTimetableItems, boolean loadedFromServer) {
        // TODO: Use Java 8 Stream API to filter out elements easier.
        ArrayList<TimetableItem> mondayTimetableItems = new ArrayList<>();
        ArrayList<TimetableItem> tuesdayTimetableItems = new ArrayList<>();
        ArrayList<TimetableItem> wednesdayTimetableItems = new ArrayList<>();
        ArrayList<TimetableItem> thursdayTimetableItems = new ArrayList<>();
        ArrayList<TimetableItem> fridayTimetableItems = new ArrayList<>();
        ArrayList<TimetableItem> saturdayTimetableItems = new ArrayList<>();
        ArrayList<TimetableItem> sundayTimetableItems = new ArrayList<>();

        for (TimetableItem timetableItem : loadedTimetableItems) {
            switch (timetableItem.getDay()){
                case MONDAY:
                    mondayTimetableItems.add(timetableItem);
                    break;
                case TUESDAY:
                    tuesdayTimetableItems.add(timetableItem);
                    break;
                case WEDNESDAY:
                    wednesdayTimetableItems.add(timetableItem);
                    break;
                case THURSDAY:
                    thursdayTimetableItems.add(timetableItem);
                    break;
                case FRIDAY:
                    fridayTimetableItems.add(timetableItem);
                    break;
                case SATURDAY:
                    saturdayTimetableItems.add(timetableItem);
                    break;
                case SUNDAY:
                    sundayTimetableItems.add(timetableItem);
                    break;
                case ALL_SUBJECTS:
                    allSubjectItems.add(timetableItem);
                    break;
            }
        }

        timetableItemMap.put(TimetableItem.Day.MONDAY, mondayTimetableItems);
        timetableItemMap.put(TimetableItem.Day.TUESDAY, tuesdayTimetableItems);
        timetableItemMap.put(TimetableItem.Day.WEDNESDAY, wednesdayTimetableItems);
        timetableItemMap.put(TimetableItem.Day.THURSDAY, thursdayTimetableItems);
        timetableItemMap.put(TimetableItem.Day.FRIDAY, fridayTimetableItems);
        timetableItemMap.put(TimetableItem.Day.SATURDAY, saturdayTimetableItems);
        timetableItemMap.put(TimetableItem.Day.SUNDAY, sundayTimetableItems);

        view.onItemsLoaded();
        view.displayProgressBar(false);

        // Show Edit or Sync button depending on the way of data retrieval.
        view.displayEditButton(loadedFromServer);

        // Timetable items loading has finished.
        timetableLoading = false;


        // If this is the first time the Timetable's been entered, show the tutorial.
        if (!view.didShowIntro())
            view.showIntro();
    }

    @Override
    public void onTimetableSuccessfullyUploaded() {
        timetableMode = TimetableMode.VIEW;

        this.showItems();

        view.setDraggingEnabled(false);
        view.dismissLoadingToastSuccessfully();
        view.switchToViewMode();
    }

    @Override
    public void onItemRemoved(TimetableListItem removedItem) {
        TimetableItem itemToRemove = removedItem.getTimetableItem();

        ArrayList<TimetableItem> timetableItems = timetableItemMap.get(itemToRemove.getDay());
        timetableItems.remove(itemToRemove);
        timetableItemMap.put(itemToRemove.getDay(), timetableItems);

        // Storing the removed item in List so that we can add it back if the user chooses
        // to cancel the Timetable modification. Only save items that haven't been added previously.
        if (!addedItems.contains(removedItem.getTimetableItem()))
            removedItems.add(itemToRemove);
    }

    @Override
    public void onFailure() {
        view.displayProgressBar(false);
        view.displaySnackbarFailure();
        view.dismissLoadingToastWithError();
    }

    // Append all Timetable items in one List so that we can send it to the server all at once.
    private ArrayList<TimetableItem> appendAllTimetableItems(){
        ArrayList<TimetableItem> allTimetableItems = new ArrayList<>();

        for (Map.Entry<TimetableItem.Day, ArrayList<TimetableItem>> entry : timetableItemMap.entrySet()){
            if (entry.getKey() != TimetableItem.Day.ALL_SUBJECTS)
                allTimetableItems.addAll(entry.getValue());
        }

        return allTimetableItems;
    }

    // Updating the Item's order according to it's real position in the RecyclerView.
    private void updateNewTimetableItemsPositionsFromRecyclerView(ArrayList<TimetableListItem> items) {
        for (int i = 0; i < items.size() - 1; i++)
            items.get(i).getTimetableItem().setOrder(i);
    }

    // Saving the original (server side stored) item positions. Used as backup in case the user decides to cancel all made changes.
    private void saveOriginalTimetableItemsPositions(){
        for (Map.Entry<TimetableItem.Day, ArrayList<TimetableItem>> entry : timetableItemMap.entrySet()){
            ArrayList<TimetableItem> currentTimetableItems = entry.getValue();

            for (TimetableItem item : currentTimetableItems)
                item.setOriginalOrder(item.getOrder());
        }
    }

    // Reverting the item positions from the backup.
    private void revertToOriginalTimetableItemsPositions(){
        for (Map.Entry<TimetableItem.Day, ArrayList<TimetableItem>> entry : timetableItemMap.entrySet()){
            ArrayList<TimetableItem> currentTimetableItems = entry.getValue();
            ArrayList<TimetableItem> itemsToAdd = new ArrayList<>();
            ArrayList<TimetableItem> itemsToRemove = new ArrayList<>();

            Iterator<TimetableItem> iterator = currentTimetableItems.iterator();

            while (iterator.hasNext()){
                TimetableItem currentItem = iterator.next();
                currentItem.setOrder(currentItem.getOriginalOrder());

                // Current HashMap object is in our Added list so we remove it from the map.
                for (TimetableItem addedItem : addedItems) {
                    if (currentItem == addedItem)
                        iterator.remove();
                }
            }

            // Find the item that was removed and add it to the appropriate day in the map.
            for (TimetableItem removedItem : removedItems) {
                if ((removedItem.getDay() == entry.getKey()) && (!itemsToRemove.contains(removedItem))) {
                    itemsToRemove.add(removedItem);
                    itemsToAdd.add(removedItem);
                }
            }

            currentTimetableItems.addAll(itemsToAdd);
        }

        // Clearing the Lists for later usage.
        addedItems.clear();
        removedItems.clear();
    }
}

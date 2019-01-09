package com.code_dream.almanach.models;

/**
 * Created by Qwerasdzxc on 2/28/17.
 */

public class GenericListItem {

    private String itemText;

    private int imageResource;
    private boolean showOrder;

    public GenericListItem(String itemText, int imageResource) {
        this.itemText = itemText;
        this.imageResource = imageResource;
    }

    public GenericListItem(String itemText, int imageResource, boolean showOrder) {
        this.itemText = itemText;
        this.imageResource = imageResource;
        this.showOrder = showOrder;
    }

    public String getItemText() {
        return itemText;
    }

    public int getImageResource() {
        return imageResource;
    }

    public boolean showOrder() {
        return showOrder;
    }
}

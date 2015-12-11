package net.lion.okvel.financemanager.util;

import net.lion.okvel.financemanager.entity.RecyclerViewItem;

import java.util.Comparator;

public class DateComparator implements Comparator<RecyclerViewItem> {
    @Override
    public int compare(RecyclerViewItem firstItem, RecyclerViewItem secondItem) {
        return secondItem.getDate().compareTo(firstItem.getDate());
    }
}

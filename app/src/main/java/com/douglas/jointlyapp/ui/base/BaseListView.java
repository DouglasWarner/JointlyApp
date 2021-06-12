package com.douglas.jointlyapp.ui.base;

import java.util.List;

/**
 * Interface base to set the common function
 * @param <T>
 */
public interface BaseListView<T> {
    void onSuccess(List<T> list);
}

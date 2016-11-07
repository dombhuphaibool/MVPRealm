package com.bandonleon.mvprealm.model;

import android.support.annotation.NonNull;

/**
 * Created by dom on 11/5/16.
 */

public interface People extends MutableModel {
    int getCount();
    Person get(int index);
    void add(@NonNull Person person);
    void remove(int index);

    void onAddError(String message);
    void onRemoveError(String message);
}

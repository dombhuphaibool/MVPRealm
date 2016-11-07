package com.bandonleon.mvprealm.model;

import android.support.annotation.NonNull;

/**
 * Created by dom on 11/5/16.
 */

public interface MutableModel {

    interface ChangeListener {
        void onModelChanged(boolean success);
    }

    void addModelChangeListener(@NonNull ChangeListener listener);
    void removeModelChangeListener(@NonNull ChangeListener listener);
}

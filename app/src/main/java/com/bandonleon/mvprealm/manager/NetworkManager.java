package com.bandonleon.mvprealm.manager;

import android.support.annotation.NonNull;

import com.bandonleon.mvprealm.model.Person;

/**
 * Created by dom on 11/6/16.
 */

public interface NetworkManager {

    interface NextPersonResponseHandler {
        void onSuccess(@NonNull Person nextPerson);
        void onFailure(String message);
    }

    interface RemovePersonResponseHandler {
        void onSuccess();
        void onFailure(String message);
    }

    void getNextPerson(int index, @NonNull NextPersonResponseHandler responseHandler);
    void removePerson(int index, @NonNull RemovePersonResponseHandler responseHandler);
}

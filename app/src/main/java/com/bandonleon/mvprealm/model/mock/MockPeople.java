package com.bandonleon.mvprealm.model.mock;

import android.support.annotation.NonNull;

import com.bandonleon.mvprealm.model.People;
import com.bandonleon.mvprealm.model.Person;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by dom on 11/4/16.
 */

public class MockPeople implements People {

    List<Person> mPeople;
    Set<ChangeListener> mListeners;

    public MockPeople() {
        mPeople = new LinkedList<>();
        mListeners = new HashSet<>();
    }

    @Override
    public int getCount() {
        return mPeople.size();
    }

    @Override
    public Person get(int index) {
        return mPeople.get(index);
    }

    @Override
    public void add(@NonNull Person person) {
        mPeople.add(person);
        notifyModelChanged();
    }

    @Override
    public void remove(int index) {
        mPeople.remove(index);
        notifyModelChanged();
    }

    @Override
    public void addModelChangeListener(@NonNull ChangeListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeModelChangeListener(@NonNull ChangeListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onAddError(String message) {
        // Nothing to do
    }

    @Override
    public void onRemoveError(String message) {
        // Nothing to do
    }

    private void notifyModelChanged() {
        for (ChangeListener listener : mListeners) {
            listener.onModelChanged(true);
        }
    }
}

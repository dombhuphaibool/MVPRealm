package com.bandonleon.mvprealm.model.realm;

import android.support.annotation.NonNull;

import com.bandonleon.mvprealm.model.People;
import com.bandonleon.mvprealm.model.Person;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dom on 11/5/16.
 */

public class RealmPeople implements People {

    private RealmResults<RealmPerson> mPeople;
    private Set<ChangeListener> mListeners;

    public RealmPeople() {
        mListeners = new HashSet<>();
        init();
    }

    private void init() {
        Realm realm = Realm.getDefaultInstance();
        mPeople = realm.where(RealmPerson.class).findAll();
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
        if (!(person instanceof RealmPerson)) {
            throw new IllegalStateException("person must be an instance of RealmPerson");
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm((RealmPerson) person);
        realm.commitTransaction();
    }

    @Override
    public void remove(int index) {
        if (index >= 0 && index < mPeople.size()) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            mPeople.deleteFromRealm(index);
            realm.commitTransaction();
        }
    }

    @Override
    public void addModelChangeListener(@NonNull final ChangeListener listener) {
        mListeners.add(listener);
        mPeople.addChangeListener(new RealmChangeListener<RealmResults<RealmPerson>>() {
            @Override
            public void onChange(RealmResults<RealmPerson> element) {
                listener.onModelChanged(true);
            }
        });
    }

    @Override
    public void removeModelChangeListener(@NonNull ChangeListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void onAddError(String message) {
        for (ChangeListener listener : mListeners) {
            listener.onModelChanged(false);
        }
    }

    @Override
    public void onRemoveError(String message) {
        for (ChangeListener listener : mListeners) {
            listener.onModelChanged(false);
        }
    }
}

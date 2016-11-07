package com.bandonleon.mvprealm.presenter;

import android.support.annotation.NonNull;

import com.bandonleon.mvprealm.manager.DataManager;
import com.bandonleon.mvprealm.model.MutableModel;
import com.bandonleon.mvprealm.model.People;
import com.bandonleon.mvprealm.model.Person;
import com.bandonleon.mvprealm.view.PeopleView;
import com.bandonleon.mvprealm.view.PersonView;

/**
 * Created by dom on 11/5/16.
 */

public class PeoplePresenter implements MutableModel.ChangeListener {

    private DataManager mDataManager;
    private People mPeople;
    private int mLastPeopleCount;

    private PeopleView mPeopleView;

    public PeoplePresenter(DataManager dataManager) {
        mDataManager = dataManager;
        mPeople = mDataManager.getPeople();
        mPeople.addModelChangeListener(this);
        mLastPeopleCount = mPeople.getCount();
    }

    public void bindView(@NonNull PeopleView peopleView) {
        mPeopleView = peopleView;
    }

    public void unbindView() {
        mPeopleView = null;
    }

    public void release() {
        mPeople.removeModelChangeListener(this);
    }

    public int getPeopleCount() {
        return mPeople.getCount();
    }

    public void bindPersonView(@NonNull PersonView personView, int position) {
        Person person = mPeople.get(position);
        if (person != null) {
            personView.setFirstName(person.getFirstName());
            personView.setLastName(person.getLastName());
            personView.setAge(String.valueOf(person.getAge()));
            personView.setPhoneNumber(person.getPhoneNumber());
        }
    }

    public void addPerson() {
        mDataManager.addPerson();
    }

    public void removePerson() {
        mDataManager.removePerson();
    }

    @Override
    public void onModelChanged(boolean success) {
        if (mPeopleView == null) {
            return;
        }

        if (success) {
            notifyViewOnModelChanged();
        } else {
            mPeopleView.onError();
        }
    }

    private void notifyViewOnModelChanged() {
        int newPeopleCount = mPeople.getCount();
        if (newPeopleCount > mLastPeopleCount) {
            mPeopleView.onPersonAdded();
        } else if (newPeopleCount < mLastPeopleCount) {
            mPeopleView.onPersonRemoved();
        } else {
            mPeopleView.onPersonModified();
        }
        mLastPeopleCount = newPeopleCount;
    }
}

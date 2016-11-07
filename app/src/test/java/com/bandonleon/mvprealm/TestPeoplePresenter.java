package com.bandonleon.mvprealm;

import com.bandonleon.mvprealm.manager.DataManager;
import com.bandonleon.mvprealm.manager.TestDataManager;
import com.bandonleon.mvprealm.presenter.PeoplePresenter;
import com.bandonleon.mvprealm.view.PeopleView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Unit test for PeoplePresenter
 */
public class TestPeoplePresenter {

    private DataManager mDataManager;
    private PeoplePresenter mPresenter;

    @Before
    public void setUp() {
        mDataManager = new TestDataManager();
        mPresenter = new PeoplePresenter(mDataManager);
        PeopleView mockView = mock(PeopleView.class);
        mPresenter.bindView(mockView);
    }

    @After
    public void tearDown() {
        mPresenter.unbindView();
        mPresenter.release();
    }

    @Test
    public void testPresenter() throws Exception {
        int peopleCount = mPresenter.getPeopleCount();
        assertEquals(mDataManager.getPeople().getCount(), peopleCount);

        mPresenter.addPerson();
        mPresenter.addPerson();
        assertEquals(peopleCount + 2, mPresenter.getPeopleCount());

        mPresenter.removePerson();
        assertEquals(peopleCount + 1, mPresenter.getPeopleCount());
    }
}
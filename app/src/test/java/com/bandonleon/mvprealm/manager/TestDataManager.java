package com.bandonleon.mvprealm.manager;

import com.bandonleon.mvprealm.model.mock.MockPeople;
import com.bandonleon.mvprealm.model.People;
import com.bandonleon.mvprealm.model.mock.MockPerson;

import java.util.Random;

/**
 * Created by dom on 11/6/16.
 */

public class TestDataManager implements DataManager {

    private Random mRandom;
    private MockPeople mMockPeople;

    public TestDataManager() {
        mRandom = new Random();
        initMockModel();
    }

    private void initMockModel() {
        mMockPeople = new MockPeople();
        mMockPeople.add(new MockPerson("Jose", "Cuervos", 35, "415-466-1234"));
        mMockPeople.add(new MockPerson("Maria", "Santana", 28, "206-555-4321"));
        mMockPeople.add(new MockPerson("Osvaldo", "Rugierro", 45, "303-444-3784"));
        mMockPeople.add(new MockPerson("Rebecca", "Santos", 33, "209-498-7233"));
        mMockPeople.add(new MockPerson("Rodolfo", "Borrego", 52, "516-525-9098"));
        mMockPeople.add(new MockPerson("Cindy", "Torres", 26, "650-333-4567"));

        mMockPeople.add(new MockPerson("Silvia", "Rodrigues", 35, "415-888-5432"));
        mMockPeople.add(new MockPerson("Carlos", "Dominguez", 28, "209-225-9095"));
        mMockPeople.add(new MockPerson("Mercedes", "Plaza", 45, "303-242-1985"));
        mMockPeople.add(new MockPerson("Juanita", "Gonzalez", 33, "516-498-8893"));
        mMockPeople.add(new MockPerson("Ricardo", "Bollero", 52, "715-525-9098"));
        mMockPeople.add(new MockPerson("Cecilia", "Blanca", 26, "650-258-4567"));
    }

    @Override
    public People getPeople() {
        return mMockPeople;
    }

    @Override
    public void addPerson() {
        int numPeople = mMockPeople.getCount();
        mMockPeople.add(new MockPerson(mMockPeople.get(Math.abs(mRandom.nextInt()) % numPeople)));
    }

    @Override
    public void removePerson() {
        mMockPeople.remove(mMockPeople.getCount() - 1);
    }
}

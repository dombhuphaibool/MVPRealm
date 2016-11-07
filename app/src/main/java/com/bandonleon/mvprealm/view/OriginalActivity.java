package com.bandonleon.mvprealm.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bandonleon.mvprealm.R;
import com.bandonleon.mvprealm.manager.NetworkManager;
import com.bandonleon.mvprealm.manager.NetworkManager.NextPersonResponseHandler;
import com.bandonleon.mvprealm.manager.NetworkManager.RemovePersonResponseHandler;
import com.bandonleon.mvprealm.manager.mock.MockNetworkManager;
import com.bandonleon.mvprealm.model.MutableModel;
import com.bandonleon.mvprealm.model.People;
import com.bandonleon.mvprealm.model.Person;
import com.bandonleon.mvprealm.model.realm.RealmPeople;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.realm.Realm;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class OriginalActivity extends AppCompatActivity implements MutableModel.ChangeListener {

    // UI Widgets
    private RecyclerView mRecyclerView;
    private Button mAddBtn;
    private Button mDeleteBtn;
    private ProgressBar mProgressIndicator;

    // Adapter
    private RecyclerView.Adapter mAdapter;

    // Networking
    private NetworkManager mNetworkManager;

    private int mRequestInProgress;
    private int mLastPeopleCount;
    private int mNextPersonIndex;

    // Model
    private People mPeople;
    private Map<Integer, Integer> mRemovalPendingMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAddBtn = (Button) findViewById(R.id.btn_add);
        mDeleteBtn = (Button) findViewById(R.id.btn_delete);

        mProgressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);
        mProgressIndicator.setVisibility(GONE);
        mRequestInProgress = 0;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));

        // This would normally be done somewhere else like Application.onCreate()
        Realm.init(getApplicationContext());

        mPeople = new RealmPeople();
        mPeople.addModelChangeListener(this);
        mLastPeopleCount = mPeople.getCount();
        mNextPersonIndex = mLastPeopleCount;
        mRemovalPendingMap = new HashMap<>();

        mNetworkManager = new MockNetworkManager(getApplicationContext());

        mAdapter = new OrigPeopleAdapter(mPeople);
        mRecyclerView.setAdapter(mAdapter);

        mAddBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressIndicator(true);
                addPerson();
            }
        });

        mDeleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressIndicator(true);
                removePerson();
            }
        });
    }

    @Override
    public void onModelChanged(boolean success) {
        if (success) {
            int newPeopleCount = mPeople.getCount();
            if (newPeopleCount > mLastPeopleCount) {
                onPersonAdded();
            } else if (newPeopleCount < mLastPeopleCount) {
                onPersonRemoved();
            } else {
                onPersonModified();
            }
            mLastPeopleCount = newPeopleCount;
        } else {
            onError();
        }
    }

    private void onPersonAdded() {
        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        showProgressIndicator(false);
    }

    private void onPersonRemoved() {
        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
        showProgressIndicator(false);
    }

    private void onPersonModified() {
        mAdapter.notifyDataSetChanged();
        showProgressIndicator(false);
    }

    private void onError() {
        showProgressIndicator(false);
    }

    private void showProgressIndicator(boolean show) {
        mRequestInProgress += (show ? 1 : -1);
        mProgressIndicator.setVisibility(mRequestInProgress > 0 ? VISIBLE : GONE);
    }

    private void addPerson() {
        mNetworkManager.getNextPerson(mNextPersonIndex++, new NextPersonResponseHandler() {
            @Override
            public void onSuccess(@NonNull Person nextPerson) {
                mPeople.add(nextPerson);
            }

            @Override
            public void onFailure(String message) {
                mPeople.onAddError(message);
            }
        });
    }

    private void removePerson() {
        final int indexToRemove = (mPeople.getCount() - 1) - mRemovalPendingMap.size();
        mRemovalPendingMap.put(indexToRemove, indexToRemove);
        mNetworkManager.removePerson(indexToRemove, new RemovePersonResponseHandler() {
            @Override
            public void onSuccess() {
                for (Entry<Integer, Integer> entry : mRemovalPendingMap.entrySet()) {
                    if (entry.getKey() > indexToRemove) {
                        entry.setValue(entry.getValue() - 1);
                    }
                }
                Integer removeIndex = mRemovalPendingMap.get(indexToRemove);
                if (removeIndex != null) {
                    mRemovalPendingMap.remove(indexToRemove);
                    mPeople.remove(removeIndex);
                } else {
                    // Fatal error, clear and reset all pending removal
                    mRemovalPendingMap.clear();
                    onFailure("Invalid removal index " + indexToRemove);
                }
            }

            @Override
            public void onFailure(String message) {
                mPeople.onRemoveError(message);
            }
        });
    }

    public static class OrigPeopleAdapter extends RecyclerView.Adapter<OrigPeopleAdapter.ViewHolder> {

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView mFirstName;
            private TextView mLastName;
            private TextView mAge;
            private TextView mPhoneNumber;

            public ViewHolder(View rootView) {
                super(rootView);

                mFirstName = (TextView) rootView.findViewById(R.id.first_name_txt);
                mLastName = (TextView) rootView.findViewById(R.id.last_name_txt);
                mAge = (TextView) rootView.findViewById(R.id.age_txt);
                mPhoneNumber = (TextView) rootView.findViewById(R.id.phone_txt);
            }

            public void setFirstName(@NonNull String firstName) {
                mFirstName.setText(firstName);
            }

            public void setLastName(@NonNull String lastName) {
                mLastName.setText(lastName);
            }

            public void setAge(@NonNull String age) {
                mAge.setText(age);
            }

            public void setPhoneNumber(@NonNull String phoneNumber) {
                mPhoneNumber.setText(phoneNumber);
            }
        }

        private final People mPeople;

        public OrigPeopleAdapter(@NonNull People people) {
            mPeople = people;
        }

        @Override
        public int getItemCount() {
            return mPeople.getCount();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_view, parent, false);
            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Person person = mPeople.get(position);
            if (person != null) {
                holder.setFirstName(person.getFirstName());
                holder.setLastName(person.getLastName());
                holder.setAge(String.valueOf(person.getAge()));
                holder.setPhoneNumber(person.getPhoneNumber());
            }
        }
    }
}

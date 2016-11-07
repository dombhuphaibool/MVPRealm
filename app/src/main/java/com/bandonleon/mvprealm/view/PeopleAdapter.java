package com.bandonleon.mvprealm.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bandonleon.mvprealm.R;
import com.bandonleon.mvprealm.presenter.PeoplePresenter;

/**
 * Created by dom on 11/5/16.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements PersonView {

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

    private final PeoplePresenter mPresenter;

    public PeopleAdapter(@NonNull PeoplePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public int getItemCount() {
        return mPresenter.getPeopleCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_view, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mPresenter.bindPersonView(holder, position);
    }
}

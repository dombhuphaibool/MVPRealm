package com.bandonleon.mvprealm.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bandonleon.mvprealm.MVPRealmApplication;
import com.bandonleon.mvprealm.R;
import com.bandonleon.mvprealm.manager.DataManager;
import com.bandonleon.mvprealm.presenter.PeoplePresenter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MVPActivity extends AppCompatActivity implements PeopleView {

    private RecyclerView mRecyclerView;
    private Button mAddBtn;
    private Button mDeleteBtn;

    private ProgressBar mProgressIndicator;
    private int mRequestInProgress;

    private RecyclerView.Adapter mAdapter;
    private PeoplePresenter mPresenter;

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

        // @TODO: Inject this with dagger
        DataManager dataManager = MVPRealmApplication.getInstance().getDataManager();

        mPresenter = new PeoplePresenter(dataManager);
        mPresenter.bindView(this);

        mAdapter = new PeopleAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);

        mAddBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressIndicator(true);
                mPresenter.addPerson();
            }
        });

        mDeleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressIndicator(true);
                mPresenter.removePerson();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        mPresenter.release();
        super.onDestroy();
    }

    @Override
    public void onPersonAdded() {
        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        showProgressIndicator(false);
    }

    @Override
    public void onPersonRemoved() {
        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
        showProgressIndicator(false);
    }

    @Override
    public void onPersonModified() {
        mAdapter.notifyDataSetChanged();
        showProgressIndicator(false);
    }

    @Override
    public void onError() {
        showProgressIndicator(false);
    }

    private void showProgressIndicator(boolean show) {
        mRequestInProgress += (show ? 1 : -1);
        mProgressIndicator.setVisibility(mRequestInProgress > 0 ? VISIBLE : GONE);
    }
}

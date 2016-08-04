package com.brzhang.androidjobtest;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.brzhang.androidjobtest.jobs.NetWorkJob;
import com.brzhang.androidjobtest.jobs.NormalJob;

import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView content;
    TextView addNormalJobBt;
    TextView addNetworkJobBt;
    TextView addOtherJobBt;

    JobManager mJobManager;

    private CompositeSubscription _compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = (TextView) findViewById(R.id.text);
        addNormalJobBt = (TextView) findViewById(R.id.addNormalJob);
        addNetworkJobBt = (TextView) findViewById(R.id.addNetWorkJob);
        addOtherJobBt = (TextView) findViewById(R.id.addJob);


        addNetworkJobBt.setOnClickListener(this);
        addNormalJobBt.setOnClickListener(this);
        addOtherJobBt.setOnClickListener(this);
        RxBus.getRxBusSingleton().subscribe(_compositeSubscription, new RxBus.EventLisener() {
            @Override
            public void dealRxEvent(final Object event) {
                if (event instanceof String) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            addContent((String) event);
                        }
                    });

                }
            }
        });
        Configuration configuration = new Configuration.Builder(this).build();
        mJobManager = new JobManager(configuration);
    }


    @Override
    protected void onDestroy() {
        if (!_compositeSubscription.isUnsubscribed()) {
            _compositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * 刷新content列表
     *
     * @param msg
     */
    private void addContent(String msg) {
        content.append("\n" + msg);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.addJob:
                //mJobManager.addJobInBackground(ne);
                break;
            case R.id.addNetWorkJob:
                mJobManager.addJobInBackground(new NetWorkJob(MockUtils.mockContent()));
                break;
            case R.id.addNormalJob:
                mJobManager.addJobInBackground(new NormalJob(MockUtils.mockContent()));
                break;
        }
    }
}

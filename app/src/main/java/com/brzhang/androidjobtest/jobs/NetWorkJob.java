package com.brzhang.androidjobtest.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.brzhang.androidjobtest.RxBus;

/**
 * Created by brzhang on 16/8/4.
 * Description :
 */
public class NetWorkJob extends Job {

    private static final String TAG = "NetWorkJob";

    private String text;

    public NetWorkJob(String text) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("network_job"));
        this.text = "[" + TAG + "]" + text;
    }

    @Override
    public void onAdded() {
        //job has been secured to disk, add item to database
        Log.e(TAG, "onAdded() called with: " + "");
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, "onRun() called with: " + "");
        RxBus.getRxBusSingleton().send(text);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.e(TAG, "onCancel() called with: " + "cancelReason = [" + cancelReason + "], throwable = [" + throwable + "]");
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        Log.e(TAG, "shouldReRunOnThrowable() called with: " + "throwable = [" + throwable + "], runCount = [" + runCount + "], maxRunCount = [" + maxRunCount + "]");
        return RetryConstraint.RETRY;
    }
}

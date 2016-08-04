package com.brzhang.androidjobtest.jobs;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.brzhang.androidjobtest.RxBus;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by brzhang on 16/8/4.
 * Description :
 */
public class NormalJob extends Job {

    private static final String TAG = "NormalJob";

    private String text;

    public NormalJob(String text) {
        super(new Params(Priority.MID).setRequiresNetwork(false).persist().groupBy("normal_job"));
        this.text = "[" + TAG + "]" + text;
    }

    @Override
    public void onAdded() {
        Log.e(TAG, "onAdded() called with: " + "");
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, "onRun() called with: " + "");
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SystemClock.sleep(5000);
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                RxBus.getRxBusSingleton().send(text);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
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

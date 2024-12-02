package com.example.virtualbookshelf.viewmodel.Main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class that implements live data to inform about too many API requests on a given day
 * @param <T>
 */
public class SingleLiveEventTooManyApiCalls<T> extends MutableLiveData<T> {
    /**
     * AtomicBoolean value
     */
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    /**
     * Observe the MutableLiveData
     * @param owner    The LifecycleOwner which controls the observer
     * @param observer The observer that will receive the events
     */
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, t -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    /**
     * Sets the value. If there are active observers, the value will be dispatched to them.
     * @param value The new value
     */
    @Override
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    public void call() {
        setValue(null);
    }
}

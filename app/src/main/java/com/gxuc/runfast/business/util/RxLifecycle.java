package com.gxuc.runfast.business.util;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 管理rx生命周期
 * Created by Berial on 16/5/27.
 */
public class RxLifecycle {

    private enum Event {
        DESTROY
    }

    private final BehaviorSubject<Event> eventBehavior = BehaviorSubject.create();

    public void onDestroy() {
        eventBehavior.onNext(Event.DESTROY);
    }

    public static <T> ObservableTransformer<T, T> bindLifecycle(@NonNull Impl view) {
        return new CheckLifeCycleTransformer<>(view.bindLifecycle().eventBehavior);
    }

    public interface Impl {
        RxLifecycle bindLifecycle();
    }

    private static class CheckLifeCycleTransformer<T> implements ObservableTransformer<T, T> {

        private BehaviorSubject<Event> mEventBehavior;

        CheckLifeCycleTransformer(BehaviorSubject<Event> eventBehavior) {
            mEventBehavior = eventBehavior;
        }

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.takeUntil(mEventBehavior.skipWhile(new Predicate<Event>() {
                @Override
                public boolean test(@io.reactivex.annotations.NonNull Event event) throws Exception {
                    return event != Event.DESTROY;
                }
            }));
        }
    }
}
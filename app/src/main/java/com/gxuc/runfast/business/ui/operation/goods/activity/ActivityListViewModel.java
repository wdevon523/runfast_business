package com.gxuc.runfast.business.ui.operation.goods.activity;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.gxuc.runfast.business.ItemActivityBindingModel_;
import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.data.bean.Activity;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动列表
 * Created by Berial on 2017/9/1.
 */
public class ActivityListViewModel extends BaseViewModel {

    final Adapter mAdapter = new Adapter();

    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mCallback;

    private ActivityListNavigator mNavigator;

    private int page;
    private boolean isFirstLoad = true;

    ActivityListViewModel(Context context, LoadingCallback callback, ActivityListNavigator navigator) {
        super(context);
        mCallback = callback;
        mNavigator = navigator;
    }

    void start() {
        isFirstLoad = true;
        mRepo.resetActivityPages();
        loadActivities(page = 1);
    }

    void loadMoreActivities() {
        int maxPage = mRepo.getActivityPages();
        if (page < maxPage) {
            loadActivities(++page);
        }
    }

    private void loadActivities(int page) {
        mRepo.loadActivities(page)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> {
                    if (isFirstLoad) {
                        isFirstLoad = false;
//                        mCallback.onFirstLoadFinish();
                    }
                    if (page == 1) {
                        mCallback.onRefreshFinish();
                    } else {
                        mCallback.onLoadMoreFinish(isLastPage(page));
                    }

                    if (mAdapter.isEmpty()) {
                        mCallback.onLoadEmpty("暂时没有活动!");
                    }
//                    else {
//                        mCallback.onFirstLoadFinish();
//                    }

//                    mCallback.onLoadMoreFinish(isLastPage(page));
                })
                .subscribe(new ResponseSubscriber<List<Activity>>(mContext) {
                    @Override
                    public void onSuccess(List<Activity> activities) {
                        if (page == 1 && !mAdapter.isEmpty()) {
                            mAdapter.swap(generateActivityModels(activities));
                        } else {
                            mAdapter.addMore(generateActivityModels(activities));
                        }
                    }
                });
    }

    private List<ItemActivityBindingModel_> generateActivityModels(List<Activity> activities) {
        ArrayList<ItemActivityBindingModel_> models = new ArrayList<>();
        for (Activity activity : activities) {
            models.add(new ItemActivityBindingModel_()
                    .activity(activity)
                    .viewModel(this));
        }
        return models;
    }

    private boolean isLastPage(int currentPage) {
        return currentPage == mRepo.getActivityPages();
    }

    public void viewActivityDetail(Activity activity) {
        mNavigator.viewActivityDetail(activity);
    }

    public void deleteActivity(Activity activity) {
        mRepo.deleteActivity(activity.id)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.onRefreshFinish())
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onSuccess(BaseResponse response) {
//                        if (response.success || "操作成功！".equals(response.msg)) {
//                            status.set(status.get() % 2 + 1);
//                            statusName.set("恢复活动".equals(statusName.get()) ? "暂停活动" : "恢复活动");
//                            mNavigator.changeActivityStatusSuccess();
//                        }

                        if (response.success) {
                            List<EpoxyModel<?>> models = mAdapter.getModels();
                            for (EpoxyModel<?> model : models) {
                                ItemActivityBindingModel_ m = (ItemActivityBindingModel_) model;
                                if (m.activity().id == activity.id) {
                                    mAdapter.removeModel(m);
                                    break;
                                }
                            }
                        } else {
                            toast("删除失败");
                        }
                    }
                });
    }

    @BindingAdapter("activityType")
    public static void setActivityType(TextView view, int type) {
        Context context = view.getContext();

        // @formatter:off
        Drawable drawable = ContextCompat.getDrawable(context,
                type == 1 ? R.drawable.activity_jian
                        : type == 2 ? R.drawable.activity_zhe
                        : type == 3 ? R.drawable.activity_zeng
                        : type == 4 ? R.drawable.activity_te
                        : R.drawable.activity_mian
        ); // @formatter:on

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }
}

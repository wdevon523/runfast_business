package com.gxuc.runfast.business.ui.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.util.ViewModelHolder;

public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment implements LayoutProvider {

    private static final String VIEW_MODEL_TAG = "FRAGMENT_VIEW_MODEL_TAG";

    protected B mBinding;

    private BaseViewModel mVM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, thisLayoutId(), container, false);
        onBoundBinding(mBinding);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitViews();
        onLoadData();
    }

    protected void onBoundBinding(B binding) {}

    protected void onInitViews() {}

    protected void onLoadData() {}

    protected <T extends BaseViewModel> T thisViewModel() {
        return null;
    }

    protected void startAct(Class actClazz) {
        startActivity(new Intent(getActivity(), actClazz));
    }

    protected void startAct(Class actClazz, Bundle data) {
        startActivity(new Intent(getActivity(), actClazz).putExtras(data));
    }

    @SuppressWarnings("unchecked")
    protected <T extends BaseViewModel> T findOrCreateViewModel() {
        FragmentManager fm = getChildFragmentManager();

        ViewModelHolder<T> retainedViewModel =
                (ViewModelHolder<T>) fm.findFragmentByTag(VIEW_MODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewModel() != null) {
            mVM = retainedViewModel.getViewModel();
            return retainedViewModel.getViewModel();
        } else {
            T vm = thisViewModel();
            mVM = vm;
            if (vm != null) {
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(ViewModelHolder.createContainer(vm, false), VIEW_MODEL_TAG);
                transaction.commit();
            }
            return vm;
        }
    }

    @Override
    public void onDestroyView() {
        if (mVM != null) {
            mVM.onDestroy();
        }
        super.onDestroyView();
    }
}

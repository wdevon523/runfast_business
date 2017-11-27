package com.gxuc.runfast.business.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;


/**
 * Non-UI Fragment used to retain ViewModels.
 */
public class ViewModelHolder<VM> extends Fragment {

    private VM mViewModel;

    public ViewModelHolder() {}

    public static <M> ViewModelHolder<M> createContainer(M viewModel, boolean retain) {
        ViewModelHolder<M> holder = new ViewModelHolder<>();
        Bundle bundle = new Bundle();
        bundle.putBoolean("retain", retain);
        holder.setArguments(bundle);
        holder.setViewModel(viewModel);
        return holder;
    }

    public static <M> ViewModelHolder createContainer(@NonNull M viewModel) {
        ViewModelHolder<M> viewModelContainer = new ViewModelHolder<>();
        viewModelContainer.setViewModel(viewModel);
        return viewModelContainer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        setRetainInstance(bundle == null || bundle.getBoolean("retain", true));
    }

    @Nullable
    public VM getViewModel() {
        return mViewModel;
    }

    public void setViewModel(@NonNull VM viewModel) {
        mViewModel = viewModel;
    }
}

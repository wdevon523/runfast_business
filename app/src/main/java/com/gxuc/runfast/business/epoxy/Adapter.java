package com.gxuc.runfast.business.epoxy;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;

import java.util.Collection;
import java.util.List;

public class Adapter extends EpoxyAdapter {

    public void swap(final Collection<? extends EpoxyModel<?>> epoxyModels) {
        removeAllModels();
        addModels(epoxyModels);
    }

    public void addMore(final Collection<? extends EpoxyModel<?>> epoxyModels) {
        addModels(epoxyModels);
    }

    public void addModel(final EpoxyModel<?> modelToAdd) {
        super.addModel(modelToAdd);
    }

    public void addHeader(EpoxyModel<?> header) {
        if (!models.contains(header)) {
            models.add(0, header);
            notifyDataSetChanged();
        }
    }

    public void addFooter(EpoxyModel<?> footer) {
        if (!models.contains(footer)) {
            addModel(footer);
        }
    }


    public void removeModel(final EpoxyModel<?> model) {
        super.removeModel(model);
    }

    public void clear() {
        removeAllModels();
    }

    public List<EpoxyModel<?>> getModels() {
        return models;
    }

    public int getModelPosition(EpoxyModel<?> model) {
        return super.getModelPosition(model);
    }
}

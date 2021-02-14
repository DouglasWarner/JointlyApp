package com.douglas.jointlyapp.ui.home;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.base.BaseListView;
import com.douglas.jointlyapp.ui.base.BasePresenter;

public interface HomeContract {
    interface View extends BaseListView<Initiative>
    {
        void setNoData();
        void showProgress();
        void hideProgress();
        //TODO implementar los metodo cuando el usuario interactua con la toolbar de iniciativas
    }

    interface Presenter extends BasePresenter
    {
        void load();

        //TODO implementar los metodos cuando el usuario efectua alguna accion la toolbar de iniciativas
    }
}

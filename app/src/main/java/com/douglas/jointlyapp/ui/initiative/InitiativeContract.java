package com.douglas.jointlyapp.ui.initiative;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.base.BaseListView;
import com.douglas.jointlyapp.ui.base.BasePresenter;

public interface InitiativeContract {
    interface View extends BaseListView<Initiative>
    {
        void setNoData();
        void showProgress();
        void hideProgress();
        //TODO implementar los metodo cuando el usuario interactua con la lista de iniciativas
    }

    interface Presenter extends BasePresenter
    {
        void load();

        //TODO implementar los metodos cuando el usuario efectua alguna accion con la iniciativa
    }
}

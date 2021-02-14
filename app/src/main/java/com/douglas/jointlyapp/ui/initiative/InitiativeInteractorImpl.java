package com.douglas.jointlyapp.ui.initiative;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;

import java.util.List;

public class InitiativeInteractorImpl {

    interface ListInitiativeInteractor
    {
        void onNoData();
        void onSuccess(List<Initiative> list);
        //TODO implementar los metodos cuando el usuario interactua con las iniciativas
    }

    private ListInitiativeInteractor interactor;

    public InitiativeInteractorImpl(ListInitiativeInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadData()
    {
        List<Initiative> list = InitiativeRepository.getInstance().getList();

        if (list.isEmpty()) {
            interactor.onNoData();
        } else {
            interactor.onSuccess(list);
        }
    }
}

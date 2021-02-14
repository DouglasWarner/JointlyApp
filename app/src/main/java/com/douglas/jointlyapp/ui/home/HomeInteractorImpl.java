package com.douglas.jointlyapp.ui.home;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;

import java.util.List;

public class HomeInteractorImpl {

    interface ListInitiativeInteractor
    {
        void onNoData();
        void onSuccess(List<Initiative> list);
        //TODO implementar los metodos cuando el usuario interactua con las iniciativas
    }

    private ListInitiativeInteractor interactor;

    public HomeInteractorImpl(ListInitiativeInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadData()
    {
        InitiativeRepository repository = InitiativeRepository.getInstance();
        List<Initiative> list = repository.getList();

        if (list.isEmpty()) {
            interactor.onNoData();
        } else {
            interactor.onSuccess(list);
        }
    }
}

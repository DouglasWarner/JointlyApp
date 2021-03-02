package com.douglas.jointlyapp.ui.showinitiative;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.List;

public class ShowInitiativeInteractorImpl {

    interface ShowInitiativeInteractor
    {
        void onUserListEmpty();
        void onJoined();
        void onUnJoined();
        void onLoadListUserJoined(List<User> userList);
        void onLoadUserOwner(User user);
        void onSuccessLoad(Initiative initiative);
    }

    private ShowInitiativeInteractor interactor;

    public ShowInitiativeInteractorImpl(ShowInitiativeInteractor interactor) {
        this.interactor = interactor;
    }

    /**
     * Carga el estado del boton unirse
     * @param idInitiative
     */
    public void loadUserStateJoined(final int idInitiative)
    {
        String user = JointlyPreferences.getInstance().getUser();
        UserJoinInitiative userJoinInitiative = InitiativeRepository.getInstance().getUserJoined(idInitiative, user);

        if(userJoinInitiative == null)
            interactor.onUnJoined();
        else
            interactor.onJoined();
    }

    /**
     * El usuario se une a la iniciativa
     * @param initiative
     */
    public void joinInitiative(final Initiative initiative) {
        String user = JointlyPreferences.getInstance().getUser();

        UserJoinInitiative userJoinInitiative = new UserJoinInitiative(initiative.getId(), user, CommonUtils.getDateNow());

        InitiativeRepository.getInstance().insertUserJoin(userJoinInitiative);

        interactor.onJoined();
    }

    /**
     * El usuario cancela la union a la iniciativa
     * @param initiative
     */
    public void unJoinInitiative(final Initiative initiative)
    {
        String user = JointlyPreferences.getInstance().getUser();

        UserJoinInitiative userJoinInitiative = InitiativeRepository.getInstance().getUserJoined(initiative.getId(), user);

        InitiativeRepository.getInstance().deleteUserJoin(userJoinInitiative);

        interactor.onUnJoined();
    }

    /**
     * Carga la lista de imagenes de los usuarios unidos a la iniciativa
     * @param idInitiative
     */
    public void loadListUserJoined(final int idInitiative)
    {
        List<User> userList = UserRepository.getInstance().getListUserJoined(idInitiative);

        if(userList.isEmpty())
            interactor.onUserListEmpty();
        else
            interactor.onLoadListUserJoined(userList);
    }

    /**
     * Carga la imagen del usuario creador de la iniciativa
     * @param idInitiative
     */
    public void loadUserOwner(final int idInitiative)
    {
        User user = UserRepository.getInstance().getUserOwnerInitiative(idInitiative);

        interactor.onLoadUserOwner(user);
    }

    /**
     * Carga la iniciativa
     * @param idInitiative
     */
    public void loadInitiative(final int idInitiative)
    {
        Initiative initiative = InitiativeRepository.getInstance().getInitiative(idInitiative);

        interactor.onSuccessLoad(initiative);
    }
}

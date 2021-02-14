package com.douglas.jointlyapp.data.repository;

import android.net.Uri;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class InitiativeRepository {

    private static InitiativeRepository initiativeRepository;
    private List<Initiative> list;
    private static int iteractorId;

    static {
        initiativeRepository = new InitiativeRepository();
    }

    private InitiativeRepository()
    {
        this.list = new ArrayList<>();
        iteractorId = 0;

        initialice();
    }

    private void initialice() {
        User user1 = UserRepository.getInstance().getUser("maria@gmail.com");
        User user2 = UserRepository.getInstance().getUser("douglas@gmail.com");
        Uri image = CommonUtils.getImagenInitiativeDefault(JointlyApplication.getContext());

        Initiative i = new Initiative(iteractorId, "Recoger basura en la playa","05/01/2021","12/05/2021","12:00","La playa de Málaga queremos limpiarla porque esta muy sucia por la contaminación","Limpieza de playas Málaga","Málaga", image,"20kg","A", user1.getName());
        i.getUserJoined().add(user2);
        list.add(i);

        Initiative i1 = new Initiative(iteractorId, "Recolecta de alimentos","28/02/2021","12/03/2021","14:00","Descripcion iniciativa","Recolecta de comidas","Málaga", image,"20kg","A", user2.getName());
        list.add(i1);
    }

    public static InitiativeRepository getInstance()
    {
        return initiativeRepository;
    }

    public List<Initiative> getList()
    {
        return list;
    }

    public int getCountUserJoined(int idInitiative)
    {
        return list.get(idInitiative).getUserJoined().size();
    }

    //TODO refactorizar
    public boolean setUserJoin(Initiative initiative, User user)
    {
        if(list.get(list.indexOf(initiative)).getUserJoined().contains(user))
            return false;

        list.get(list.indexOf(initiative)).getUserJoined().add(user);
        return true;
    }

    public void add(Initiative initiative)
    {
        initiative.setId(iteractorId++);
        list.add(initiative);
    }

    public void update(Initiative editInitiative) {
        list.set(list.indexOf(editInitiative), editInitiative);
    }

    public void delete(Initiative initiative) {
        list.remove(initiative);
    }
}

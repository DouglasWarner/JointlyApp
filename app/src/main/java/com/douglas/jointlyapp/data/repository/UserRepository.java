package com.douglas.jointlyapp.data.repository;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static UserRepository userRepository;
    private List<User> list;
    private static int iteractorId;

    static {
        userRepository = new UserRepository();
    }

    private UserRepository() {
        this.list = new ArrayList<>();
        iteractorId = 0;
        initialice();
    }

    private void initialice() {
        User user1 = new User(iteractorId++,"douglas@gmail.com","Dou123456","Douglas","666666666",CommonUtils.getImagenUserDefault(JointlyApplication.getContext()),"Málaga","Me dedico a crear iniciativas para ayudar a los mas necesitados","12/02/2021");
        User user2 = new User(iteractorId++,"maria@gmail.com","Mai123456","Maria","666666666",CommonUtils.getImagenUserDefault(JointlyApplication.getContext()),"Sevilla","Me gusta sanear las zonas importante de donde vivo","01/01/2021");
        list.add(user1);
        list.add(user2);

        user1.getUserFollowed().add(user2);
    }

    public static UserRepository getInstance()
    {
        return userRepository;
    }

    public List<User> getList()
    {
        return list;
    }

    public void add(String email, String password, String userName)
    {
        list.add(new User(iteractorId++, email, password, userName,"", CommonUtils.getImagenUserDefault(JointlyApplication.getContext()),"","",""));
    }

    public boolean setUserFollowed(User user, User userFollow)
    {
        if(list.get(list.indexOf(user)).getUserFollowed().contains(userFollow))
            return false;

        list.get(list.indexOf(user)).getUserFollowed().add(userFollow);
        return true;
    }

    public List<User> getUserFollowed(User user)
    {
        return list.get(list.indexOf(user)).getUserFollowed();
    }

    public List<User> getUserFollowers(User user)
    {
        return list.get(list.indexOf(user)).getUserFollowers();
    }

    public boolean validateCredentials(String email, String password)
    {
        for (User user : list) {
        if ((user.getEmail().equals(email)) && (user.getPassword().equals(password)))
            return true;
        }
        return false;
    }

    public User getUser(String email)
    {
        for (User user :
                list) {
            if(user.getEmail() == email)
                return user;
        }

        return null;
    }

    public boolean userExists(String email)
    {
        for (User user : list) {
            if(user.getEmail().equals(email))
                return true;
        }
        return false;
    }
}

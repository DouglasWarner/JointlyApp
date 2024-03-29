package com.douglas.jointlyapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.douglas.jointlyapp.data.dao.ChatDao;
import com.douglas.jointlyapp.data.dao.InitiativeDao;
import com.douglas.jointlyapp.data.dao.UserDao;
import com.douglas.jointlyapp.data.dao.UserFollowUserDao;
import com.douglas.jointlyapp.data.dao.UserJoinInitiativeDao;
import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Initiative.class, UserFollowUser.class, UserJoinInitiative.class, Chat.class}, version = 4, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class JointlyDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract InitiativeDao initiativeDao();
    public abstract UserFollowUserDao userFollowUserDao();
    public abstract UserJoinInitiativeDao userJoinInitiativeDao();
    public abstract ChatDao chatDao();

    public static volatile JointlyDatabase instance;
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService DATABASE_WRITE_EXECUTOR = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static void create(final Context context)
    {
        if(instance == null)
        {
            synchronized (JointlyDatabase.class)
            {
                if(instance == null)
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            JointlyDatabase.class, "jointlyapp").build();
            }
        }
    }

    public static JointlyDatabase getDatabase()
    {
        return instance;
    }

}

package com.douglas.jointlyapp.ui.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.repository.ChatRepository;
import com.douglas.jointlyapp.ui.initiative.InitiativeInteractorImpl;

public class BackgroundJobService extends JobService {

    private static final int TIME = 10000;
    @Override
    public boolean onStartJob(JobParameters params) {

        ChatRepository repository = ChatRepository.getInstance();

        Initiative initiative = (Initiative)params.getTransientExtras().getSerializable(Initiative.TAG);

        while (true)
        {
            int countChat = repository.getChatInitiative(initiative.getId()).size();
            new Handler().postDelayed(() -> {
                if(repository.getChatInitiative(initiative.getId()).size() > countChat)
                {
                    Intent intent = new Intent("com.douglas.notification_new_message_chat");
                    sendBroadcast(intent);
                    jobFinished(params, false);
                }
            },TIME);
            try {
                Thread.sleep(TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}

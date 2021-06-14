package com.douglas.jointlyapp.ui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.service.CheckConnectionService;

/**
 * Entity that manage the message reciever of CheckConnectionService
 */
public class CheckConnectionBroadCast extends BroadcastReceiver {

    TextView tvNoConnection;

    public CheckConnectionBroadCast() {
    }

    public CheckConnectionBroadCast(TextView tvNoConnection) {
        this.tvNoConnection = tvNoConnection;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(JointlyApplication.CHECK_CONNECTION_BROADCAST)){
            //TODO comprobar los datos al hacer sync
            boolean isConnectionAvailable = intent.getExtras().getBoolean(CheckConnectionService.TAG_CONNECTION);
            boolean isConnectionWithApiAvailable = intent.getExtras().getBoolean(CheckConnectionService.TAG_SYNC);
            JointlyApplication.setConnection(isConnectionAvailable);
            if(isConnectionAvailable && isConnectionWithApiAvailable) {
                tvNoConnection.setText(JointlyApplication.getContext().getString(R.string.refresh_for_sync));
                tvNoConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_refresh_connection, 0);
            } else if(!isConnectionAvailable) {
                JointlyPreferences.getInstance().putSync(false);
//                tvNoConnection.setVisibility(View.VISIBLE);
                tvNoConnection.setText(JointlyApplication.getContext().getString(R.string.error_no_connection));
                tvNoConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_connection, 0);
            }
        }
    }
}

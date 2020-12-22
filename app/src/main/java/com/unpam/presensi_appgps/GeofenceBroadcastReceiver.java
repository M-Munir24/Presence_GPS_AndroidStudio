package com.unpam.presensi_appgps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();

        NotificationHelper notificationHelper = new NotificationHelper ( context );


        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent ( intent );

        if (geofencingEvent.hasError ()) {
            Log.d ( TAG, "onReceive: Error receiving geofence event..." );
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences ();
        for (Geofence geofence : geofenceList) {
            Log.d ( TAG, "onReceive: " + geofence.getRequestId () );
        }
//        Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition ();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText ( context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT ).show ();
                notificationHelper.sendHighPriorityNotification ( "GEOFENCE_TRANSITION_ENTER", "", MapsActivity.class );

                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText ( context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT ).show ();
                notificationHelper.sendHighPriorityNotification ( "GEOFENCE_TRANSITION_DWELL", "", MapsActivity.class );
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText ( context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT ).show ();
                notificationHelper.sendHighPriorityNotification ( "GEOFENCE_TRANSITION_EXIT", "", MapsActivity.class );
                break;
        }
//        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
//            startActivity(new Intent(GeofenceBroadcastReceiver.this, MapsActivity.class));


        //}

    }
}

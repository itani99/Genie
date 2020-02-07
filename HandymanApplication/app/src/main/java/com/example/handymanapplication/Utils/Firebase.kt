package com.example.handymanapplication.Utils

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class Firebase : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //any notification reach here so
        //so here if type is comment :P
        //// mesheee el 7all fhemet bs matwak3ta hal add sheemle
        // here u send if type is comment u send notification and broadcast to the chat activity and
        //append the chat list . nice nice, fe ashya fene e2ra 3ana w mawjoden bhak shape sa7?
        // akid tamemm Rabee, ysalemoonn <3 :p
        // hh enjoy coding hahahahaha nshallahh
        if (remoteMessage.data.isNotEmpty()){
            //there is a new notification
            // to be notified to the device
        }


    }

    override fun onNewToken(token: String) {

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        Utils.sendRegistrationToServer(baseContext)
    }

}
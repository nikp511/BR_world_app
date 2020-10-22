package helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


/**
 * Created by Andoid-Devloper on 11/21/2018.
 */

public class SMSReceiver extends BroadcastReceiver {
    private static SMSListner mListener;
    Boolean b = true;
    String abcd, xyz;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            // b=sender.endsWith("WNRCRP");  //Just to fetch otp sent from WNRCRP
            String messageBody = smsMessage.getMessageBody();
            abcd = messageBody.replaceAll("[^0-9]", "");
            Log.e("abcddd", "code" + abcd);
            // here abcd contains otp
            // which is in number format
            //Pass on the text to our listener.


            if (b == true) {
                mListener.messageReceived(abcd);  // attach value to interface
                // object
            } else {
            }
        }
    }

    public static void bindListener(SMSListner listener) {
        mListener = listener;
    }
}

package studio.smartters.moward.Others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import studio.smartters.moward.HelpActivity;

public class MySmsBroadcastReciever extends BroadcastReceiver
{

    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String from = "";
            String body = "";
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[0]);
            String smsBody = smsMessage.getMessageBody().toString();
            String address = smsMessage.getOriginatingAddress();
            if(from.equalsIgnoreCase( "MD-JAVATC")) {
                HelpActivity inst = HelpActivity.instance();
                if (inst != null)
                    if (inst.et_otp != null)
                        inst.et_otp.setText(body.split(":")[1]);
            }
        }
    }


}

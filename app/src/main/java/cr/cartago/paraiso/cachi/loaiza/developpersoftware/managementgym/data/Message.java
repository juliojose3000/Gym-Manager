package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.data;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class Message {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private String customerNumberPhone;

    private String customerName;

    private String endPayment;

    public Message(String customerNumberPhone, String customerName, String endPayment){

        this.customerName = customerName;

        this.customerNumberPhone = customerNumberPhone;

        this.endPayment = endPayment;

    }

    public void sendMessage(Context context){

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission(context)) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestPermission(context);
            }
        }

        String sms = "Buenos días "+customerName+", de parte del gimnacio Cachí Fitness Center, se le informa que su mensualidad caduca este "+this.endPayment+". Se agradece la puntualidad en los pagos.";
        //String sms = "Buenos dias "+customerName;
        String phoneNum = this.customerNumberPhone;
        if(!TextUtils.isEmpty(sms) && !TextUtils.isEmpty(phoneNum)) {
            if(checkPermission(context)) {

                //Get the default SmsManager//

                SmsManager smsManager = SmsManager.getDefault();

                ArrayList<String> parts = smsManager.divideMessage(sms);

                //Send the SMS//
                smsManager.sendMultipartTextMessage(phoneNum, null, parts, null, null);
                //smsManager.sendTextMessage(phoneNum, null, sms, null, null);
            }else {
                //Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(Context context) {
        Activity activity = (Activity) context;
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);

    }


}

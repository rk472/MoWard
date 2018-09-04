package studio.smartters.moward;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import studio.smartters.moward.Others.Constants;

public class HelpActivity extends AppCompatActivity {
    private EditText numberText;
    private Button otpButton;
    private BottomSheetDialog mBottomSheetDialog;
    private View dialogView;
    public EditText et_otp;
    private TextView tv_modal;
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        numberText=findViewById(R.id.help_no);
        otpButton=findViewById(R.id.otp_button);
        otpButton.setEnabled(false);
        numberText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    otpButton.setEnabled(false);
                }else{
                    otpButton.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void createModal(View view) {
        number=numberText.getText().toString();
        VerifyTask vt=new VerifyTask();
        mBottomSheetDialog = new BottomSheetDialog(HelpActivity.this);
        dialogView = HelpActivity.this.getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        et_otp = dialogView.findViewById(R.id.otp_et);
        tv_modal= dialogView.findViewById(R.id.modal_text);
        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        mBottomSheetDialog.show();
        vt.execute(Constants.URL+"verifyNumber");
    }
    static HelpActivity inst;
    public static HelpActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    class VerifyTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                URLConnection con=url.openConnection();
                InputStream is=con.getInputStream();
                InputStreamReader ir=new InputStreamReader(is);
                int data=ir.read();
                String res="";
                while(data!=-1){
                    res+=(char)data;
                    data=ir.read();
                }
                return res;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject json=new JSONObject();
            try {
                json.put("status","false");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject json=new JSONObject(s);
                String status=json.getString("status");
                if(status.equalsIgnoreCase("true")){
                    final String id=json.getString("id");
                    et_otp.setVisibility(View.VISIBLE);
                    tv_modal.setText("Waiting For OTP");
                    final int captcha=generateCaptchaString();
                    OTPTask ot=new OTPTask();
                    ot.execute("http://sms.forcesms.info/sendSMS?username=jtbabu&message=Your onetime password for asking help in MoWard app is MW:"+captcha+"&sendername=JAVATC&smstype=TRANS&numbers="+number+"&apikey=261819f9-0181-4274-941b-c8602ef5225d");
                    et_otp.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(Integer.parseInt(s.toString())==captcha){
                                Intent i=new Intent(HelpActivity.this,HelpContentActivity.class);
                                i.putExtra("number",number);
                                i.putExtra("id",id);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                }else{
                    Toast.makeText(HelpActivity.this, "Cant verify number :"+s, Toast.LENGTH_SHORT).show();
                    mBottomSheetDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
    public int generateCaptchaString() {
        Random rnd=new Random();
        int n=100000+rnd.nextInt(900000);
        return n;
    }
    class OTPTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                URLConnection con=url.openConnection();
                InputStream is=con.getInputStream();
                return "res";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "err";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("err")){
                Toast.makeText(HelpActivity.this, "Some Error Occured..Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

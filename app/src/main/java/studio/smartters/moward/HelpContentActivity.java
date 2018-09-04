package studio.smartters.moward;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HelpContentActivity extends AppCompatActivity {
    private EditText helpText;
    private Button helpButton;
    private ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_content);
        helpText=findViewById(R.id.help_text);
        helpButton=findViewById(R.id.help_button);
        helpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    helpButton.setEnabled(false);
                }else{
                    helpButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void submit(View view) {
        String body=helpText.getText().toString();
        String number=getIntent().getExtras().getString("number");
        String id=getIntent().getExtras().getString("id");
        HelpTAsk ht=new HelpTAsk();
        p=new ProgressDialog(this);
        p.setMessage("Please wait while we are submitting your query");
        p.setTitle("Please wait");
        p.setCanceledOnTouchOutside(false);
        p.setCancelable(false);
        p.show();
        ht.execute("http://205.147.101.127:8084/MoWord/submitQuery?id="+id+"&phone="+number+"&body="+body);
    }
    private class HelpTAsk extends AsyncTask<String,Void,String>{

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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            try {
                JSONObject json=new JSONObject(s);
                if(Boolean.parseBoolean(json.getString("status"))){
                    Toast.makeText(HelpContentActivity.this, "Your Query has been successfully submitted", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(HelpContentActivity.this, "Some Error Occured...try again later..", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

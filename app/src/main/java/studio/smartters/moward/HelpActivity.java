package studio.smartters.moward;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void createModal(View view) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(HelpActivity.this);
        View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        final EditText et_otp = dialogView.findViewById(R.id.otp_et);
        final TextView tv_modal = dialogView.findViewById(R.id.modal_text);
        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        mBottomSheetDialog.show();
        et_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetDialog.dismiss();
                        startActivity(new Intent(HelpActivity.this,HelpContentActivity.class));
                    }
                },3000);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                et_otp.setVisibility(View.VISIBLE);
                tv_modal.setText("Waiting For OTP");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        et_otp.setText("123456");
                    }
                },3000);
            }
        },3000);
    }
}

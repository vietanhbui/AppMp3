package com.anhbv.appmp3.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.anhbv.appmp3.R;

public class TimerDiaLog extends Dialog {
    private static final String TAG = "TimerDiaLog";
    private TextView tvTime;
    private SwitchCompat scTime;
    private RadioButton rbtnTime30;
    private RadioButton rbtnTime60;
    private RadioButton rbtnTime90;
    private RadioButton rbtnTime120;
    private EditText edtTime;
    private TextView tvTimeOk;
    private RadioGroup rgTime;
    private RadioButton radioButton;

    public TimerDiaLog(Context context) {
        super(context);
        initDiaLog();
        registrationView();

    }


    public TimerDiaLog(Context context, int themeResId) {
        super(context, themeResId);
        initDiaLog();
        registrationView();
    }

    protected TimerDiaLog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDiaLog();
        registrationView();
    }


    private void registrationView() {
        rgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.r_btn_30:
                        tvTime.setText("00:29");
                        break;
                    case R.id.r_btn_60:
                        tvTime.setText("00:59");
                        break;
                    case R.id.r_btn_90:
                        tvTime.setText("01:29");
                        break;
                    case R.id.r_btn_120:
                        tvTime.setText("01:59");
                        break;
                }
            }
        });

        tvTimeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTime.getText().toString().isEmpty()){
                    edtTime.setText("1");
                }else {
                    String value = edtTime.getText().toString();
                    int hour = Integer.parseInt(value)/60;
                    int minute = (Integer.parseInt(value) % 60)-1;
                    tvTime.setText("0"+hour+":"+minute);
                }

            }
        });
    }


    private void initDiaLog() {
        setContentView(R.layout.timer_dialog);
        tvTime = findViewById(R.id.tv_timer_dialog);
        scTime = findViewById(R.id.sc_switch_dialog);
        rbtnTime30 = findViewById(R.id.r_btn_30);
        rbtnTime60 = findViewById(R.id.r_btn_60);
        rbtnTime90 = findViewById(R.id.r_btn_90);
        rbtnTime120 = findViewById(R.id.r_btn_120);
        edtTime = findViewById(R.id.edt_timer);
        tvTimeOk = findViewById(R.id.tv_timer_ok);
        rgTime = findViewById(R.id.rg_timer_dialog);
    }
}

package com.cmput301f19t09.vibes.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.cmput301f19t09.vibes.R;

public class MoodFilterDialog extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public MoodFilterDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.btn_cancel);
        no = (Button) findViewById(R.id.btn_confirm);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                c.finish();
                break;
            case R.id.btn_confirm:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}

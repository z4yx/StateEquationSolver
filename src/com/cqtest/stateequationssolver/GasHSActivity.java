package com.cqtest.stateequationssolver;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhang on 14-12-14.
 */
public class GasHSActivity extends GasActivity {

    @InjectView(R.id.edit_Hid)
    EditText mEditHid;
    @InjectView(R.id.edit_Sid)
    EditText mEditSid;
    @InjectView(R.id.edit_P0)
    EditText mEditP0;

    @InjectView(R.id.textResult)
    TextView mResult;

    @InjectView(R.id.radio_RK)
    RadioButton mMethodRK;
    @InjectView(R.id.radio_Virial)
    RadioButton mMethodVirial;

    @Override
    protected void gotError(String mathException, Object obj) {

        mResult.setText(mathException+"\n"+obj);
    }

    @Override
    protected void gotResult(Object obj, int operation) {
        mResult.setText((String)obj);
    }

    @Override
    protected void initContentView() {
        equation_filter=EquationDb.FILTER_STATE;
        setContentView(R.layout.activity_gas_hs);
    }

    @OnClick({R.id.calc_H,R.id.calc_S})
    void onCalcHS(View v)
    {
        int equ = 1;
        assert EquationDb.getNames(EquationDb.FILTER_STATE)[equ].equals("RK");

        try {
            Calculator c = new Calculator();
            c.prepareSubstance(
                    mEditTc.getText().toString(),
                    mEditPc.getText().toString(),
                    mEditZc.getText().toString(),
                    mEditW.getText().toString(),
                    mEditT.getText().toString(),
                    equ
            );
            if (mEnable2.isChecked()) {
                Calculator c2 = new Calculator();
                c2.prepareSubstance(
                        mEditTc2.getText().toString(),
                        mEditPc2.getText().toString(),
                        mEditZc2.getText().toString(),
                        mEditW2.getText().toString(),
                        mEditT.getText().toString(),
                        equ
                );
                    c.mixWith(c2,
                            (mMixMethodA.isChecked() ? Calculator.MIX_SQR : Calculator.MIX_LIN),
                            Calculator.MIX_LIN,
                            mEditY1.getText().toString(),
                            mEditY2.getText().toString()
                    );
            }
            String H_S_id;
            boolean isH;
            if(v.getId() == R.id.calc_H){
                H_S_id = mEditHid.getText().toString();
                isH = true;
            }else{
                H_S_id = mEditSid.getText().toString();
                isH = false;
            }
            if(mMethodRK.isChecked()) {
                c.calcHS(mEditT.getText().toString(),
                        mEditP.getText().toString(),
                        mEditVm.getText().toString(),
                        H_S_id,
                        mEditP0.getText().toString(),
                        isH, mHandler, v.getId());
            }else{
                c.calcHS2(mEditT.getText().toString(),
                        mEditP.getText().toString(),
                        mEditTc.getText().toString(),
                        mEditPc.getText().toString(),
                        mEditW.getText().toString(),
                        H_S_id,
                        mEditP0.getText().toString(),
                        isH, mHandler, v.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

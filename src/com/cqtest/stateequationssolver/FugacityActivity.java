package com.cqtest.stateequationssolver;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhang on 14-12-14.
 */
public class FugacityActivity extends GasActivity {

    @InjectView(R.id.textResult)
    TextView mResult;

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
        equation_filter=EquationDb.FILTER_Fugacity;
        setContentView(R.layout.activity_gas_fugacity);
    }

    @OnClick(R.id.calc_fugacity)
    void onCalc(View v){

        int equ = EquationDb.item2equ(mChooseEquation.getSelectedItemPosition(), equation_filter);

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

            c.calcPhi(equ,
                    mEditT.getText().toString(),
                    mEditP.getText().toString(),
                    mEditVm.getText().toString(),
                    mHandler, v.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

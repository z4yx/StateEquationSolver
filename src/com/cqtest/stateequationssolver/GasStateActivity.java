package com.cqtest.stateequationssolver;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhang on 14-12-14.
 */
public class GasStateActivity extends GasActivity {

    @InjectView(R.id.textResult)
    TextView mResult;

    protected void gotError(String mathException, Object obj) {
        mResult.setText(mathException+"\n"+obj);
    }

    @Override
    protected void initContentView() {
        equation_filter=EquationDb.FILTER_STATE;
        setContentView(R.layout.activity_gas_state);
    }


    @OnClick({R.id.calc_P,R.id.calc_Vm})
    public void OnParamCalc(View v) {
        String unknown;
        String known;

        switch (v.getId()){
            case R.id.calc_Vm:
                unknown="Vm,P";
                known = "P=="+mEditP.getText().toString();
                break;
            case R.id.calc_P:
                unknown="P,Vm";
                known = "Vm=="+mEditVm.getText().toString();
                break;
            default:
                return;

        }
        try {
            int equ = EquationDb.item2equ(mChooseEquation.getSelectedItemPosition(), EquationDb.FILTER_STATE);
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
            c.solveEquation(equ, unknown, known, mEditT.getText().toString(), mHandler, v.getId());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "发生异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    protected void gotResult(Object obj, int operation) {
        HashMap<String, ArrayList<String>> collect = (HashMap<String, ArrayList<String>>)obj;
        ArrayList<String> values = null;
        mResult.setText("");
        String finalResult = new String();
        switch (operation){
            case R.id.calc_Vm:
                values = collect.get("vm");
                finalResult += "Vm:\n";
                break;
            case R.id.calc_P:
                values = collect.get("p");
                finalResult += "P:\n";
                break;
        }
        if(values == null)
            return;
        double vm=0;
        for(String v : values){
            if(v.contains("I"))
                continue;
            v=v.trim();
            if(operation==R.id.calc_Vm){
                try {
                    double t = Double.valueOf(v);
                    if(t > vm)
                        vm=t;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
//            finalResult += v + "\n";
        }
        if(vm != 0) {
            finalResult += vm + "\n";
            ((SolverApplication)getApplication()).setV(vm);
            mEditVm.setText(String.valueOf(vm));
        }
        mResult.setText(finalResult);
    }

}

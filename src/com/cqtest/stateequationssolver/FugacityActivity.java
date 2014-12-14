package com.cqtest.stateequationssolver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

    private double edit2double(EditText e){
        return Double.valueOf(e.getText().toString());
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
                String name = EquationDb.getEquationName(equ);
                if(name.equals("RK")){
                    Calculator c2 = new Calculator();
                    c2.prepareSubstance(
                            mEditTc2.getText().toString(),
                            mEditPc2.getText().toString(),
                            mEditZc2.getText().toString(),
                            mEditW2.getText().toString(),
                            mEditT.getText().toString(),
                            equ
                    );
                    double
                    b1 = c.get_b(),
                    b2 = c2.get_b(),
                    a1 = c.get_a(),
                    a2 = c2.get_a();
                    c.mixWith(c2,
                            (mMixMethodA.isChecked() ? Calculator.MIX_SQR : Calculator.MIX_LIN),
                            Calculator.MIX_LIN,
                            mEditY1.getText().toString(),
                            mEditY2.getText().toString()
                    );

                    c.calcMixPhiRK(
                            mEditT.getText().toString(),
                            mEditP.getText().toString(),
                            mEditVm.getText().toString(),
                            a1,a2,b1,b2,
                            edit2double(mEditY1),
                            edit2double(mEditY2),
                            mHandler, v.getId());

                }else if(name.equals("Pitzer")){
                    final View layout = getLayoutInflater().inflate(R.layout.dialog_input_b,null);
                    new AlertDialog.Builder(this).setTitle("Input").setView(layout).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                            double phi1, phi2, f1, f2;
                            double B11=edit2double((EditText)layout.findViewById(R.id.edit_B11)),
                                    B12=edit2double((EditText)layout.findViewById(R.id.edit_B12)),
                                    B22=edit2double((EditText)layout.findViewById(R.id.edit_B22));
                            double P=edit2double(mEditP), y1=edit2double(mEditY1), y2=edit2double(mEditY2);
                            double Bm = (y1*y1*B11+2*y1*y2*B12+y2*y2*B22);
                            double R=8.3144621;
                            double T=edit2double(mEditT);
                            phi1 = Math.exp(P*(2*y1*B11+2*y2*B12-Bm)/R/T);
                            phi2 = Math.exp(P*(2*y1*B12+2*y2*B22-Bm)/R/T);
                            f1 = phi1*y1*P;
                            f2 = phi2*y2*P;

                            String result =
                                    "Φ1="+phi1+"\n" +
                                            "f1="+f1+"\n" +
                                            "Φ2="+phi2+"\n" +
                                            "f2="+f2;
                            mResult.setText(result);
                        }
                    }).show();
                }else{
                    mResult.setText("该方法不支持混合物");
                }
            }else {

                c.calcPhi(equ,
                        mEditT.getText().toString(),
                        mEditP.getText().toString(),
                        mEditVm.getText().toString(),
                        mHandler, v.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mResult.setText(e.getMessage());
        }
    }

}

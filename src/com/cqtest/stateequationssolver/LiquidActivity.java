package com.cqtest.stateequationssolver;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class LiquidActivity extends Activity {
	ArrayAdapter<String> mEquationAdapter;
    SubstanceDb mSubstanceAdapter;
	@InjectView(R.id.choose_equation) Spinner mChooseEquation;
	@InjectView(R.id.choose_substance) Spinner mChooseSubstance;

    @InjectView(R.id.edit_Pc)
    EditText mEditPc;
    @InjectView(R.id.edit_Tc)
    EditText mEditTc;
    @InjectView(R.id.edit_w)
    EditText mEditW;
    @InjectView(R.id.edit_Zc)
    EditText mEditZc;
    @InjectView(R.id.edit_Vc)
    EditText mEditVc;


    @InjectView(R.id.edit_P)
    EditText mEditP;
    @InjectView(R.id.edit_T)
    EditText mEditT;
    @InjectView(R.id.edit_Vm)
    EditText mEditVm;
    @InjectView(R.id.edit_Ps)
    EditText mEditPs;
    @InjectView(R.id.edit_Vs)
    EditText mEditVs;
    @InjectView(R.id.edit_PhoS)
    EditText mEditPhoS;
    @InjectView(R.id.edit_VR)
    EditText mEditVR;
    @InjectView(R.id.edit_TR)
    EditText mEditTR;

    @InjectView(R.id.textResult)
    TextView mResult;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message.what == 1){
                gotResult(message.obj, message.arg1);
            }else if(message.what == 2){
                gotError("SyntaxError", message.obj);
            }else if(message.what == 3){
                gotError("MathException", message.obj);
            }
            return false;
        }
    });

    protected void gotError(String mathException, Object obj) {

        mResult.setText(mathException+"\n"+obj);
    }

    protected void gotResult(Object obj, int operation) {
        mResult.setText(""+obj);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liquid);

        AssetsDatabaseManager.initManager(getApplication());

        ButterKnife.inject(this);
		
		mEquationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EquationDb.getNames(EquationDb.FILTER_Liquid));
		mEquationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mChooseEquation.setAdapter(mEquationAdapter);

		mSubstanceAdapter = new SubstanceDb(this);
		mChooseSubstance.setAdapter(mSubstanceAdapter);
	}

	 @OnItemSelected(R.id.choose_equation)
	 void onEquSelected(int position) {
	   Log.v("onEquSelected", ""+position);
	 }
	 

	 @OnItemSelected(R.id.choose_substance)
	 void onSubstanceSelected(int position)
     {
         long id = mSubstanceAdapter.getItemId(position);
         Log.e("onSubstanceSelected", "pos:"+position+",id:"+id);
         double[] params = mSubstanceAdapter.getSubstanceParam(id);
         if(params != null){

             mEditTc.setText(Double.toString(params[0]));
             mEditPc.setText(Double.toString(params[1]));
             mEditW.setText(Double.toString(params[2]));
             mEditZc.setText(Double.toString(params[3]));
             mEditVc.setText(Double.toString(params[4]));
         }
	 }


	@OnClick({R.id.calc})
	public void OnParamCalc(View v) {

        try {
            int equ = EquationDb.item2equ(mChooseEquation.getSelectedItemPosition(),EquationDb.FILTER_Liquid);
            Calculator c = new Calculator();
            c.calcLiquid(
                    mEditTc.getText().toString(),
                    mEditPc.getText().toString(),
                    mEditZc.getText().toString(),
                    mEditVc.getText().toString(),
                    mEditW.getText().toString(),
                    mEditT.getText().toString(),
                    mEditVm.getText().toString(),
                    mEditP.getText().toString(),
                    mEditTR.getText().toString(),
                    mEditVR.getText().toString(),
                    mEditPs.getText().toString(),
                    mEditVs.getText().toString(),
                    mEditPhoS.getText().toString(),
                    equ,
                    mHandler
            );
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "发生异常:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

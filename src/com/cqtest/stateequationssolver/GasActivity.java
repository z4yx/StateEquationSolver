package com.cqtest.stateequationssolver;

import butterknife.ButterKnife;
import butterknife.InjectView;
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
import android.widget.ToggleButton;

public abstract class GasActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
	ArrayAdapter<String> mEquationAdapter;
    SubstanceDb mSubstanceAdapter, mSubstanceAdapter2;
	@InjectView(R.id.choose_equation) Spinner mChooseEquation;
	@InjectView(R.id.choose_substance) Spinner mChooseSubstance;
    @InjectView(R.id.choose_substance2) Spinner mChooseSubstance2;

    @InjectView(R.id.edit_Pc)
    EditText mEditPc;
    @InjectView(R.id.edit_Tc)
    EditText mEditTc;
    @InjectView(R.id.edit_w)
    EditText mEditW;
    @InjectView(R.id.edit_Zc)
    EditText mEditZc;

    @InjectView(R.id.checkSubstance2)
    CheckBox mEnable2;

    @InjectView(R.id.edit_Pc2)
    EditText mEditPc2;
    @InjectView(R.id.edit_Tc2)
    EditText mEditTc2;
    @InjectView(R.id.edit_w2)
    EditText mEditW2;
    @InjectView(R.id.edit_Zc2)
    EditText mEditZc2;

    @InjectView(R.id.mixMethodA)
    ToggleButton mMixMethodA;
    @InjectView(R.id.editY1)
    EditText mEditY1;
    @InjectView(R.id.editY2)
    EditText mEditY2;

    @InjectView(R.id.edit_P)
    EditText mEditP;
    @InjectView(R.id.edit_T)
    EditText mEditT;
    @InjectView(R.id.edit_Vm)
    EditText mEditVm;

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

    int equation_filter = -1;
    abstract protected void gotError(String mathException, Object obj);
    abstract protected void gotResult(Object obj, int operation);
    abstract protected void initContentView();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContentView();

        AssetsDatabaseManager.initManager(getApplication());

        ButterKnife.inject(this);

        mChooseSubstance2.setEnabled(false);
        mEnable2.setOnCheckedChangeListener(this);
		
		mEquationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EquationDb.getNames(equation_filter));
		mEquationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mChooseEquation.setAdapter(mEquationAdapter);

		mSubstanceAdapter = new SubstanceDb(this);
		mChooseSubstance.setAdapter(mSubstanceAdapter);

        mSubstanceAdapter2 = new SubstanceDb(this);
        mChooseSubstance2.setAdapter(mSubstanceAdapter2);

        final SolverApplication app = (SolverApplication)getApplication();
        if(app.getV() != 0)
            mEditVm.setText(String.valueOf(app.getV()));
        if(app.getP() != 0)
            mEditP.setText(String.valueOf(app.getP()));
        if(app.getT() != 0)
            mEditT.setText(String.valueOf(app.getT()));

        mEditP.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        app.setP(Double.valueOf(mEditP.getText().toString()));
                    }catch (Exception e){

                    }
                }
            }
        });

        mEditT.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        app.setT(Double.valueOf(mEditT.getText().toString()));
                    }catch (Exception e){

                    }
                }
            }
        });
        mEditVm.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        app.setV(Double.valueOf(mEditVm.getText().toString()));
                    }catch (Exception e){

                    }
                }
            }
        });
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
         }
	 }

    @OnItemSelected(R.id.choose_substance2)
    void onSubstanceSelected2(int position)
    {
        long id = mSubstanceAdapter2.getItemId(position);
        Log.e("onSubstanceSelected2", "pos:"+position+",id:"+id);
        double[] params = mSubstanceAdapter2.getSubstanceParam(id);
        if(params != null){

            mEditTc2.setText(Double.toString(params[0]));
            mEditPc2.setText(Double.toString(params[1]));
            mEditW2.setText(Double.toString(params[2]));
            mEditZc2.setText(Double.toString(params[3]));
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId() == mEnable2.getId()){
            mEditPc2.setEnabled(b);
            mEditTc2.setEnabled(b);
            mEditZc2.setEnabled(b);
            mEditW2.setEnabled(b);
            mChooseSubstance2.setEnabled(b);
            findViewById(R.id.mixOptions).setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        }
    }


}

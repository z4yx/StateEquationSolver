package com.cqtest.stateequationssolver;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
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

    @InjectView(R.id.edit_P)
    EditText mEditP;
    @InjectView(R.id.edit_T)
    EditText mEditT;
    @InjectView(R.id.edit_Vm)
    EditText mEditVm;

    @InjectView(R.id.mixMethodA)
    ToggleButton mMixMethodA;
    @InjectView(R.id.editY1)
    EditText mEditY1;
    @InjectView(R.id.editY2)
    EditText mEditY2;

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

    private void gotError(String mathException, Object obj) {
        mResult.setText(mathException+"\n"+obj);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        AssetsDatabaseManager.initManager(getApplication());

        ButterKnife.inject(this);

        mChooseSubstance2.setEnabled(false);
        mEnable2.setOnCheckedChangeListener(this);
		
		mEquationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EquationDb.getNames());
		mEquationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mChooseEquation.setAdapter(mEquationAdapter);

		mSubstanceAdapter = new SubstanceDb(this);
		mChooseSubstance.setAdapter(mSubstanceAdapter);

        mSubstanceAdapter2 = new SubstanceDb(this);
        mChooseSubstance2.setAdapter(mSubstanceAdapter2);
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
            int equ = mChooseEquation.getSelectedItemPosition();
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
            Toast.makeText(this, "发生异常:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void gotResult(Object obj, int operation) {
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
        for(String v : values){
            if(v.contains("I"))
                continue;
            v=v.trim();
            finalResult += v + "\n";
        }
        mResult.setText(finalResult);
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

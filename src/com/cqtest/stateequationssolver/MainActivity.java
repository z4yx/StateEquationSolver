package com.cqtest.stateequationssolver;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity implements EquationParams.ParamListListener {
	ArrayAdapter<String> mEquationAdapter, mSubstanceAdapter;
	EquationParams mParamsAdapter;
	@InjectView(R.id.choose_equation) Spinner mChooseEquation;
	@InjectView(R.id.choose_substance) Spinner mChooseSubstance;
	@InjectView(R.id.param_list) ListView mParamList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ButterKnife.inject(this);
		
		mEquationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EquationDb.getNames());
		mEquationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mChooseEquation.setAdapter(mEquationAdapter);

		mSubstanceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SubstanceDb.getNames());
		mSubstanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mChooseSubstance.setAdapter(mSubstanceAdapter);
		
		mParamsAdapter = new EquationParams(this, this);
		mParamList.setAdapter(mParamsAdapter);
	}

	 private void evaluate(String equation, String unknown)
	    {
	        try{
	        	IExpr result;
	            EvalUtilities util = new EvalUtilities(false, true);
	            util.evaluate("R=8.3144621");
	            util.evaluate("Tc=-118.57+273.15");
                util.evaluate("P=10^5");
                util.evaluate("T=30+273.15");
	            result = util.evaluate("Pc=5.0426*10^6");
	            Log.v("evaluate", "Pc "+ result.toString());
	            equation = "NSolve({" + equation + "},{" + unknown +"})";
                Log.v("evaluate", "equ: " + equation);
	            result = util.evaluate(equation);
	            Log.v("evaluate", result.toString()+"");
	        } catch (SyntaxError e) {
	            // catch Symja parser errors here
	            Log.e("evaluate", "SyntaxError|" + e.getMessage());
	        } catch (MathException me) {
	            // catch Symja math errors here
	            Log.e("evaluate", "MathException|" + me.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	 @OnItemSelected(R.id.choose_equation)
	 void onEquSelected(int position) {
	   Log.v("onEquSelected", ""+position);
	 }
	 

	 @OnItemSelected(R.id.choose_substance)
	 void onSubstanceSelected(int position) {
	   Log.e("onSubstanceSelected", ""+position);
	 }

	@Override
	public void OnParamCalc(int param) {
		evaluate(EquationDb.getEquation(mChooseEquation.getSelectedItemPosition()), mParamsAdapter.getParamName(param));
	}
}

package com.cqtest.stateequationssolver;

import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import android.content.Context;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class EquationParams extends BaseAdapter {
	
	ParamListListener mListener;
	LayoutInflater mInflater;
	String[] parameters = new String[] { "P", "T", "Vm" };
	double[] values = new double[] { 0.0, 0.0, 0.0};

	public EquationParams(Context context, ParamListListener listener) {
		super();
		mInflater = LayoutInflater.from(context);
		mListener = listener;
	}
	
	public String getParamName(int i) {
		return parameters[i];
	}

	@Override
	public int getCount() {
		return parameters.length;
	}

	@Override
	public Object getItem(int arg0) {
		return values[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.param_item, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.listener = mListener;
		holder.position = position;
		holder.name.setText(parameters[position]);
		holder.value.setText(String.valueOf(values[position]));
		return convertView;
	}
	
	public interface ParamListListener{
		public void OnParamCalc(int position);
	}

	static class ViewHolder {
	    @InjectView(R.id.param_name) TextView name;
	    @InjectView(R.id.param_value) EditText value;
	    int position;
	    ParamListListener listener;

	    public ViewHolder(View view) {
	      ButterKnife.inject(this, view);
	    }
	    
	    @OnClick(R.id.param_calc)
	    public void calc(){
	    	Log.v("calc","param "+position+" calc");
	    	listener.OnParamCalc(position);
	    }
	  }
}

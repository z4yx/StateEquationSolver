package com.cqtest.stateequationssolver;

import android.util.Log;

/**
 * Created by zhang on 14-12-27.
 */
public class ActivityCoefficientActivity extends WilsonBubbleActivity {

    @Override
    protected void initContentView() {
        equation_filter=EquationDb.FILTER_STATE;
        setContentView(R.layout.activity_activity_coefficient);

    }

    @Override
    protected boolean preStartCalcThread(double[] tc, double[] pc, double[] w, double[] aa, double[] bb, double[] c, double[] a12, double p, double x1, LiquidTheory calcGama) {
        double gama[] = new double[3];
        double x[] = new double[]{0, x1, 1-x1};
        calcGama.calc_gama(gama,Double.valueOf(mEditT.getText().toString()),x,a12);
        Log.v("gama",gama[1]+","+gama[2]);
        mResult.setText("γ1="+gama[1]+"\nγ2="+gama[2]);
        return true;
    }
}

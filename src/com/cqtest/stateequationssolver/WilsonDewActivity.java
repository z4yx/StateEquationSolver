package com.cqtest.stateequationssolver;

/**
 * Created by zhang on 14-12-25.
 */
public class WilsonDewActivity extends WilsonBubbleActivity {

    @Override
    protected void initContentView() {
        equation_filter=EquationDb.FILTER_STATE;
        setContentView(R.layout.activity_wilson_dew);
    }

    @Override
    protected double calc_yi(double xi,double gi,double pi,double fi,double p,double fiv)
    {
        return xi*1/(gi*pi*fi/(p*fiv));
    }
}

package com.cqtest.stateequationssolver;

import android.app.Application;

/**
 * Created by zhang on 14-12-16.
 */
public class SolverApplication extends Application {
    double T=0,P=0,V=0;

    void StoreValue(double t, double p, double v)
    {
        T=t;
        P=p;
        V=v;
    }

    public double getT() {
        return T;
    }

    public void setT(double t) {
        T = t;
    }

    public double getP() {
        return P;
    }

    public void setP(double p) {
        P = p;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }
}

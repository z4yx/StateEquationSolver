package com.cqtest.stateequationssolver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhang on 14-12-21.
 */
public class WilsonBubbleActivity extends GasActivity {


    @InjectView(R.id.textResult)
    TextView mResult;

    @InjectView(R.id.edit_a1)
    EditText mEdit_a1;
    @InjectView(R.id.edit_a2)
    EditText mEdit_a2;
    @InjectView(R.id.edit_X)
    EditText mEdit_X;


    @InjectView(R.id.edit_AntoineA)
    EditText mEdit_A;
    @InjectView(R.id.edit_AntoineB)
    EditText mEdit_B;
    @InjectView(R.id.edit_AntoineC)
    EditText mEdit_C;
    @InjectView(R.id.edit_AntoineA2)
    EditText mEdit_A2;
    @InjectView(R.id.edit_AntoineB2)
    EditText mEdit_B2;
    @InjectView(R.id.edit_AntoineC2)
    EditText mEdit_C2;

    @InjectView(R.id.choose_liquid_gama)
    Spinner mChooseGamaMethod;
    ArrayAdapter<String> mChooseGamaAdapter;

    @Override
    protected void gotError(String mathException, Object obj) {
        mResult.setText((String)obj);
    }

    @Override
    protected void gotResult(Object obj, int operation) {
        mResult.setText((String)obj);
    }

    @Override
    protected void initContentView() {
        equation_filter=EquationDb.FILTER_STATE;
        setContentView(R.layout.activity_wilson_bubble);

    }

    @Override
    protected void postActivityCreate() {
        mEnable2.setChecked(true);
        findViewById(R.id.mixOptions).setVisibility(View.GONE);

        mChooseGamaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mGamaMethodNames);
        mChooseGamaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseGamaMethod.setAdapter(mChooseGamaAdapter);
    }

    @Override
    protected void postSubstanceSelected(String name, boolean substance2) {
        Log.v("postSubstanceSelected", "name: "+name);
        double[] result = SubstanceDb.getSubstanceAntoine(name);
        if(substance2){
            if(result != null) {
                mEdit_A2.setText(String.valueOf(result[0]));
                mEdit_B2.setText(String.valueOf(result[1]));
                mEdit_C2.setText(String.valueOf(result[2]));
            }else{
                mEdit_A2.setText("");
                mEdit_B2.setText("");
                mEdit_C2.setText("");
            }
        }else{
            if(result != null) {
                mEdit_A.setText(String.valueOf(result[0]));
                mEdit_B.setText(String.valueOf(result[1]));
                mEdit_C.setText(String.valueOf(result[2]));
            }else{
                mEdit_A.setText("");
                mEdit_B.setText("");
                mEdit_C.setText("");
            }
        }
    }

    private void fetchParams(double tc[],double pc[],double w[] ,double aa[],double bb[],double c[],double a12[])
    {
        tc[1] = Double.valueOf(mEditTc.getText().toString());
        tc[2] = Double.valueOf(mEditTc2.getText().toString());
        pc[1] = Double.valueOf(mEditPc.getText().toString());
        pc[2] = Double.valueOf(mEditPc2.getText().toString());
        w[1] = Double.valueOf(mEditW.getText().toString());
        w[2] = Double.valueOf(mEditW2.getText().toString());
        aa[1] = Double.valueOf(mEdit_A.getText().toString());
        aa[2] = Double.valueOf(mEdit_A2.getText().toString());
        bb[1] = Double.valueOf(mEdit_B.getText().toString());
        bb[2] = Double.valueOf(mEdit_B2.getText().toString());
        c[1] = Double.valueOf(mEdit_C.getText().toString());
        c[2] = Double.valueOf(mEdit_C2.getText().toString());

        a12[1]=Double.valueOf(mEdit_a1.getText().toString());
        a12[2]=Double.valueOf(mEdit_a2.getText().toString());
    }

    protected boolean preStartCalcThread(double tc[],double pc[],double w[] ,double aa[],double bb[],double c[],double a12[], double p,double x1,LiquidTheory calcGama)
    {
        return false;
    }

    @OnClick(R.id.calc_Y)
    protected void onClickCalc(View v){
        try {
            final double tc[] = new double[3], pc[] = new double[3], w[] = new double[3] ,
                    aa[] = new double[3], bb[]  = new double[3], c[] = new double[3];
//            double a1, a2,  p, x1;
            final double a12[] = new double[3];
            final LiquidTheory method = mGamaClasses[mChooseGamaMethod.getSelectedItemPosition()];
            fetchParams(tc,pc,w,aa,bb,c,a12);
            method.require_params(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mResult.setText("计算中...");
                    if(!preStartCalcThread(
                        tc,pc,w,aa,bb,c,
                        a12,
                        Double.valueOf(mEditP.getText().toString()),
                        Double.valueOf(mEdit_X.getText().toString()),
                        method))
                    {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                double T = 0, y[] = new double[3];
                                try {
                                    T = calcWilson(
                                            tc, pc, w, aa, bb, c,
                                            a12,
                                            Double.valueOf(mEditP.getText().toString()),
                                            Double.valueOf(mEdit_X.getText().toString()),
                                            method,
                                            y
                                    );
                                    mHandler.obtainMessage(1, String.format("T=%.6f    %.6f:%.6f", T, y[1],y[2])).sendToTarget();

                                    //                                mResult.setText("T=" + T);
                                } catch (Exception e) {
                                    mHandler.obtainMessage(3, e.getMessage()).sendToTarget();
                                    //                                mResult.setText(e.getMessage());
                                }

                            }
                        }).start();
                    }
                }
            });
        }catch (Exception e){
            mResult.setText(e.getMessage());
        }
    }

    double calcAntoine(double t, double A,double B,double C){
        if(A>0){
            return Math.pow(10, A - B / (t - 273.15 + C)) / 760 * 101325;
        }else{
            return Math.pow(10,-52.23*B/t+C)/ 760 * 101325;
        }
    }

    private double calcWilson(double tc[],double pc[],double w[] ,double aa[],double bb[],double c[],double a12[], double p,double x1,LiquidTheory calcGama,double y[]) throws Exception {
        double r=8.314, t = 200;
//        p=101325;
//        x1=0.2;
//        tc[1]=516.2;tc[2]=647.3;
//        pc[1]=6.383e6;pc[2]=2.205e7;
//        w[1]=0.635;w[2]=0.344;
//        aa[1]=8.04494;aa[2]=7.96681;
//        bb[1]=1554.3;bb[2]=1668.21;
//        c[1]=222.65;c[2]=228.0;
//        a1=0.08417;
//        a2=0.95143;
        for(int i=1;i<=2;i++){
            Log.v("dump", "tc[]=" + tc[i]);
            Log.v("dump", "pc[]=" + pc[i]);
            Log.v("dump", "w[]=" + w[i]);
            Log.v("dump", "aa[]=" + aa[i]);
            Log.v("dump", "bb[]=" + bb[i]);
            Log.v("dump", "c[]=" + c[i]);
            Log.v("dump", "a12[]=" + a12[i]);
        }
        Log.v("dump", "p=" + p);
        Log.v("dump", "x1=" + x1);

        double tr[] = new double[3], pr[] = new double[3], fisat[] = new double[3],
                fiv[] = new double[3],psat[] = new double[3], b[] = new double[3],
                f0[] = new double[3], f1[] = new double[3], gama[] = new double[3],
                x[] = new double[3];
        do {
            double bm;
            t = t + 0.001;
            x[1] = x1;
            x[2] = 1 - x1;
            for (int i = 1; i <= 2; i++) {
                tr[i] = t / tc[i];
                pr[i] = p / pc[i];
                fisat[i] = 1;
                fiv[i] = 1;
                //初值
                f0[i] = 0.1445 - 0.330 / tr[i] - 0.1385 / Math.pow(tr[i], 2) - 0.0121 / Math.pow(tr[i], 3) - 0.000607 / Math.pow(tr[i], 8);
                f1[i] = 0.0637 + 0.331 / Math.pow(tr[i], 2) - 0.423 / Math.pow(tr[i], 3) - 0.008 / Math.pow(tr[i], 8);
                b[i] = r * tc[i] / pc[i] * (f0[i] + w[i] * f1[i]);
            }
            //计算B
            bm = Math.pow(x1, 2) * b[1] + 2 * x[1] * x[2] * Math.pow(Math.sqrt(b[1] * b[2]), 2) + Math.pow(x[2], 2) * b[2];
            for (int i = 1; i <= 2; i++) {
                psat[i] = calcAntoine(t,aa[i],bb[i],c[i]); // aa / bb为Antoine公式中A / B
                //计算饱和蒸汽压
                fisat[i] = Math.exp(b[i] * psat[i] / (r * t));
                // printf("%lf %lf %lf\n", b[i], psat[i], fisat[i]);

                fiv[i] = Math.exp((2 * b[i] * x[i] - bm) * p / (r * t));
            }
            calcGama.calc_gama(gama,t,x,a12);
            //计算逸度系数
//            double x2 = 1-x1;
//            gama[1] = Math.exp(x2 * (a1 / (x1 + x2 * a1) - a2 / (x2 + x1 * a2)) - Math.log(x1 + x2 * a1));
//            gama[2] = Math.exp(x1 * (a2 / (x2 + x1 * a2) - a1 / (x1 + x2 * a1)) - Math.log(x2 + x1 * a2));
            //计算活度
            for (int i = 1; i <= 2; i++) {
                // printf("%lf %lf %lf %lf\n", gama[i], psat[i], fisat[i], fiv[i]);
                y[i] = calc_yi(x[i] , gama[i] , psat[i] , fisat[i] , p , fiv[i]);
//                y[i] = x[i] * (gama[i] * psat[i] * fisat[i] / (p * fiv[i]));
            }
            // printf("%lf %lf\n", y[1], y[2]);
            //计算y
            double yy = Math.abs(1 - y[1] - y[2]);
            if ((yy <= 0.001) && (y[1] < 1) && (y[2] < 1)) {
                //printf("T=%f\n,y1=%f\n,y2=%f\n", t-273.15, y[1], y[2]);
                return t-273.15;
            }
        } while (t < 800);
        throw new Exception("No answer found!");
    }

    protected double calc_yi(double xi,double gi,double pi,double fi,double p,double fiv)
    {
        return xi*(gi*pi*fi/(p*fiv));
    }

    protected String mGamaMethodNames[] = new String[]{"S-H","Van Laar","Margules","Symmetric","Flory Huggins","Wilson","NRTL"};
    protected LiquidTheory mGamaClasses[] = new LiquidTheory[]{
            new S_H(),
            new Van_Laar(),
            new Margules(),
            new Symmetric(),
            new Flory_Huggins(),
            new Wilson(),
            new NRTL()
    };

    abstract class LiquidTheory{
        final double r = 8.314;
        protected void require_params(final DialogInterface.OnClickListener done){
            done.onClick(null,0);
        }
        abstract protected void calc_gama(double gama[],double t,double x[],double a[]);
    }
    class S_H extends LiquidTheory{
        double v[] = new double[3],
                dlt[] = new double[3];
        @Override
        protected void require_params(final DialogInterface.OnClickListener done) {

            final View layout = getLayoutInflater().inflate(R.layout.dialog_input_h_dash_s,null);
            new AlertDialog.Builder(WilsonBubbleActivity.this).setTitle("Input").setView(layout).setPositiveButton("确定",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    v[1] = Double.valueOf(((EditText)layout.findViewById(R.id.edit_V1)).getText().toString());
                    v[2] = Double.valueOf(((EditText)layout.findViewById(R.id.edit_V2)).getText().toString());
                    dlt[1] = Double.valueOf(((EditText)layout.findViewById(R.id.edit_Dlt1)).getText().toString());
                    dlt[2] = Double.valueOf(((EditText)layout.findViewById(R.id.edit_Dlt2)).getText().toString());

                    done.onClick(dialogInterface,i);
                }
            })
                    .setNegativeButton("取消",null)
                   .show();
        }

        @Override
        protected void calc_gama(double[] gama, double t, double[] x, double[] a) {
            for(int i=1;i<=2;i++)
                gama[i]=Math.exp(v[i]*Math.pow(dlt[1]-dlt[2],2)/ r /t*Math.pow(x[i]*v[i]/(x[1]*v[1]+x[2]*v[2]),2));
        }
    }
    class Van_Laar extends LiquidTheory{
        @Override
        protected void calc_gama(double[] gama, double t, double[] x, double[] a) {
            for(int i=1;i<=2;i++)
                gama[i]=Math.exp(a[3-i]/Math.pow(1+a[3-i]/a[i]*x[i]/x[3-i],2));
        }
    }
    class Margules extends LiquidTheory{
        @Override
        protected void calc_gama(double[] gama, double t, double[] x, double[] a) {
            for(int i=1;i<=2;i++)
                gama[i]=Math.exp(x[3-i]*x[3-i]*(a[3-i]+2*x[i]*(a[i]-a[3-i])));
        }
    }
    class Symmetric extends LiquidTheory{
        @Override
        protected void calc_gama(double[] gama, double t, double[] x, double[] a) {
            for(int i=1;i<=2;i++)
                gama[i]=a[3-i]*x[3-i]*x[3-i];
        }
    }
    class Flory_Huggins extends LiquidTheory{
        double v[] = new double[3];
        @Override
        protected void require_params(final DialogInterface.OnClickListener done) {

            final View layout = getLayoutInflater().inflate(R.layout.dialog_input_floryhuggins,null);
            new AlertDialog.Builder(WilsonBubbleActivity.this).setTitle("Input").setView(layout).setPositiveButton("确定",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    v[1] = Double.valueOf(((EditText)layout.findViewById(R.id.edit_V1)).getText().toString());
                    v[2] = Double.valueOf(((EditText)layout.findViewById(R.id.edit_V2)).getText().toString());

                    done.onClick(dialogInterface,i);
                }
            })
                    .setNegativeButton("取消",null)
                    .show();
        }
        @Override
        protected void calc_gama(double[] gama, double t,  double[] x, double[] a) {
            for(int i=1;i<=2;i++)
                gama[i]=Math.exp(Math.log((x[i] * v[i] / (x[1] * v[1] + x[2] * v[2])) / x[i]) + 1 - (x[i] * v[i] / (x[1] * v[1] + x[2] * v[2])) / x[i]);
        }
    }
    class Wilson extends LiquidTheory{
        @Override
        protected void calc_gama(double[] gama, double t, double[] x, double[] a) {
            for(int i=1;i<=2;i++)
                gama[i] = Math.exp(x[3-i] * (a[i] / (x[i] + x[3-i] * a[i]) - a[3-i] / (x[3-i] + x[i] * a[3-i])) - Math.log(x[i] + x[3-i] * a[i]));
        }
    }
    class NRTL extends LiquidTheory{
        double tau[] = new double[3],
                G[] = new double[3],
                alpha;

        @Override
        protected void require_params(final DialogInterface.OnClickListener done) {

            final View layout = getLayoutInflater().inflate(R.layout.dialog_input_nrtl,null);
            new AlertDialog.Builder(WilsonBubbleActivity.this).setTitle("Input").setView(layout).setPositiveButton("确定",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tau[1] = Double.valueOf(((EditText)layout.findViewById(R.id.edit_tau1)).getText().toString());
                    tau[2] = Double.valueOf(((EditText)layout.findViewById(R.id.edit_tau2)).getText().toString());
                    alpha = Double.valueOf(((EditText)layout.findViewById(R.id.edit_alpha12)).getText().toString());

                    done.onClick(dialogInterface,i);
                }
            })
                    .setNegativeButton("取消",null)
                    .show();
        }

        @Override
        protected void calc_gama(double[] gama, double t,  double[] x, double[] a) {
            for(int i=1;i<=2;i++)
                G[i]=Math.exp(-alpha*tau[i]);
            for(int i=1;i<=2;i++)
                gama[i]=Math.exp(x[3-i]*x[3-i]*(tau[i]*G[i]*G[i]/Math.pow(x[i]+x[3-i]*G[i],2)+tau[3-i]*G[3-i]/Math.pow(x[3-i]+x[i]*G[3-i],2)));
        }
    }
}

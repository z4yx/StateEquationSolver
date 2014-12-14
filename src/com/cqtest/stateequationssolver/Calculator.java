package com.cqtest.stateequationssolver;

import android.os.Handler;
import android.util.Log;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.eval.EvalDouble;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.math.MathException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhang on 14-11-9.
 */
public class Calculator {

    final static public int MIX_LIN = 1, MIX_SQR = 2;

    static Pattern mResultPattern = Pattern.compile("([a-zA-z]+)->([^,}]+)");

    double a, b, Zc, Tc;
    public void prepareSubstance(String Tc, String Pc, String Zc, String w, String T, int equation){
        String fa = EquationDb.getFormulaA(equation);
        String fb = EquationDb.getFormulaB(equation);

        String Tr = "((" + T + ")/(" + Tc + "))";

        this.Tc = Double.valueOf(Tc);
        this.Zc = Double.valueOf(Zc);

        try {
            EvalDouble util = new EvalDouble(false);
//            EvalUtilities util = new EvalUtilities(false, true);

            util.evaluate("R=8.3144621");
            util.evaluate("Tc="+Tc);
            util.evaluate("Pc="+Pc);
            util.evaluate("Tr="+Tr);
            util.evaluate("w="+w);

            if(fa == null)
                a = 0;
            else
                a = util.evaluate(fa);

            if(fb == null)
                b = 0;
            else
                b = util.evaluate(fb);

            Log.v("double", "a="+a+",b="+b);

        }catch (SyntaxError e) {
            // catch Symja parser errors here
            Log.e("evaluate", "SyntaxError|" + e.getMessage());
        } catch (MathException me) {
            // catch Symja math errors here
            Log.e("evaluate", "MathException|" + me.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calcHS(final String T, final String P, final String V ,final String _HorSid, final String _P0, boolean isH, final Handler handler, final int operation)
    {
        String result = new String();
        try{
            DoubleEvaluator util = new DoubleEvaluator(false);
            util.clearVariables();
            util.evaluate("R=8.3144621");
            util.evaluate("T="+T);
            util.evaluate("P="+P);
            util.evaluate("V="+V);
            util.evaluate("a="+a);
            util.evaluate("b="+b);
//            double A = util.evaluate("(a/(R^2*T^2.5))^0.5");
//            double B = util.evaluate("b/(R*T)");

            util.evaluate("A=(a/(R^2*T^2.5))^0.5");
            util.evaluate("B=b/(R*T)");
            if(isH) {
                double HR = (util.evaluate("R*T*(P*V/R/T-1-3*A^2*Log[1+B*P/(P*V/R/T)]/(2*B))"));
                result += "HR=" + HR + "\n";
                String Hid = _HorSid.trim();
                if (!Hid.isEmpty()) {
                    try {
                        double Hid_d = Double.valueOf(Hid);
                        result += "H=" + (Hid_d + HR);
                    } catch (Exception e) {

                    }
                }
            }else{
                double SR = (util.evaluate("R*(-A^2/(2*B)*Log[1+B*P/(P*V/R/T)]+Log[P*V/R/T-B*P])"));
                result += "SR=" + SR + "\n";
                String Sid = _HorSid.trim();
                String P0 = _P0.trim();
                if (!Sid.isEmpty() && !P0.isEmpty()) {
                    try {
                        double Sid_d = Double.valueOf(Sid);
                        double P0_d = Double.valueOf(P0);
                        util.evaluate("P0="+P0_d);
                        result += "S=" + util.evaluate((Sid_d + SR)+"-R*Log[P/P0]");
                    } catch (Exception e) {

                    }
                }

            }
            handler.obtainMessage(1, operation, 0, result).sendToTarget();
        }catch (SyntaxError e) {
            // catch Symja parser errors here
            Log.e("evaluate", "SyntaxError|" + e.getMessage());
            handler.obtainMessage(2, operation, 0, e.getMessage()).sendToTarget();
        } catch (MathException me) {
            // catch Symja math errors here
            Log.e("evaluate", "MathException|" + me.getMessage());
            handler.obtainMessage(3, operation, 0, me.getMessage()).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void solveEquation(final int equation, final String unknown, final String known, final String T, final Handler handler, final int operation)
    {
        Thread calc = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.v("solveEquation", "unknown: "+unknown+",known: "+known);


                    IExpr result;
                    EvalUtilities util = new EvalUtilities(false, true);
                    util.evaluate("R=8.3144621");
                    util.evaluate("T="+T);
//                    util.evaluate(known);
                    util.evaluate("a="+a);
                    util.evaluate("b="+b);
                    String stmt = "NSolve({" + EquationDb.getEquation(equation) + ","+ known +"},{" + unknown +"})";
                    Log.v("evaluate", "equ: " + stmt);
                    result = util.evaluate(stmt);

                    String strResult = result.toString();
                    Log.v("evaluate", strResult);

                    Matcher m = mResultPattern.matcher(strResult);
                    HashMap<String, ArrayList<String>> collect = new HashMap<String, ArrayList<String>>();
                    while(m.find()){
                        String name = m.group(1).toLowerCase(), value = m.group(2);
                        Log.v("match", "name: "+name+", value:"+value);
                        if(collect.containsKey(name)){
                            collect.get(name).add(value);
                        }else{
                            ArrayList<String> list = new ArrayList<String>(1);
                            list.add(value);
                            collect.put(name, list);
                        }
                    }

                    handler.obtainMessage(1, operation, 0, collect).sendToTarget();
                } catch (SyntaxError e) {
                    // catch Symja parser errors here
                    Log.e("evaluate", "SyntaxError|" + e.getMessage());
                    handler.obtainMessage(2, operation, 0, e.getMessage()).sendToTarget();
                } catch (MathException me) {
                    // catch Symja math errors here
                    Log.e("evaluate", "MathException|" + me.getMessage());
                    handler.obtainMessage(3, operation, 0, me.getMessage()).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        calc.run();
    }

    private double calcMix(double q1, double q2, int type, double tc1, double tc2, double zc1, double zc2, double y1, double y2) throws Exception {
        if(type == MIX_LIN){
            double r = (y1*q1+y2*q2)/(y1+y2);
            Log.i("calcMix","r="+r);
            return r;
        }else if(type == MIX_SQR){
            double z = (zc1+zc2)/2;
            double k = Math.pow( (tc1*tc2/(tc1+tc2)*2),z);
            Log.i("calcMix","z="+z+",k="+k);
            return y1*y1*q1+2*y1*y2*Math.sqrt(q1*q2)*k+y2*y2*q2;
        }else{
            throw new Exception("Wrong type");
        }
    }

    public void mixWith(Calculator c2, int type_a, int type_b, String y1, String y2) throws Exception {
        Log.d("mixWith", "type_a="+type_a+",type_b="+type_b+",y1="+y1+",y2="+y2);
        double Y1 = Double.valueOf(y1);
        double Y2 = Double.valueOf(y2);
        a = calcMix(a, c2.a, type_a, Tc, c2.Tc, Zc, c2.Zc, Y1, Y2);
        b = calcMix(b, c2.b, type_b, Tc, c2.Tc, Zc, c2.Zc, Y1, Y2);
    }
}

package com.cqtest.stateequationssolver;

import android.os.Handler;
import android.util.Log;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.eval.EvalDouble;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhang on 14-11-9.
 */
public class Calculator {

    static Pattern mResultPattern = Pattern.compile("([a-zA-z]+)->([^,}]+)");

    double a, b;
    public void prepareSubstance(String Tc, String Pc, String w, String T, int equation){
        String fa = EquationDb.getFormulaA(equation);
        String fb = EquationDb.getFormulaB(equation);

        String Tr = "((" + T + ")/(" + Tc + "))";

        try {
            EvalDouble util = new EvalDouble(false);
//            EvalUtilities util = new EvalUtilities(false, true);

            util.evaluate("R=8.3144621");
            util.evaluate("Tc="+Tc);
            util.evaluate("Pc="+Pc);
            util.evaluate("Tr="+Tr);
            util.evaluate("w="+Tr);

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
                    handler.obtainMessage(2, operation, 0, e.getMessage());
                } catch (MathException me) {
                    // catch Symja math errors here
                    Log.e("evaluate", "MathException|" + me.getMessage());
                    handler.obtainMessage(3, operation, 0, me.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        calc.run();
    }
}

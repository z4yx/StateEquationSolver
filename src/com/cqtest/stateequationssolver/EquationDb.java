package com.cqtest.stateequationssolver;

import java.util.ArrayList;

public class EquationDb {
    public final static int FILTER_STATE = 0;
    public final static int FILTER_Fugacity = 1;
	
	private static String names[] = {
		"Van del Waals",
		"RK",
		"SRK",
        "PR",
        "Pitzer",
        "Improved Pitzer"
	};

    private static int f_Fugacity[] = {1,2,3,4};
    private static int f_State[] = {0,1,2,3,4,5};

	private static String equations[] = {
		"(P+a/Vm^2)*(Vm-b)==R*T",
		"P==R*T/(Vm-b)-a/(T^0.5*Vm*(Vm+b))",
		"P==R*T/(Vm-b)-a/(Vm*(Vm+b))",
        "P==R*T/(Vm-b)-a/(Vm*(Vm+b)+b*(Vm-b))",
        "Vm==(1+b*P/(R*T))*R*T/P",
        "Vm==(1+b*P/(R*T))*R*T/P"
	};

    private  static String param_phi[] = {
            null,
            "Exp[(Z-1)-Log[Z]-a/(b*R*T^1.5)*Log[1+b/V]-Log[1-b/V]]",
            "Exp[Z-1-Log[Z*(1-b/V)]-a/(b*R*T)*Log[1+b/V]]",
            "Exp[Z-1-Log[Z-b*P/R/T]-(a*P/R^2/T^2)/(2*1.414*b*P/R/T)*Log[(Z+2.414*b*P/R/T)/(Z-2.414*b*P/R/T)]]",
            "Exp[b*P/R/T]",
            null
    };

    private static String param_a[] = {
            "27/64*(R*Tc)^2/Pc",
            "0.42748*R^2*Tc^2.5/Pc",
            "0.42748*R^2*Tc^2/Pc*(1+(0.480+1.574*w-0.176*w^2)*(1-Tr^0.5))^2",
            "0.45724*R^2*Tc^2/Pc*(1+(0.37646+1.54226*w-0.26992*w^2)*(1-Tr^0.5))^2",
            null,
            null
    };
    private static String param_b[] = {
            "(R*Tc)/(8*Pc)",
            "0.08664*R*Tc/Pc",
            "0.08664*R*Tc/Pc",
            "0.07780*R*Tc/Pc",
            "R*Tc/Pc*(0.083-0.422/Tr^1.6+w*(0.139-0.172/Tr^4.2))",
            "R*Tc/Pc*(0.1445-0.330/Tr-0.1385/Tr^2-0.0121/Tr^3-0.000607/Tr^8+w*(0.139+0.331/Tr^2-0.423/Tr^3-0.008/Tr^8))"
    };
	
	public static final String[] getNames(int filter)
	{
		if(filter == FILTER_STATE){
            String[] l = new String[f_State.length];
            for (int i=0; i<f_State.length; i++)
                l[i] = names[f_State[i]];
            return l;
        }else if(filter == FILTER_Fugacity){
            String[] l = new String[f_Fugacity.length];
            for (int i=0; i<f_Fugacity.length; i++)
                l[i] = names[f_Fugacity[i]];
            return l;
        }
        return null;
	}

    public static final int item2equ(int item, int filter)
    {
        if(filter == FILTER_STATE)
            return f_State[item];
        else if(filter == FILTER_Fugacity)
            return f_Fugacity[item];
        return -1;
    }

    public static final String getEquationName(int i) { return names[i]; }
	
	public static final String getEquation(int i)
	{
		return equations[i];
	}

    public static final String getFormulaA(int i)
    {
        return param_a[i];
    }

    public static final String getFormulaB(int i)
    {
        return param_b[i];
    }

    public static final String getFormulaPhi(int i)
    {
        return param_phi[i];
    }
}

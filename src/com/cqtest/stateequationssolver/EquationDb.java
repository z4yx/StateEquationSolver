package com.cqtest.stateequationssolver;

import java.util.ArrayList;

public class EquationDb {
    public final static int FILTER_STATE = 0;
    public final static int FILTER_Fugacity = 1;
    public final static int FILTER_Liquid = 2;
	
	private static String names[] = {
		"Van del Waals",
		"RK",
		"SRK",
        "PR",
        "Pitzer",
        "Improved Pitzer",
        "Rackett",
        "Yamada-Gunn",
        "Thomson-Brobst-Hankinson",
        "Chueh-Prausnitz"
	};

    private static int f_Fugacity[] = {1,2,3,4};
    private static int f_State[] = {0,1,2,3,4,5};
    private static int f_Liquid[] = {6,7,8,9};

	private static String equations[] = {
		"(P+a/Vm^2)*(Vm-b)==R*T",
		"P==R*T/(Vm-b)-a/(T^0.5*Vm*(Vm+b))",
		"P==R*T/(Vm-b)-a/(Vm*(Vm+b))",
        "P==R*T/(Vm-b)-a/(Vm*(Vm+b)+b*(Vm-b))",
        "Vm==(1+b*P/(R*T))*R*T/P",
        "Vm==(1+b*P/(R*T))*R*T/P",
        "Vc*Zc^((1-Tr)^(2/7))",
        "VR*(0.29056-0.08775*w)^((1-Tr)^(2/7)-(1-TrR)^(2/7))",
        "Vs*(1-(0.0861488+0.0344483*w)*Log[1+(P-Ps)/(Ps+Pc*(-1-9.070217*(T-Tr)^(1/3)+62.45326*(1-Tr)^(2/3)-135.1102*(1-Tr)+Exp[4.79594+0.250047*w+1.14188*w^2]*(1-Tr)^(4/3)))])",
        "PhoS*(1+9*Zc*((1-0.89*w)*(Exp[6.9547]-76.2853*Tr+191.3060*Tr^2-203.5472*Tr^3+82.7631*Tr^4))*(P-Ps)/Pc)"
	};

    private  static String param_phi[] = {
            null,
            "Exp[(Z-1)-Log[Z]-a/(b*R*T^1.5)*Log[1+b/V]-Log[1-b/V]]",
            "Exp[Z-1-Log[Z*(1-b/V)]-a/(b*R*T)*Log[1+b/V]]",
            "Exp[Z-1-Log[Z-b*P/R/T]-(a*P/R^2/T^2)/(2*1.414*b*P/R/T)*Log[(Z+2.414*b*P/R/T)/(Z-2.414*b*P/R/T)]]",
            "Exp[b*P/R/T]",
            null,
            null,
            null,
            null,
            null
    };

    private static String param_a[] = {
            "27/64*(R*Tc)^2/Pc",
            "0.42748*R^2*Tc^2.5/Pc",
            "0.42748*R^2*Tc^2/Pc*(1+(0.480+1.574*w-0.176*w^2)*(1-Tr^0.5))^2",
            "0.45724*R^2*Tc^2/Pc*(1+(0.37646+1.54226*w-0.26992*w^2)*(1-Tr^0.5))^2",
            null,
            null,
            null,
            null,
            null,
            null
    };
    private static String param_b[] = {
            "(R*Tc)/(8*Pc)",
            "0.08664*R*Tc/Pc",
            "0.08664*R*Tc/Pc",
            "0.07780*R*Tc/Pc",
            "R*Tc/Pc*(0.083-0.422/Tr^1.6+w*(0.139-0.172/Tr^4.2))",
            "R*Tc/Pc*(0.1445-0.330/Tr-0.1385/Tr^2-0.0121/Tr^3-0.000607/Tr^8+w*(0.139+0.331/Tr^2-0.423/Tr^3-0.008/Tr^8))",
            null,
            null,
            null,
            null
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
        }else if(filter == FILTER_Liquid){
            String[] l = new String[f_Liquid.length];
            for (int i=0; i<f_Liquid.length; i++)
                l[i] = names[f_Liquid[i]];
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
        else if(filter == FILTER_Liquid)
            return f_Liquid[item];
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

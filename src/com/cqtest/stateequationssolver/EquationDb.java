package com.cqtest.stateequationssolver;

public class EquationDb {
	
	private static String names[] = {
		"Van del Waals",
		"RK",
		"SRK",
        "PR",
        "Pitzer",
        "Improved Pitzer"
	};

	private static String equations[] = {
		"(P+a/Vm^2)*(Vm-b)==R*T",
		"P==R*T/(Vm-b)-a/(T^0.5*Vm*(Vm+b))",
		"P==R*T/(Vm-b)-a/(Vm*(Vm+b))",
        "P==R*T/(Vm-b)-a/(Vm*(Vm+b)+b*(Vm-b))",
        "Vm==(1+b*P/(R*T))*R*T/P",
        "Vm==(1+b*P/(R*T))*R*T/P"
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
	
	public static final String[] getNames()
	{
		return names;
	}
	
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
}

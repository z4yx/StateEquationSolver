package com.cqtest.stateequationssolver;

public class EquationDb {
	
	private static String names[] = {
		"Van del Waals",
		"RK",
		"SRK",
        "PR"
	};

	private static String equations[] = {
		"(P+a/Vm^2)*(Vm-b)==R*T",
		"P==R*T/(Vm-b)-a/(T^0.5*Vm*(Vm+b))",
		"P==R*T/(Vm-b)-a/(Vm*(Vm+b))",
        "P==R*T/(Vm-b)-a/(Vm*(Vm+b)+b*(Vm-b))"
	};

    private static String param_a[] = {
            "27/64*(R*Tc)^2/Pc",
            "0.42748*R^2*Tc^2.5/Pc",
            "0.42748*R^2*Tc^2/Pc*(1+(0.480+1.574*w-0.176*w^2)*(1-Tr^0.5))^2",
            "0.45724*R^2*Tc^2/Pc*(1+(0.37646+1.54226*w-0.26992*w^2)*(1-Tr^0.5))^2"
    };
    private static String param_b[] = {
            "(R*Tc)/(8*Pc)",
            "0.08664*R*Tc/Pc",
            "0.08664*R*Tc/Pc",
            "0.07780*R*Tc/Pc"
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

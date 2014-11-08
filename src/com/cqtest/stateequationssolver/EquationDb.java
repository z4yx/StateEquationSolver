package com.cqtest.stateequationssolver;

public class EquationDb {
	
	private static String names[] = {
		"Van del Waals",
		"RK",
		"SRK"
	};

	private static String equations[] = {
		"(P+a/Vm^2)*(Vm-b)==R*T,a==27/64*(R*Tc)^2/Pc,b==(R*Tc)/(8*Pc)",
		"P==R*T/(Vm-b)-a/(T^0.5*Vm*(Vm+b)),a==0.42748*R^2*Tc^2.5/Pc,b==0.08664*R*Tc/Pc",
		"P==R*T/(Vm-b)-(0.42748*R^2*Tc^2.5/Pc*(1+m(1-Tr^0.5))^2)/(Vm*(Vm+b)),m==0.480+1.574*w-0.176*w^2,b==0.08664*R*Tc/Pc"
	};
	
	public static final String[] getNames()
	{
		return names;
	}
	
	public static final String getEquation(int i)
	{
		return equations[i];
	}
}

package org.fixme.core.utils;

public class Utils {

	public static boolean isnumeric(String str)
	{
		@SuppressWarnings("unused")
		float value;
		try {
			value = Float.parseFloat(str);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
}

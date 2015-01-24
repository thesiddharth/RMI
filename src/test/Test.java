package test;

public class Test {

	public static void main(String[] args)
	{
		String a = "111";
		String b = "111";
		
		Test a1 = new Test();
		System.out.println(a1.add(a, b));
	}
	
	public String add(String a , String b)
	{
		char[] aBits = a.toCharArray();
		char[] bBits = b.toCharArray();
		char[] ret = new char[aBits.length];
		
		int carry = 0;
		for(int i = aBits.length -1 ; i >=0 ; i--)
		{
			if(aBits[i] == bBits[i])
			{
				ret[i] = '0';
				if(aBits[i] == 1)
					carry = 1;
				else carry = 0;
			}
			else
				ret[i] = '1';
		}
		
		return new String(ret);
	
	}
	
}

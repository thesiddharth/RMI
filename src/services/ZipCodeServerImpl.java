/**
 * 
 */
package services;

import interfaces.ZipCodeServer;
import services.ZipCodeList.ZipCode;

/**
 * Sample implementation for {@link ZipCodeServer}.
 * @author surajd
 *
 */
public class ZipCodeServerImpl implements ZipCodeServer {
	
	private static final long serialVersionUID = -6386128010761053115L;
	ZipCodeList zipCodeList;
	
	@Override
	public void initialise(ZipCodeList newlist) {
		this.zipCodeList = newlist;	
	}

	@Override
	public String find(String city) {
		for (ZipCode zipCode : zipCodeList)
		{
			if(city.equals(zipCode.getCity()))
					return zipCode.getZipCode();
		}
		return null;
	}

	@Override
	public ZipCodeList findAll() {
		return zipCodeList;
	}

	@Override
	public void printAll() {
		System.out.println();
		for(ZipCode zipCode : zipCodeList)
			System.out.println(zipCode);
		System.out.println();
	}

}

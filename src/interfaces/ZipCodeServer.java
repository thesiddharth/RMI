package interfaces;

import data.RMIException;
import services.ZipCodeList;

public interface ZipCodeServer extends Remote640 {
	
	 public void initialise(ZipCodeList newlist) throws RMIException;
	 public String find(String city) throws RMIException;
	 public ZipCodeList findAll() throws RMIException;
	 public void printAll() throws RMIException;

}

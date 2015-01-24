/**
 * 
 */
package data;

/**
 * @author surajd
 *
 */
public class RMIException extends Exception {

	private static final long serialVersionUID = -128282937055597091L;
	
	public RMIException(String message, Throwable t)
	{
		super(message ,t);
	}
	
	public RMIException(Throwable t)
	{
		super(t);
	}

}

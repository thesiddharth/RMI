/**
 * 
 */
package data;

/**
 * 
 * @author surajd
 *
 */
public class RMIInvocationReply extends RMIMessage {

	private static final long serialVersionUID = 7821362789177372784L;
	
	// the result of the computation.
	private Object result;
	//private type of result;
	private Class<?> resultType;


	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}


	/**
	 * @return the resultType
	 */
	public Class<?> getResultType() {
		return resultType;
	}

	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}

}

/**
 * 
 */
package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a list of services in the registry.
 * @author surajd
 *
 */
public class ServicesList implements Serializable {

	private static final long serialVersionUID = 1754012673076007665L;
	
	private List<Service> services;
	
	public ServicesList()
	{
		services = new LinkedList<Service>();
	}
	
	public void addService(String  serviceName, String serviceInfo)
	{
		services.add(new Service(serviceName, serviceInfo));
	}
	
	public List<Service> getServices()
	{
		return services;
	}
	
	public String printServices()
	{
		StringBuilder builder = new StringBuilder();
		for(Service service : services)
		{
			builder.append(service);
			builder.append("\n");
		}
		return builder.toString();
	}

}

class Service implements Serializable
{
	private static final long serialVersionUID = -6591732703051580614L;
	String serviceName;
	String serviceInfo;
	
	public Service()
	{
		
	}
	
	public Service(String serviceName , String serviceInfo)
	{
		this.serviceName = serviceName;
		this.serviceInfo = serviceInfo;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the serviceInfo
	 */
	public String getServiceInfo() {
		return serviceInfo;
	}
	/**
	 * @param serviceInfo the serviceInfo to set
	 */
	public void setServiceInfo(String serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Service [serviceName=" + serviceName + ", serviceInfo="
				+ serviceInfo + "]";
	}
	
}

package services;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import services.ZipCodeList.ZipCode;

public class ZipCodeList implements Iterable<ZipCode> , Serializable {
	
	private static final long serialVersionUID = -8177468853016488243L;

	static class ZipCode implements Serializable
	{
		private static final long serialVersionUID = -8143167742996628235L;

		String city;
		
		String zipCode;
		
		ZipCode(String city, String zipCode)
		{
			this.city = city;
			this.zipCode = zipCode;
		}
		/**
		 * @return the city
		 */
		public String getCity() {
			return city;
		}

		/**
		 * @param city the city to set
		 */
		public void setCity(String city) {
			this.city = city;
		}
		/**
		 * @return the zipCode
		 */
		public String getZipCode() {
			return zipCode;
		}
		/**
		 * @param zipCode the zipCode to set
		 */
		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ZipCode [city=" + city + ", zipCode=" + zipCode + "]";
		}

		
		
	}
	
	private List<ZipCode> zipCodeList;
	
	public ZipCodeList()
	{
		zipCodeList = new LinkedList<ZipCode>();
	}
	
	public void add(String city, String zipCode)
	{
		zipCodeList.add(new ZipCode(city, zipCode));
	}
	
	public List<ZipCode> getZipCodeList()
	{
		return zipCodeList;
	}

	@Override
	public Iterator<ZipCode> iterator() {
		return zipCodeList.iterator();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZipCodeList [zipCodeList=" + zipCodeList + "]";
	}
	
	

}

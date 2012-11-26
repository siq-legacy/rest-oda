package oda.datatools.connectivity.rest.impl;

public class ColumnNameMapping {

	/*
	 * This Class is used for the Following:
	 * 		This is the class from which user will be able to provide input to the ColumnNameMapping vector in AccessPattern class.
	 * 		1.sourceKey	->a key which is used as the sourcekey for the request in the level.
	 * 		
	 */
	private String sourceKey;
	private String sourceValue;
	private String destinationKey;

	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
	public String getSourceValue() {
		return sourceValue;
	}
	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}
	public String getDestinationKey() {
		return destinationKey;
	}
	public void setDestinationKey(String destinationKey) {
		this.destinationKey = destinationKey;
	}

	

}

package dev.abreu.bankapp.utils;

public enum ResourceType {
	CUSTOMER("Customer"),
	ACCOUNT("Account"),
	TRANSACTION("Transaction");
	
	private final String resourceName;
	
	ResourceType(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}
	
}

package dev.abreu.bankapp.util;

/**
 * Represents the different types of resources.
 * 
 * @author Devin Abreu
 */
public enum ResourceType {
	CUSTOMER("Customer"),
	ACCOUNT("Account"),
	TRANSACTION("Transaction"),
	TRANSFER("Transfer");
	
	private final String resourceName;
	
    /**
     * Constructs a new ResourceType instance.
     *
     * @param resourceName The name of the resource.
     */
	ResourceType(String resourceName) {
		this.resourceName = resourceName;
	}

    /**
     * Returns the name of the resource.
     *
     * @return The name of the resource.
     */
	public String getResourceName() {
		return resourceName;
	}
	
}

package com.mdsol.skyfire;

import java.util.List;

/**
 * A class that represents an object of a mapping.
 * 
 * @author Nan Li
 * @version 1.0 Nov 12, 2012
 *
 */
public class Mapping {
    // name of the mapping
    private String name;
    // type of the mapped identifiable element
    private IdentifiableElementType type;
    // name of the identifiable element
    private String identifiableElementName;
    // test code that is mapped to
    private String testCode;
    // other mappings that are required for this mapping
    private List<String> requiredMappings;
    // mappings that are used as method parameters
    // the parameters also appear in the list of required mappings
    // the parameters are listed separately for the ease of testing different
    // oracle methods
    private List<String> parameters;
    // mappings for objects that call methods
    private List<String> callers;
    // mappings for objects that are returned by methods
    private List<String> returnObjects;

    /**
     * Allocates a {@link Mapping} object and initialize it to represent the the
     * mapping
     * 
     * @param mappingName
     *            the name of the mapping
     * @param type
     *            the type of the identifiable element
     * @param identifiableElementName
     *            the name of the identifiable element
     * @param testCode
     *            the mapped test code
     * @param requiredMappings
     *            the required mappings for this mapping
     * @param parameters
     *            the names of object mappings that are used to be method
     *            parameters
     * @param callers
     *            the names of object mappings that are used to call methods
     * @param returnObjects
     *            the names of object mappings that are returned by methods
     */
    public Mapping(String mappingName, IdentifiableElementType type, String identifiableElementName, String testCode,
	    List<String> requiredMappings, List<String> parameters, List<String> callers, List<String> returnObjects) {
	this.name = mappingName;
	this.type = type;
	this.identifiableElementName = identifiableElementName;
	this.testCode = testCode;
	this.requiredMappings = requiredMappings;
	this.parameters = parameters;
	this.setCallers(callers);
	this.setReturnObjects(returnObjects);
    }

    /**
     * Constructs a Mapping object with no parameters
     */
    public Mapping() {

    }

    /**
     * Gets the mapping name
     * 
     * @return the mapping name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the mapping name
     * 
     * @param mappingName
     *            the name of the mapping
     */
    public void setName(String mappingName) {
	this.name = mappingName;
    }

    /**
     * Gets the identifiable element type
     * 
     * @return the type of the identifiable element
     */
    public IdentifiableElementType getType() {
	return type;
    }

    /**
     * Sets the identifiable element type
     * 
     * @param type
     *            the type of the identifiable element
     */
    public void setType(IdentifiableElementType type) {
	this.type = type;
    }

    /**
     * Gets the name of the identifiable element
     * 
     * @return the name of the identifiable element
     */
    public String getIdentifiableElementName() {
	return identifiableElementName;
    }

    /**
     * Sets the name of the identifiable element
     * 
     * @param identifiableElementName
     *            the name of the identifiable element
     */
    public void setIdentifiableElementName(String identifiableElementName) {
	this.identifiableElementName = identifiableElementName;
    }

    /**
     * Gets the test code
     * 
     * @return the mapped test code
     */
    public String getTestCode() {
	return testCode;
    }

    /**
     * Sets the test code
     * 
     * @param testCode
     *            the mapped test code
     */
    public void setTestCode(String testCode) {
	this.testCode = testCode;
    }

    /**
     * Gets the required mappings for this mapping
     * 
     * @return a list of names of required mappings
     */
    public List<String> getRequiredMappings() {
	return requiredMappings;
    }

    /**
     * Sets the required mappings
     * 
     * @param requiredMappings
     *            the required mappings for this mapping
     */
    public void setRequiredMappings(List<String> requiredMappings) {
	this.requiredMappings = requiredMappings;
    }

    /**
     * Returns the parameters used in this mapping
     * 
     * @return a list of {@link String}s
     */
    public List<String> getParameters() {
	return parameters;
    }

    /**
     * Sets the parameters
     * 
     * @param parameters
     *            the parameters used in this mapping
     */
    public void setParameters(List<String> parameters) {
	this.parameters = parameters;
    }

    /**
     * Gets the objects that call methods
     * 
     * @return a list of names of the objects that call methods in a String
     *         format
     */
    public List<String> getCallers() {
	return callers;
    }

    /**
     * Gets the objects that are returned by methods
     * 
     * @return a list of names of the objects that call methods in a String
     *         format
     */
    public List<String> getReturnObjects() {
	return returnObjects;
    }

    /**
     * Sets the names of the callers
     * 
     * @param callers
     *            a list of names of callers
     */
    public void setCallers(List<String> callers) {
	this.callers = callers;
    }

    /**
     * Sets the names of return objects
     * 
     * @param returnObjects
     *            a list of return objects
     */
    public void setReturnObjects(List<String> returnObjects) {
	this.returnObjects = returnObjects;
    }

}

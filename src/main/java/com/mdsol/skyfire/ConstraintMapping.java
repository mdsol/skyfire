/**
 * 
 */
package com.mdsol.skyfire;

import java.util.List;

/**
 * A {@link ConstraintMapping} class that maps test code to a constraint.
 * 
 * @author Nan Li
 * @version 1.0 Feb 19, 2013
 *
 */
public class ConstraintMapping extends Mapping {

	//names of the mappings that can be used to solve the constraint
	private List<String> constSolvingMappings;
	//names of the mappings that use this constraint as preconditions
	private List<String> preconditions;
	//names of the mappings that use this constraint as postconditions
	private List<String> postconditions;
	//names of the mappings that use this constraint as state invariants
	private List<String> stateinvariants;
	
	/**
	 * 
	 * @param mappingName
	 * @param type
	 * @param identifiableElementName
	 * @param testCode
	 * @param requiredMappings
	 * @param parameters
	 * @param callers
	 * @param returnObjects
	 * @param constSolvingMappings
	 * @param preconditions
	 * @param postconditions
	 * @param stateinvariants
	 */
	public ConstraintMapping(String mappingName, IdentifiableElementType type,
			String identifiableElementName, String testCode,
			List<String> requiredMappings, List<String> parameters,
			List<String> callers, List<String> returnObjects, List<String> constSolvingMappings, List<String> preconditions, List<String> postconditions, List<String> stateinvariants) {
		super(mappingName, type, identifiableElementName, testCode,
				requiredMappings, parameters, callers, returnObjects);
		this.constSolvingMappings = constSolvingMappings;
		this.preconditions = preconditions;
		this.postconditions = postconditions;
		this.stateinvariants = stateinvariants;
	}

	/**
	 * 
	 */
	public ConstraintMapping() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Gets the names of mappings that used to solve the constraints.
	 * @return	a list of names of the mappings in a String format
	 */
	public List<String> getConstSolvingMappings() {
		return constSolvingMappings;
	}
	/**
	 * Sets the mappings specified by the parameter.
	 * @param constSolvingMappings	a list of names of the mappings that solve the constraint
	 */
	public void setConstSolvingMappings(List<String> constSolvingMappings) {
		this.constSolvingMappings = constSolvingMappings;
	}
	/**
	 * Gets the names of mappings that use this constraint as postconditions
	 * @return	a list of names of the mappings in a String format
	 */
	public List<String> getPostconditions() {
		return postconditions;
	}
	/**
	 * Sets the mappings specified by the parameter
	 * @param postconditions	a list of names of the mappings that use this constraint as postconditions
	 */
	public void setPostconditions(List<String> postconditions) {
		this.postconditions = postconditions;
	}
	/**
	 * Gets the names of mappings that use this constraint as preconditions
	 * @return	a list of names of the mappings in a String format
	 */
	public List<String> getPreconditions() {
		return preconditions;
	}
	/**
	 * Sets the mappings specified by the parameter
	 * @param preconditions	a list of names of the mappings that use this constraint as preconditions
	 */
	public void setPreconditions(List<String> preconditions) {
		this.preconditions = preconditions;
	}
	/**
	 * Gets the names of mappings that use this constraint as state invariants
	 * @return	a list of names of the mappings in a String format
	 */
	public List<String> getStateinvariants() {
		return stateinvariants;
	}
	/**
	 * Sets the mappings specified by the parameter
	 * @param stateinvariants	a list of names of the mappings that use this constraint as state invariants
	 */
	public void setStateinvariants(List<String> stateinvariants) {
		this.stateinvariants = stateinvariants;
	}

}
